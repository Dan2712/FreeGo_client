package com.freego.bean;

public class HomepageContent {

    public String imageName;

    public int imageID;

    public String imageType;

    public HomepageContent(String placeName, int imageID, String imageType){
        this.imageName = placeName;
        this.imageID = imageID;
        this.imageType = imageType;
    }

    public int getImageID(){
        return imageID;
    }

    public String getImageName(){
        return imageName;
    }

    public String getImageType(){
        return imageType;
    }

}
