package com.freego.bean;

/**
 * Created by henryye on 1/22/16.
 */
public class ImagePlace {

    public String placeName;

    public int imageID;

    public ImagePlace(String placeName, int imageID){
        this.placeName = placeName;
        this.imageID = imageID;
    }

    public int getImageID(){
        return imageID;
    }

    public String getPlaceName(){
        return placeName;
    }

}
