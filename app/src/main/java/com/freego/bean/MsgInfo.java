package com.freego.bean;

import android.graphics.Bitmap;

import java.io.Serializable;

public class MsgInfo implements Serializable {

    private String content;

    private int type;

    private Bitmap userIcon;

    private String userName;

    public MsgInfo(String content, int type, Bitmap userIcon){
        this.content = content;
        this.type = type;
        this.userIcon = userIcon;
        this.userName = userName;
    }

    public int getType(){
        return type;
    }

    public Bitmap getUserIcon(){
        return userIcon;
    }

    public String getContent(){
        return content;
    }

    public Bitmap findIconByName(String name){
        if (name.equals(userName)){
            return userIcon;
        }else
            return null;
    }


}
