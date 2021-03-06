package com.freego.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.GetDataCallback;
import com.freego.R;
import com.freego.adapter.Homepage_RecycleView_adapter;
import com.freego.app.GlobalApplication;
import com.freego.bean.HomepageContent;
import com.freego.util.FileUtil;
import com.freego.util.ProgressUtil;
import com.freego.view.CircleImageView;
import com.freego.view.Homepage_ViewSearch;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class HomepageActivity extends Activity {

    private static final int AVATAR_UPDATE = 0;

    private static final int INFO_DOWNLOAD = 1;

    private ArrayList<HomepageContent> contents = new ArrayList<HomepageContent>();

    private ArrayList<String> hintItems = new ArrayList<String>();

    private String city;

    private CircleImageView circleImageView;

    private RecyclerView mRecyclerView;

    private Homepage_RecycleView_adapter mAdapter;

    private ArrayAdapter hintAdapter;

    private AutoCompleteTextView searchBox;

    private LinearLayoutManager mLayoutManager;

    private Homepage_ViewSearch search;

    private static boolean isExpand;

    public static boolean isExpand() {
        return isExpand;
    }

    public static void setIsExpand(boolean isExpand) {
        HomepageActivity.isExpand = isExpand;
    }

    private AVUser currentUser = AVUser.getCurrentUser();

    private AVObject user_type = null;

    private String userName;

    private Bitmap avatar;

    private ImageView alertImage;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case AVATAR_UPDATE:
                    circleImageView.setImageBitmap(avatar);
                    break;
                case INFO_DOWNLOAD:
                    Intent intent = new Intent(HomepageActivity.this, HotelListActivity.class);
                    intent.putExtra("city", city);
                    startActivity(intent);
                    ProgressUtil.dismissProgress();
                    overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_homepage);

        search = (Homepage_ViewSearch) findViewById(R.id.homepage_search_view);
        mRecyclerView = (RecyclerView) findViewById(R.id.homepage_recycler_view);
        alertImage = (ImageView) findViewById(R.id.alertImage);

        isExpand = true;
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                GlobalApplication.setIsPressed(false);
                int firstPosition = mLayoutManager.findFirstCompletelyVisibleItemPosition();
                if (firstPosition != 0 && isExpand && !GlobalApplication.isPressed()) {
                    search.updateShow(false);
                    isExpand = false;
                }
                if (firstPosition == 0 && !isExpand && !GlobalApplication.isPressed()) {
                    search.updateShow(true);
                    isExpand = true;
                }
            }
        });


        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        hintAdapter = new ArrayAdapter(this,R.layout.homepage_hint, hintItems);
        searchBox = (AutoCompleteTextView) findViewById(R.id.search_round);
        searchBox.setAdapter(hintAdapter);
        searchBox.setSingleLine(true);
        searchBox.setDropDownWidth(600);
        searchBox.setThreshold(1);

        initHintItems();
        initContents();

        mAdapter = new Homepage_RecycleView_adapter(contents);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new Homepage_RecycleView_adapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, final HomepageContent content) {
                ProgressUtil.showProgress(HomepageActivity.this);
                if (content.getImageType().equals("city")) {
                    try {
                        city = content.getImageName();
                        downloadHotelInfo(content.getImageName());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (content.getImageType().equals("hotel")) {
                    Intent intent = new Intent(HomepageActivity.this, HostInfoActivity.class);
                    intent.putExtra("city", "Beijing");
                    intent.putExtra("hotelName", content.getImageName());
                    startActivity(intent);
                    ProgressUtil.dismissProgress();
                    overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                } else if (content.getImageType().equals("note")) {
                    Intent intent = new Intent(HomepageActivity.this, NotesActivity.class);
                    startActivity(intent);
                    ProgressUtil.dismissProgress();
                    overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                }
            }
        });

        circleImageView = (CircleImageView) findViewById(R.id.circleImageView);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AVUser.getCurrentUser().getInt("type") == 1) {
                    Intent intent = new Intent(HomepageActivity.this, MyHotelActivity.class);
                    startActivity(intent);
                } else if (AVUser.getCurrentUser().getInt("type") == 0) {
                    Intent intent = new Intent(HomepageActivity.this, MyStoryActivity.class);
                    startActivity(intent);
                }
            }
        });

        searchBox.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    String contentInput = searchBox.getText().toString();
                    byte[] items = contentInput.getBytes();

                    if (items[0] > 96 && items[0] < 123)
                        items[0] = (byte)((char)items[0] - ('a' - 'A'));
                    String contentName = new String(items);
                    ProgressUtil.showProgress(HomepageActivity.this);
                    city = contentName;
                    try {
                        downloadHotelInfo(contentName);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });

        userName = currentUser.getUsername();
        if (currentUser.getInt("type") == 0)
            user_type = currentUser.getAVObject("link_volunteer");
        else
            user_type = currentUser.getAVObject("link_host");

        new DownloadTask().execute();
    }

    private void initContents(){
        HomepageContent beijing = new HomepageContent("Beijing", R.drawable.homepage_beijing, "city");
        contents.add(beijing);
        HomepageContent shanghai = new HomepageContent("Hongkong", R.drawable.homepage_shanghai, "city");
        contents.add(shanghai);
        HomepageContent sichuan = new HomepageContent("Hunan", R.drawable.homepage_sichuan, "city");
        contents.add(sichuan);
        HomepageContent hotHotel01 = new HomepageContent("Shuke Hotel", R.drawable.homepage_hotel01, "hotel");
        contents.add(hotHotel01);
        HomepageContent hotHotel02 = new HomepageContent("Holmes' House", R.drawable.homepage_hotel02, "hotel");
        contents.add(hotHotel02);
        HomepageContent hotHotel03 = new HomepageContent("Beita Hotel", R.drawable.homepage_hotel03, "hotel");
        contents.add(hotHotel03);
        HomepageContent hotNote01 = new HomepageContent("hotNote01", R.drawable.homepage_note1, "note");
        contents.add(hotNote01);
        HomepageContent hotNote02 = new HomepageContent("hotNote02", R.drawable.homepage_note2, "note");
        contents.add(hotNote02);
        HomepageContent hotNote03 = new HomepageContent("hotNote03", R.drawable.homepage_note3, "note");
        contents.add(hotNote03);

    }

    private void initHintItems(){

        hintItems.add("Hongkong");
        hintItems.add("Hunan");
        hintItems.add("Taiwan");
        hintItems.add("Shanghai");
        hintItems.add("Tibet");
        hintItems.add("Yunnan");
        hintItems.add("Guangdong");
        hintItems.add("Zhejiang");
        hintItems.add("Jiangsu");
        hintItems.add("Shandong");
        hintItems.add("Sichuan");
        hintItems.add("Henan");
        hintItems.add("Fujian");
        hintItems.add("Hebei");
        hintItems.add("Anhui");
        hintItems.add("Hainan");
        hintItems.add("Hubei");
        hintItems.add("Jiangxi");
        hintItems.add("Liaoning");
        hintItems.add("Guizhou");
        hintItems.add("Shanxi");
        hintItems.add("Heilongjiang");
        hintItems.add("Gansu");
        hintItems.add("Jilin");
        hintItems.add("Qinghai");
        hintItems.add("Shaanxi");
        hintItems.add("Macau");
        hintItems.add("Beijing");
        hintItems.add("Tianjing");
        hintItems.add("Chongqing");
        hintItems.add("Nei Mongol");
    }

    private void downloadHotelInfo(final String contentName) throws IOException{

        AVQuery<AVObject> query = new AVQuery<AVObject>("host");
        query.whereEqualTo("region", contentName);

        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                try {
                    if (e == null) {
                        for (AVObject avObject : list) {
                            JSONObject jsonObject = avObject.toJSONObject();

                            String hotelName = jsonObject.getString("hotelName");
                            File file = new File(getFilesDir() + File.separator + "hotels" + File.separator
                                    + contentName + File.separator + hotelName + File.separator + hotelName + ".txt");
                            if (!file.getParentFile().exists())
                                file.getParentFile().mkdirs();
                            PrintStream out = new PrintStream(new FileOutputStream(file));

                            out.print(jsonObject);
                        }
                        Message msg = new Message();
                        msg.what = INFO_DOWNLOAD;
                        handler.sendMessage(msg);
                    } else {
                        Log.e("Error: ", e.getMessage());
                    }
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    class DownloadTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            downloadAvatar();
            downloadFile();
            return null;
        }

        private void downloadFile() {
            user_type.fetchIfNeededInBackground(new GetCallback<AVObject>() {
                @Override
                public void done(AVObject avObject, AVException e) {
                    JSONObject content = avObject.toJSONObject();
                    String url = "profile" + File.separator + userName + File.separator + userName + ".txt";

                    FileUtil.writeFile(url, content.toString());
                }
            });
        }

        private void downloadAvatar() {

            user_type.fetchIfNeededInBackground(new GetCallback<AVObject>() {
                @Override
                public void done(AVObject avObject, AVException e) {
                    AVFile avFile = user_type.getAVFile("avatar");
                    if (avFile != null) {
                        avFile.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] bytes, AVException e) {
                                avatar = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                String url = "profile" + File.separator + userName + File.separator + "avatar_" + userName;

                                Message msg = new Message();
                                msg.what = AVATAR_UPDATE;
                                handler.sendMessage(msg);

                                FileUtil.writeImage(url, avatar);
                            }
                        });
                    }
                }
            });
        }
    }

}
