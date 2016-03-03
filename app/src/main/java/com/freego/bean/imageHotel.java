package com.freego.bean;

import android.graphics.Bitmap;

public class ImageHotel {

    private String region;

    private Bitmap image;

    private String hotelName;

    private String hotelAdress;

    private int week;

    private int timeStart;

    private int gender;

    public ImageHotel(Bitmap bitmap, String info1, String info2, int gender, int week, int timeStart){
        this.image = bitmap;
        hotelName = info1;
        hotelAdress = info2;
        this.gender = gender;
        this.week = week;
        this.timeStart = timeStart;
    }

    public String getHotelName(){ return  hotelName;  }

    public String getHotelAdress(){ return  hotelAdress;  }

    public int getGender(){
        return gender;
    }


    public int getWeek(){
        return week;
    }

    public int getTimeStart(){
        return timeStart;
    }

    public String getRegion() {
        return region;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setHotelName(String name){
        this.hotelName = name;
    }

    public void setHotelAdress(String adress){
        this.hotelAdress = adress;
    }

    public void setGender(int gender){
        this.gender = gender;
    }

    public void setWeek(int week){
        this.week = week;
    }

    public void setTimeStart(int timeStart){
        this.timeStart = timeStart;
    }

}
