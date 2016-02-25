package com.freego.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.freego.R;
import com.freego.bean.ImageHotel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by henryye on 1/22/16.
 */
public class HotelList_ListView_adapter extends BaseAdapter {

    private List<ImageHotel> myList;

    private List<ImageHotel> filterdList;

    private LayoutInflater myInflator;

    private List<ImageHotel> convertList;        //存储中间转换的list， 否则报错

    private hotelFilter hotelfilter;

    public HotelList_ListView_adapter(Context context, ArrayList<ImageHotel> list) {
        myList = list;
        convertList = list;
        myInflator = LayoutInflater.from(context);
        filterdList = new ArrayList<ImageHotel>();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        if(myList == null){
            return 0;
        }else{
            return myList.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if(myList == null){
            return null;
        }else{
            return myList.get(position);
        }
    }

    public Filter getFilter(){
        if(hotelfilter == null){
            hotelfilter = new hotelFilter();
            return hotelfilter;
        }else{
            return  hotelfilter;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder holder;

        ImageHotel imageplace = (ImageHotel)getItem(position);

        View view;

        ImageView search;

        if(convertView == null) {
            holder = new Holder();
            view = myInflator.inflate(R.layout.hotellist_adapter_list, null);
            holder.image = (ImageView)view.findViewById(R.id.hotelList_hotelImage);
            holder.hotelName = (TextView)view.findViewById(R.id.hotelList_hotelName);
            holder.hotelAdress = (TextView)view.findViewById(R.id.hotelList_hotelAddress);
            holder.gender = (ImageView)view.findViewById(R.id.hotelList_genderImage);
            holder.week = (TextView)view.findViewById(R.id.hotelList_weekDuration);
            holder.timeStart = (TextView)view.findViewById(R.id.hotelList_timeFrom);
            view.setTag(holder);

        }else {
            view = convertView;
            holder = (Holder)view.getTag();

        }

        holder.hotelName.setText(imageplace.getHotelName());
        holder.hotelAdress.setText(imageplace.getHotelAdress());
        holder.image.setImageResource(imageplace.getImageID());
        if(imageplace.getGender() == 1){
            holder.gender.setImageResource(R.drawable.public_male);
        }else if(imageplace.getGender() == 2){
            holder.gender.setImageResource(R.drawable.public_female);
        }
        holder.week.setText(""+imageplace.getWeek());
        SimpleDateFormat format = new SimpleDateFormat("yyMM");
        SimpleDateFormat format1 = new SimpleDateFormat("yy-MM");
        try {
            Date date = format.parse(""+imageplace.getTimeStart());
            holder.timeStart.setText("20" + format1.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return view;

    }
    class Holder{
        private ImageView image;
        private TextView hotelName;
        private TextView hotelAdress;
        private TextView week;
        private TextView timeStart;
        private ImageView gender;

    }

    class hotelFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            String info = constraint.toString();
            filterdList = new ArrayList<ImageHotel>();
            myList = convertList;

            String startTime = info.substring(3,7);
            String endTime = info.substring(9,13);
            String gender = info.substring(0, 1);
            String week = info.substring(13, info.length());
            String weekStart = "0";
            String weekEnd = "0";
            Log.d("st", startTime);
            Log.d("et", endTime);
            Log.d("gd", gender);
            Log.d("week", week);
            Log.d("ws", weekStart);


            if(!week.equals("0")) {

                String[] weeks = week.split("0");
                weekStart = weeks[0];
                weekEnd = weeks[1];
                Log.d("---------", weekStart);
            }
            filterHotel(weekStart, weekEnd, gender, startTime, endTime, filterdList, myList);
            results.values = filterdList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            myList = (ArrayList<ImageHotel>)results.values;
            if(results.count > 0){
                notifyDataSetChanged();
            }else {
                notifyDataSetInvalidated();
            }

        }
    }

    public void filterHotel(String week1, String week2, String sex, String time1, String time2,List<ImageHotel> unfilter, List<ImageHotel> filter) {
        int weekStart = Integer.parseInt(week1);
        int weekEnd = Integer.parseInt(week2);
        int gender = Integer.parseInt(sex);
        int startTime = Integer.parseInt(time1);
        int endTime = Integer.parseInt(time2);

        if (gender == 0 & weekStart != 0 & startTime != 0) {
            Log.d("ENTER-----1", "-------");
            for (int i = 0; i < filter.size(); i++) {
                if (weekStart < filter.get(i).getWeek() & filter.get(i).getWeek() < weekEnd) {
                    if (startTime < filter.get(i).getTimeStart() & filter.get(i).getTimeStart() < endTime) {
                        unfilter.add(filter.get(i));
                    }
                }
            }
        } else if (gender != 0 & weekStart == 0 & startTime != 0) {
            Log.d("ENTER-----2", "-------");
            for (int i = 0; i < filter.size(); i++) {
                if (gender == filter.get(i).getGender()) {
                    if (startTime < filter.get(i).getTimeStart() & filter.get(i).getTimeStart() < endTime) {
                        unfilter.add(filter.get(i));
                    }
                }
            }
        } else if (gender != 0 & weekStart != 0 & startTime == 0) {
            Log.d("ENTER-----3", "-------");
            for (int i = 0; i < filter.size(); i++) {
                if (weekStart < filter.get(i).getWeek() & filter.get(i).getWeek() < weekEnd) {
                    if (startTime < filter.get(i).getTimeStart() & filter.get(i).getTimeStart() < endTime) {
                        unfilter.add(filter.get(i));
                    }
                }
            }
        } else if (gender != 0 & weekStart != 0 & startTime != 0) {
            for (int i = 0; i < filter.size(); i++) {
                if (weekStart < filter.get(i).getWeek() & filter.get(i).getWeek() < weekEnd) {
                    if (startTime < filter.get(i).getTimeStart() & filter.get(i).getTimeStart() < endTime) {
                        if (gender == filter.get(i).getGender()) {
                            unfilter.add(filter.get(i));
                        }
                    }
                }
            }
        }
    }
}
