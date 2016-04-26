package com.freego.bean;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;

public class ChatInfo implements Serializable {

    private Bitmap userIcon;

    private int newMsgHint;

    private String chatUserName;

    private String textHint;

    private ArrayList<MsgInfo> conversation;

    public ChatInfo(String chatUserName, String textHint, Bitmap userIcon, int newMsgHint, ArrayList<MsgInfo> conversation){
        this.conversation = conversation;
        this.textHint = textHint;
        this.userIcon = userIcon;
        this.newMsgHint = newMsgHint;
        this.chatUserName = chatUserName;
    }
    public void setConversation(ArrayList<MsgInfo> list){
        this.conversation = list;
    }

    public ArrayList<MsgInfo> getConversation(){
        return conversation;
    }

    public void setTextHint(String text){
        textHint = text;
    }

    public void setNewMsgHint(int hint){
        newMsgHint = hint;
    }


    public String getChatUserName(){
        return chatUserName;
    }

    public Bitmap getUserIcon(){
        return userIcon;
    }

    public int getNewMsgHint(){
        return newMsgHint;
    }

    public String getTextHint(){
        return textHint;
    }
}
