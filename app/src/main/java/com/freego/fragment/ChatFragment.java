package com.freego.fragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageHandler;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMMessagesQueryCallback;
import com.avos.avoscloud.im.v2.callback.AVIMSingleMessageQueryCallback;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.freego.R;
import com.freego.activity.ChatUserActivity;
import com.freego.adapter.ChatActivity_ChatAdapter;
import com.freego.bean.ChatInfo;
import com.freego.bean.MsgInfo;
import com.freego.bean.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends ListFragment {

    private View view;

    private Button send;

    private EditText input;

    private ListView listView;

    private AVIMClient newClient;

    private String conversationID;

    private ImageView chatBack;

    private ChatInfo user_other;

    private UserInfo user_me;

    private AVIMTextMessage message;

    private MsgInfo msgInfo;

    private ArrayList<MsgInfo> listMsg;

    private ChatActivity_ChatAdapter chatAdapter;

    private AVIMConversation Conversation;

    private AVIMTextMessage lastMessage;

    private final static int mySelf = 0;

    private final static int others = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AVIMMessageManager.registerDefaultMessageHandler(new CustomMessageHandler());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_main, container, false);
        chatBack = (ImageView)view.findViewById(R.id.chat_back);
        send = (Button)view.findViewById(R.id.send);
        input = (EditText)view.findViewById(R.id.input);
        listView = (ListView)view.findViewById(android.R.id.list);
        conversationID = ((ChatUserActivity)getActivity()).getConversationID();
        user_other = ((ChatUserActivity)getActivity()).getChatInfo();
        user_me = ((ChatUserActivity)getActivity()).getUserInfo();
        listMsg = ((ChatUserActivity)getActivity()).getListMsg();

        newClient = AVIMClient.getInstance(user_me.getUserName());
        Conversation = newClient.getConversation(conversationID);

        Conversation.getLastMessage(new AVIMSingleMessageQueryCallback() {
            @Override
            public void done(AVIMMessage avimMessage, AVIMException e) {
                lastMessage = (AVIMTextMessage) avimMessage;
            }
        });

        if (lastMessage != null) {
            Conversation.queryMessages(lastMessage.getMessageId(), lastMessage.getTimestamp(), 10, new AVIMMessagesQueryCallback() {
                @Override
                public void done(List<AVIMMessage> list, AVIMException e) {
                    for(int i = 0; i < list.size(); i++){
                        String msgFrom = list.get(i).getFrom();
                        String con = list.get(i).getContent();
                        JSONObject jsonObject = null;
                        String content = null;
                        try {
                            jsonObject = new JSONObject(con);
                            content = jsonObject.getString("_lctext");
                        } catch (JSONException e1) {
                            e.printStackTrace();
                        }
                        if(msgFrom.equals(user_other.getChatUserName())){
                            MsgInfo msginfo = new MsgInfo(content, others, user_other.getUserIcon());
                            listMsg.add(msginfo);
                        }else {
                            MsgInfo msginfo = new MsgInfo(content, mySelf, user_me.getUserIcon());
                            listMsg.add(msginfo);
                        }
                    }
                    listView.setBottom(list.size());
                }
            });

        }
        chatAdapter = new ChatActivity_ChatAdapter(getActivity(),R.layout.chatpage_list_item_other, listMsg);

        chatBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.fragment_slide_down_enter, R.anim.fragment_slide_up_exit);
                fragmentTransaction.remove(ChatFragment.this).commit();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = input.getText().toString();
                msgInfo = new MsgInfo(msg, mySelf,user_me.getUserIcon());
                message = new AVIMTextMessage();
                message.setText(msg);
                Conversation.sendMessage(message, new AVIMConversationCallback() {
                    @Override
                    public void done(AVIMException e) {
                        if (e == null) {
                            listMsg.add(msgInfo);
                            listView.setSelection(listView.getBottom());
                            input.setText("");
                        }
                    }
                });
            }
        });
        return  view;
    }

    @Override
    public void onResume() {
        super.onResume();
        listView.setAdapter(chatAdapter);
        chatAdapter.notifyDataSetChanged();
    }

    class CustomMessageHandler extends AVIMMessageHandler {
        boolean flag = false;
        @Override
        public void onMessage(AVIMMessage message, AVIMConversation conversation, AVIMClient client) {
            flag = !flag;
            if(conversation.getConversationId().equals(conversationID)) {
                JSONObject jsonObject = null;
                String MSG = null;
                String msgContent = message.getContent();
                try {
                    jsonObject = new JSONObject(msgContent);
                    MSG = jsonObject.getString("_lctext");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(user_other.getChatUserName().equals(message.getFrom())) {
                    MsgInfo msgInfo = new MsgInfo(MSG, others, user_other.getUserIcon());
                    if(flag) {
                        listMsg.add(msgInfo);
                    }
                    Toast.makeText(getActivity(), "Get new Message", Toast.LENGTH_SHORT).show();
                }
                listView.setSelection(listView.getBottom());
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }
}
