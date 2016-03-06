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
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.GetDataCallback;
import com.freego.R;
import com.freego.adapter.HotelList_ListView_adapter;
import com.freego.bean.ImageHotel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;

public class HotelListActivity extends Activity {

    private static final int FEMALE = 2;

    private static final int MALE = 1;

    private static final int NoWeekChosen = 0;

    private static final int NoGenderChosen = 0;

    private static final int IMAGE_DOWN = 3;

    private ArrayList<ImageHotel> hotels = new ArrayList<ImageHotel>();

    private String destination;

    private ListView places_image;

    private ImageView search;

    private DrawerLayout myDrawer;

    private RadioButton female;

    private RadioButton male;

    private RadioGroup gender;

    private Button reset;

    private Button confirm;

    private EditText week1;

    private EditText week2;

    private Spinner month_start;

    private Spinner year_start;

    private Spinner month_end;

    private Spinner year_end;

    private Calendar rightnow = Calendar.getInstance();

    private String[] years;         //用于spinner初始值得设置

    private String[] months;          //同上

    private String[] subMonths;        // 已经过去的月分不显示

    private Filter filter;

    public String weeks;

    public String time_start;

    public String time_end;

    public String genderInfo;

    private boolean flag = false;

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

        Intent intent = getIntent();
        destination = intent.getStringExtra("destination");

        week2 = (EditText)findViewById(R.id.weekTo);
        week1 = (EditText)findViewById(R.id.weekFrom);
        places_image = (ListView)findViewById(R.id.hotelList_view);
        search = (ImageView)findViewById(R.id.hotelList_search);
        myDrawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        gender = (RadioGroup)findViewById(R.id.drawer_gender);
        female = (RadioButton)findViewById(R.id.drawer_female);
        male = (RadioButton)findViewById(R.id.drawer_male);
        confirm = (Button)findViewById(R.id.drawer_confirm);
        reset = (Button)findViewById(R.id.drawer_reset);
        year_end = (Spinner)findViewById(R.id.spinnerYearTo);
        month_end = (Spinner)findViewById(R.id.spinnerMonthTo);
        year_start = (Spinner)findViewById(R.id.spinnerYearFrom);
        month_start = (Spinner)findViewById(R.id.spinnerMonthFrom);

        initHotels();
        new DownloadImages().execute();

        months = getResources().getStringArray(R.array.months);
        years = getResources().getStringArray(R.array.years);
        year_start.setSelection(setInitialDate(rightnow.get(Calendar.YEAR), years)+1);
        month_start.setSelection(setInitialDate(rightnow.get(Calendar.MONTH), months) + 2);
        filterAdapter = new HotelList_ListView_adapter(this, hotels);
        filter = filterAdapter.getFilter();
        places_image.setAdapter(filterAdapter);
        search.setImageResource(R.drawable.public_search);

