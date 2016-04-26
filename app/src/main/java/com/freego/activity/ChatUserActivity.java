package com.freego.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageHandler;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.freego.R;
import com.freego.RecycleOnClickListener.RecycleOnClickListener;
import com.freego.adapter.ChatActivity_RecycleAdapter;
import com.freego.bean.ChatInfo;
import com.freego.bean.MsgInfo;
import com.freego.bean.UserInfo;
import com.freego.fragment.ChatFragment;
import com.freego.util.FileUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

public class ChatUserActivity extends Activity {

    private RecyclerView chatUser;

    private RecyclerView.LayoutManager layoutManager;

    private ChatActivity_RecycleAdapter reAdapter;

    private LinkedList<ChatInfo> chats = new LinkedList<>();

    private UserInfo userInfo = null;

    private ArrayList<String> list ;

    private String conversationID = "";

    private Intent go;

    private final static int CONVERSATION_DONE = 1;

    private AVIMClient newClient;

    private ChatInfo user_other;

    private ArrayList<MsgInfo> listMsg;

    private FragmentManager fragmentManager;

    private FragmentTransaction fragmentTransaction;

    private ChatFragment chatFragment;

    private String conversationName = "hello";

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.arg1 == CONVERSATION_DONE) {
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.fragment_slide_down_enter, R.anim.fragment_slide_up_exit);
                fragmentTransaction.replace(R.id.chat_userLayout, chatFragment);
                fragmentTransaction.commit();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.chatuser_layout);
        AVIMMessageManager.registerDefaultMessageHandler(new CustomMessageHandler());
        String current_user = AVUser.getCurrentUser().get("username").toString();
        Bitmap user_icon = FileUtil.readImage("profile" + File.separator + current_user + File.separator + "avatar_" + current_user);
        userInfo = new UserInfo(current_user, user_icon);
        initialChatInfo();
        chatFragment = new ChatFragment();
        listMsg = new ArrayList<>();

        chatUser = (RecyclerView)findViewById(R.id.recycleUser);

        layoutManager = new LinearLayoutManager(this);
        chatUser.setLayoutManager(layoutManager);
        chatUser.setHasFixedSize(true);
        reAdapter = new ChatActivity_RecycleAdapter(chats, new RecycleOnClickListener() {

            @Override
            public void onClick(View view, final int position) {
                listMsg = new ArrayList<>();
                list  = new ArrayList<>();
                user_other = chats.get(position);
                list.add(userInfo.getUserName());

                list.add(user_other.getChatUserName());
                newClient = AVIMClient.getInstance(userInfo.getUserName());

                newClient.open(new AVIMClientCallback() {
                    @Override
                    public void done(AVIMClient avimClient, AVIMException e) {
                        if (null != e) {
                            e.printStackTrace();
                        } else {
                            Toast.makeText(ChatUserActivity.this, "Login successfully", Toast.LENGTH_SHORT).show();
                            newClient.createConversation(list, conversationName, null, false, true, new AVIMConversationCreatedCallback() {
                                @Override
                                public void done(AVIMConversation Conversation, AVIMException e) {
                                    if (null != Conversation) {
                                        conversationID = Conversation.getConversationId();
                                        Log.d("user List", list.get(0));
                                        Log.d("user List1", list.get(1));

                                        list.clear();
                                        ChatInfo newMsg = chats.get(position);
                                        newMsg.setTextHint("");
                                        newMsg.setNewMsgHint(0);
                                        Message message = new Message();
                                        message.arg1 = CONVERSATION_DONE;
                                        handler.sendMessage(message);

                                        reAdapter.notifyDataSetChanged();
                                    }
                                }
                            });

                        }
                    }
                });
            }
        });
        chatUser.setAdapter(reAdapter);
    }

    public void initialChatInfo(){
        ChatInfo user1 = new ChatInfo("MrsHudson","", BitmapFactory.decodeResource(getResources(), R.drawable.mrhud), 0, new ArrayList<MsgInfo>());
        ChatInfo user2 = new ChatInfo("Dan","",BitmapFactory.decodeResource(getResources(), R.drawable.dan), 0, new ArrayList<MsgInfo>());
        ChatInfo user3 = new ChatInfo("Mai","", BitmapFactory.decodeResource(getResources(), R.drawable.catwoman), 0, new ArrayList<MsgInfo>());
        ChatInfo user4 = new ChatInfo("PaPa","", BitmapFactory.decodeResource(getResources(), R.drawable.contractor), 0, new ArrayList<MsgInfo>());
        ChatInfo user5 = new ChatInfo("LuLu","", BitmapFactory.decodeResource(getResources(), R.drawable.dandy), 0, new ArrayList<MsgInfo>());
        ChatInfo user6 = new ChatInfo("ChenCheng","", BitmapFactory.decodeResource(getResources(), R.drawable.ninja), 0, new ArrayList<MsgInfo>());

        chats.add(user1);
        chats.add(user2);
        chats.add(user3);
        chats.add(user4);
        chats.add(user5);
        chats.add(user6);
    }

    class CustomMessageHandler extends AVIMMessageHandler {
        @Override
        public void onMessage(AVIMMessage message, AVIMConversation conversation, AVIMClient client) {
            Toast.makeText(ChatUserActivity.this, "Get new Message", Toast.LENGTH_SHORT).show();
            String user_from = message.getFrom();
            Log.d("user from","from   "+ user_from);
            for(int i = 0; i < chats.size(); i++){

                if(chats.get(i).getChatUserName().equals(user_from)){
                    JSONObject jsonObject = null;
                    String MSG = null;
                    String msgContent = message.getContent();
                    try {
                        jsonObject = new JSONObject(msgContent);
                        MSG = jsonObject.getString("_lctext");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ChatInfo newMsg = chats.get(i);
                    MsgInfo msgInfo = new MsgInfo(MSG, 1, newMsg.getUserIcon());
                    newMsg.setConversation(listMsg);
                    newMsg.setNewMsgHint(R.drawable.red);
                    newMsg.setTextHint(MSG);
                    chats.remove(i);
                    chats.addFirst(newMsg);
                    reAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    public AVIMClient getClient(){
        return newClient;
    }

    public String getConversationID(){
        return conversationID;
    }

    public ChatInfo getChatInfo(){
        return user_other;
    }

    public UserInfo getUserInfo(){
        return userInfo;
    }

    public ArrayList<MsgInfo> getListMsg(){
        return listMsg;
    }

}
