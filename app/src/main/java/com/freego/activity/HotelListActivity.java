package com.freego.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetDataCallback;
import com.freego.R;
import com.freego.adapter.HotelList_ListView_adapter;
import com.freego.bean.ImageHotel;
import com.freego.util.FileUtil;
import com.freego.view.CircleImageView;
import com.freego.view.HeaderContainedList;
import com.freego.view.HotelList_NiceSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class HotelListActivity extends Activity {

    private final static int FEMALE = 2;

    private final static int MALE = 1;

    private final static int NoGenderChosen = 0;

    private final static int IMAGE_DOWN = 3;

    private ArrayList<ImageHotel> hotels = new ArrayList<ImageHotel>();

    private HeaderContainedList places_image;

    private ImageView search;

    private DrawerLayout myDrawer;

    private RadioButton female;

    private RadioButton male;

    private RadioGroup gender;

    private Button reset;

    private Button confirm;

    private EditText week1;

    private EditText week2;

    private HotelList_NiceSpinner month_start;

    private HotelList_NiceSpinner year_start;

    private HotelList_NiceSpinner month_end;

    private HotelList_NiceSpinner year_end;

    private String[] years;

    private String[] months;

    private Filter filter;

    public String weeks;

    public String time_start;

    public String time_end;

    public String genderInfo;

    private TextView headerImage;

    private TextView locationText;

    private TextView userText;

    private RelativeLayout searchAndCircle;

    private ImageView header;

    private LinearLayout headerText;

    private CircleImageView circleImageView;

    private String city;

    private HotelList_ListView_adapter filterAdapter;

    private ArrayList<String> imagesId = new ArrayList<>();

    private Bitmap bitmap;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case IMAGE_DOWN:
                    filterAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_hotellist);

        final Intent intent = getIntent();
        city = intent.getStringExtra("city");
        String username = AVUser.getCurrentUser().get("username").toString();

        week2 = (EditText)findViewById(R.id.week2);
        week1 = (EditText)findViewById(R.id.week1);

        searchAndCircle = (RelativeLayout) findViewById(R.id.hotelList_circleAndSearch);
        headerImage = (TextView) findViewById(R.id.hotelList_locationText);
        locationText = (TextView) findViewById(R.id.hotelList_locationText);
        locationText.setText(city);

        circleImageView = (CircleImageView) findViewById(R.id.hotellist_circleImageView);
        Bitmap user_avatar = FileUtil.readImage("profile" + File.separator + username + File.separator + "avatar_" + username);
        circleImageView.setImageBitmap(user_avatar);

        places_image = (HeaderContainedList)findViewById(R.id.hotelList_placeListview);
        search = (ImageView)findViewById(R.id.hotelList_search);
        myDrawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        gender = (RadioGroup)findViewById(R.id.drawer_gender);
        female = (RadioButton)findViewById(R.id.female);
        male = (RadioButton)findViewById(R.id.male);
        confirm = (Button)findViewById(R.id.confirmFilter);
        reset = (Button)findViewById(R.id.resetFilter);
        year_end = (HotelList_NiceSpinner)findViewById(R.id.spinnerYear2);
        month_end = (HotelList_NiceSpinner)findViewById(R.id.spinnerMonth2);
        year_start = (HotelList_NiceSpinner)findViewById(R.id.spinnerYear1);
        month_start = (HotelList_NiceSpinner)findViewById(R.id.spinnerMonth1);

        initHotels();
        new DownloadImages().execute();

        months = getResources().getStringArray(R.array.months);
        years = getResources().getStringArray(R.array.years);
        List<String> months_list = new LinkedList<>(Arrays.asList(months));
        month_start.attachDataSource(months_list);
        month_end.attachDataSource(months_list);

        List<String> years_list = new LinkedList<>(Arrays.asList(years));
        year_start.attachDataSource(years_list);
        year_end.attachDataSource(years_list);

        filterAdapter = new HotelList_ListView_adapter(this, hotels);
        filter = filterAdapter.getFilter();


        LayoutInflater headerInflater = (LayoutInflater)this.getLayoutInflater();
        header = (ImageView)headerInflater.inflate(R.layout.hotellist_header_item, null);
        LayoutInflater headerTextInflater = (LayoutInflater)this.getLayoutInflater();
        headerText = (LinearLayout)headerInflater.inflate(R.layout.hotellist_header_item2, null);
        places_image.addHeaderView(header);
        places_image.addHeaderView(headerText);
        places_image.setFinalTopHeight(450);
        userText = (TextView) headerText.findViewById(R.id.hotellist_header_user);
        userText.setText(username);

        places_image.setAdapter(filterAdapter);
        places_image.setFocusable(false);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDrawer.openDrawer(Gravity.LEFT);
            }
        });

        places_image.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                int alphafloat = header.getTop();
                int absAlpha = Math.abs(alphafloat);
                if (absAlpha / 2 > 30 & absAlpha / 2 < 255) {
                    headerImage.getBackground().setAlpha(255 - absAlpha / 2);

                    int a = 255 - absAlpha / 2;
                }
                if (absAlpha < 100) {
                    headerImage.getBackground().setAlpha(255);
                }
            }

        });

        places_image.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent1 = new Intent(HotelListActivity.this, HostInfoActivity.class);
                intent1.putExtra("hotelName", hotels.get(position - 2).getHotelName());
                intent1.putExtra("city", city);
                startActivity(intent1);
                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                finish();
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                month_start.setSelectedIndex(0);
                year_start.setSelectedIndex(0);
                year_end.setSelectedIndex(0);
                month_end.setSelectedIndex(0);
                week2.setText("");
                week1.setText("");
                female.setChecked(false);
                male.setChecked(false);

            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDrawer.closeDrawer(Gravity.LEFT);
                if(gender.getCheckedRadioButtonId() == R.id.female){
                    genderInfo = "" + FEMALE;
                }else if(gender.getCheckedRadioButtonId() == R.id.male){
                    genderInfo = "" + MALE;
                } else {
                    genderInfo = "" + NoGenderChosen;
                }
                time_start = year_start.getSelectedIndex()+15 +"" + "0"+(month_start.getSelectedIndex()+1);
                time_end = year_end.getSelectedIndex()+15 +"" + "0"+(month_end.getSelectedIndex()+1);
                weeks = week1.getText()+"0"+week2.getText();
                filter.filter(genderInfo + time_start + time_end + weeks);
                myDrawer.closeDrawer(Gravity.LEFT);
                filterAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initHotels(){
        File file = new File(getFilesDir() + File.separator + "hotels" + File.separator + city);
        File[] files = file.listFiles();

        if (files == null)
            return;

        FileInputStream inputStream = null;
        BufferedReader reader = null;
        JSONObject jsonObject = null;

        try {
            for (int i = 0; i < files.length; i++) {
                inputStream = new FileInputStream(files[i] + File.separator + files[i].getName() + ".txt");
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                StringBuilder content = new StringBuilder();

                while ((line = reader.readLine()) != null)
                    content.append(line);

                jsonObject = new JSONObject(content.toString());
                JSONObject imageObject = jsonObject.getJSONObject("image");
                String imageId = imageObject.getString("objectId");
                imagesId.add(imageId);
                ImageHotel imageHotel = new ImageHotel(null, jsonObject.getString("hotelName"), jsonObject.getString("region")
                        , jsonObject.getInt("requireSex"), jsonObject.getInt("requireNumber"), jsonObject.getInt("startDate"));
                hotels.add(imageHotel);

            }
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        } catch (JSONException e3) {
            e3.printStackTrace();
        } finally {
            if (reader != null)
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    class DownloadImages extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            for (int i=0; i<imagesId.size(); i++) {
                AVQuery<AVObject> query = new AVQuery<>("image");
                AVObject image;
                try {
                    image = query.get(imagesId.get(i));
                    final int finalI = i;

                    AVFile image_down = image.getAVFile("image1");
                    if (image_down != null) {
                        final int finalI1 = i;
                        image_down.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] bytes, AVException e) {
                                if (e == null) {
                                    bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                    hotels.get(finalI1).setImage(bitmap);
                                    String url = "hotels/" + city + File.separator + hotels.get(finalI).getHotelName() + File.separator + "image1";

                                    Message msg = new Message();
                                    msg.what = IMAGE_DOWN;
                                    handler.sendMessage(msg);

                                    FileUtil.writeImage(url, bitmap);
                                }
                            }
                        });
                    }

                } catch (AVException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }
}
