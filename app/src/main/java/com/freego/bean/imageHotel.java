package com.freego.bean;

/**
 * Created by henryye on 1/22/16.
 */
public class ImageHotel {

    private String placeName;

    private int imageID;

    private String hotelName;

    private String hotelAdress;

    private int week;

    private int timeStart;

    private int gender;

    private int timeEnd;


    public ImageHotel(int imageID, String info1, String info2, int gender, int week, int timeStart){
        hotelName = info1;
        hotelAdress = info2;
        this.imageID = imageID;
        this.gender = gender;
        this.week = week;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
    }

    public String getHotelName(){ return  hotelName;  }

    public String getHotelAdress(){ return  hotelAdress;  }

    public int getImageID(){
        return imageID;
    }

    public int getGender(){
        return gender;
    }


    public int getWeek(){
        return week;
    }

    public int getTimeStart(){
        return timeStart;
    }

    public int getTimeEnd(){
        return timeStart;
    }


    public void setHotelName(String name){
        this.hotelName = name;
    }

    public void setHotelAdress(String adress){
        this.hotelAdress = adress;
    }

    public void setImageID(int id){
        this.imageID = id;
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
