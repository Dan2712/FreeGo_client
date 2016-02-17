package com.freego.homepage;

/**
 * Created by henryye on 1/22/16.
 */
public class imagePlace {
    String placeName;
    int imageID;

    imagePlace(String placeName, int imageID){
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