        places_image.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent1 = new Intent(HotelListActivity.this, HostInfoActivity.class);
                startActivity(intent1);
            }
        });
        //全部空值的话，设置关掉drawer， 绑定一个输入了，另一个就必须输入
        confirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {     //如果什么都没有填则直接关掉drawer
                if (judgeCondition()) {
                    myDrawer.closeDrawer(Gravity.LEFT);
                }
                else if((!week1.getText().toString().equals("") & week2.getText().toString().equals("")) | (week1.getText().toString().equals("") & !week2.getText().toString().equals("")))
                {
                    Toast.makeText(HotelListActivity.this, "Please Input Week Completely", Toast.LENGTH_SHORT).show();
                }
                else if((!year_end.getSelectedItem().toString().equals("") & month_end.getSelectedItem().toString().equals("")) | (year_end.getSelectedItem().toString().equals("") & !month_end.getSelectedItem().toString().equals("")))
                {
                    Toast.makeText(HotelListActivity.this, "Please Input Year Completely", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (!week1.getText().equals("")) {
                        weeks = week1.getText().toString() + "0" + week2.getText().toString();
                    } else {
                        weeks = ""+ NoWeekChosen;
                    }
                    if (gender.getCheckedRadioButtonId() == R.id.drawer_male) {
                        genderInfo = "" + MALE;
                    } else if (gender.getCheckedRadioButtonId() == R.id.drawer_female) {
                        genderInfo = "" + FEMALE;
                    } else {
                        genderInfo = "" + NoGenderChosen;
                    }
                    if (!year_end.equals("")) {
                        String monthBegining = "";
                        String monthEnd = "";
                        if (Integer.parseInt(month_start.getSelectedItem().toString()) < 10) {
                            monthBegining = "0" + month_start.getSelectedItem().toString();
                        } else {
                            monthBegining = month_start.getSelectedItem().toString();
                        }
                        if (Integer.parseInt(month_end.getSelectedItem().toString()) < 10) {
                            monthEnd = "0" + month_end.getSelectedItem().toString();
                        } else {
                            monthEnd = month_end.getSelectedItem().toString();
                        }
                        time_start = year_start.getSelectedItem().toString() + monthBegining;
                        time_end = year_end.getSelectedItem().toString() + monthEnd;

                    } else {
                        time_start = "0000";
                        time_end = "0000";
                    }
                    filter.filter(genderInfo + time_start + time_end + weeks);//         性别+开始日期+结束日期+最低星期+0+最高星期
                    myDrawer.closeDrawer(Gravity.LEFT);                 //         eg： 1 1601 1603 5 0 9
                }
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDrawer.openDrawer(Gravity.LEFT);
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                year_end.setSelection(0);
                month_end.setSelection(0);
                week2.setText("");
                week1.setText("");
                female.setChecked(false);
                male.setChecked(false);

            }
        });

        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!flag){
                    female.setChecked(true);
                    flag = true;
                }else{
                    female.setChecked(false);
                    flag = false;
                }
            }
        });

        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!flag){
                    male.setChecked(true);
                    flag = true;
                }else{
                    male.setChecked(false);
                    flag = false;
                }
            }
        });

    }


    public int setInitialDate(int date, String[] items){

        for(int i = 0; i < items.length; i++){
                return i;
            }
        return 1;
    }

    private void initHotels(){
        File file = new File(getFilesDir() + File.separator + "hotels" + File.separator + destination);
        File[] files = file.listFiles();
        FileInputStream inputStream = null;
        BufferedReader reader = null;
        JSONObject jsonObject = null;

        try {
            for (int i = 0; i < files.length; i++) {
                inputStream = new FileInputStream(files[i] + File.separator + files[i].getName() + ".txt");
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String content = "";

                while ((content = reader.readLine()) != null) {
                    jsonObject = new JSONObject(content.toString());
                    JSONObject imageObject = jsonObject.getJSONObject("image");
                    String imageId = imageObject.getString("objectId");
                    imagesId.add(imageId);
                    ImageHotel imageHotel = new ImageHotel(null, jsonObject.getString("hotelName"), jsonObject.getString("region")
                            , jsonObject.getInt("requireSex"), jsonObject.getInt("requireNumber"), jsonObject.getInt("startDate"));
                    hotels.add(imageHotel);
                }
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

    public boolean judgeCondition(){
        if(week1.getText().toString().equals("") & week2.getText().toString().equals("")
                & female.isChecked() == false & male.isChecked() == false
                & month_end.getSelectedItem().toString().equals("")
                & year_end.getSelectedItem().toString().equals("")){

            return true;
        }else {
            return false;
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

                                    Message msg = new Message();
                                    msg.what = IMAGE_DOWN;
                                    handler.sendMessage(msg);

                                    try {
                                        File file = new File("data/data/com.freego/files/hotels/" + destination
                                                + File.separator + hotels.get(finalI).getHotelName() + File.separator + "image1");
                                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                                        fileOutputStream.flush();
                                        fileOutputStream.close();
                                    } catch (IOException e1) {
                                        e1.printStackTrace();
                                    }
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
