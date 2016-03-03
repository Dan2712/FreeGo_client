package com.freego.bean;

import android.widget.ImageView;

public class HintPlace {
    private String places;
    private ImageView transparentHead;

    public HintPlace(String p, ImageView t){
        places = p;
        transparentHead = t;
    }


    public String getPlaces(){
        return places;
    }

    public ImageView getHead(){
        return transparentHead;
    }
}
