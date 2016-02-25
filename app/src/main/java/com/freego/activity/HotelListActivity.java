package com.freego.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.freego.R;
import com.freego.adapter.HotelList_ListView_adapter;
import com.freego.bean.ImageHotel;

import java.util.ArrayList;
import java.util.Calendar;

public class HotelListActivity extends Activity {

    private static final int FEMALE = 2;

    private static final int MALE = 1;

    private static final int NoWeekChosen = 0;

    private static final int NoGenderChosen = 0;

    private ArrayList<ImageHotel> places = new ArrayList<ImageHotel>();

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_hotellist);

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


        iniPlaces();
        months = getResources().getStringArray(R.array.months);
        years = getResources().getStringArray(R.array.years);
        year_start.setSelection(setInitialDate(rightnow.get(Calendar.YEAR), years)+1);
        month_start.setSelection(setInitialDate(rightnow.get(Calendar.MONTH), months) + 2);
        final HotelList_ListView_adapter filterAdapter = new HotelList_ListView_adapter(this, places);
        filter = filterAdapter.getFilter();
        places_image.setAdapter(filterAdapter);
        search.setImageResource(R.drawable.public_search);

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

    public void iniPlaces(){
        ImageHotel bj = new ImageHotel(R.drawable.aaa,"Sala Old Town Singharat Road","ChengDu, SiChuan Province", 1, 4, 1609);

        places.add(bj);
        ImageHotel hk = new ImageHotel(R.drawable.aab,"Amaka Deluxe 3A","ChangSha ,HuNan Province", 2, 7, 1702);
        places.add(hk);
        ImageHotel hn = new ImageHotel(R.drawable.aac,"condo near nimmana","BaoA, ShenZhen", 1, 4, 1711);
        places.add(hn);
        ImageHotel sh = new ImageHotel(R.drawable.aad,"Great nice location","Kowloon Bay, HongKong", 2, 5, 1610);
        places.add(sh);
        ImageHotel tw = new ImageHotel(R.drawable.aae,"Swim Pool, MR","WenZhou, ZheJiang Province", 1, 2, 1711);
        places.add(tw);
        ImageHotel xz = new ImageHotel(R.drawable.aaf,"Just Seoul Stn.","HuHeHaoTe, NeiMengGu Province", 0, 6, 1705);
        places.add(xz);
        ImageHotel yn = new ImageHotel(R.drawable.aag,"QueenBedRoom","GuangZhou, GuangDong Province", 2, 8, 1812);
        places.add(yn);

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
}
