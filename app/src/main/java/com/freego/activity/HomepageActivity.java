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
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.GetDataCallback;
import com.freego.R;
import com.freego.adapter.Homepage_RecycleView_adapter;
import com.freego.app.GlobalApplication;
import com.freego.bean.ImagePlace;
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

public class HomepageActivity extends Activity {

    private static final int AVATAR_UPDATE = 0;

    private ArrayList<ImagePlace> places = new ArrayList<ImagePlace>();

    private ArrayList<String> hintItems = new ArrayList<String>();

    private CircleImageView circleImageView;

    private RecyclerView mRecyclerView;

    private RecyclerView.Adapter mAdapter;

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

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case AVATAR_UPDATE:
                    circleImageView.setImageBitmap(avatar);
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

        initPlaces();
        initHintItems();

        mAdapter = new Homepage_RecycleView_adapter(places);
        mRecyclerView.setAdapter(mAdapter);
        circleImageView = (CircleImageView) findViewById(R.id.circleImageView);

        searchBox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent jump = new Intent(HomepageActivity.this, HotelListActivity.class);
                startActivity(jump);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });

        searchBox.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN){
                    Intent jump = new Intent(HomepageActivity.this, HotelListActivity.class);
                    startActivity(jump);
                    overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
                    return true;
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

    public void initPlaces(){
        ImagePlace bj = new ImagePlace("BJ", R.drawable.homepage_beijing);
        places.add(bj);
        ImagePlace hk = new ImagePlace("hk", R.drawable.homepage_hongkong);
        places.add(hk);
        ImagePlace hn = new ImagePlace("hn", R.drawable.homepage_hunan);
        places.add(hn);
        ImagePlace sh = new ImagePlace("sh", R.drawable.homepage_shanghai);
        places.add(sh);
        ImagePlace tw = new ImagePlace("tw", R.drawable.homepage_taiwan);
        places.add(tw);
        ImagePlace xz = new ImagePlace("xz", R.drawable.homepage_xizhang);
        places.add(xz);
        ImagePlace yn = new ImagePlace("yn", R.drawable.homepage_tunnan);
        places.add(yn);

    }

    public void initHintItems(){

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
                    try {
                        JSONObject content = new JSONObject();

                        content.put("age", user_type.getInt("age"));
                        content.put("sex", user_type.getString("sex"));
                        content.put("region", user_type.getString("region"));
                        content.put("introduction", user_type.getString("introduction"));

                        File file = new File(HomepageActivity.this.getFilesDir() + File.separator + "profile" + File.separator + userName + File.separator + userName + ".txt");
                        if (!file.getParentFile().exists())
                            file.getParentFile().mkdirs();

                        PrintStream out = new PrintStream(new FileOutputStream(file));
                        out.print(content.toString());
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    } catch (FileNotFoundException e2) {
                        e2.printStackTrace();
                    }
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

                                Message msg = new Message();
                                msg.what = AVATAR_UPDATE;
                                handler.sendMessage(msg);

                                try {
                                    File file = new File("data/data/com.freego/files/profile/" + userName + "/avatar_" + userName);
                                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                                    avatar.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                                    fileOutputStream.flush();
                                    fileOutputStream.close();
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        });
                    }
                }
            });
        }
    }
}
