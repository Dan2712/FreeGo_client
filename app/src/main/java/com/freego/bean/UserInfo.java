package com.freego.bean;

import android.graphics.Bitmap;

import java.io.Serializable;

public class UserInfo implements Serializable {

    private String userName;

    private Bitmap userIcon;

    public UserInfo(String userName, Bitmap userIcon){
        this.userIcon = userIcon;
        this.userName = userName;
    }

    public Bitmap getUserIcon(){
        return userIcon;
    }

    public String getUserName(){
        return userName;
    }
}
