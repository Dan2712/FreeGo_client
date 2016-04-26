package com.freego.adapter;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.freego.R;
import com.freego.RecycleOnClickListener.RecycleOnClickListener;
import com.freego.bean.ChatInfo;

import java.util.LinkedList;

public class ChatActivity_RecycleAdapter extends RecyclerView.Adapter {

    private LinkedList<ChatInfo> list = new LinkedList<>();

    private RecycleOnClickListener onItemClickListener;

    public int mPosition;

    public ChatActivity_RecycleAdapter(LinkedList<ChatInfo> list) {
        super();
        this.list = list;
    }
    public ChatActivity_RecycleAdapter(LinkedList<ChatInfo> list, RecycleOnClickListener onClickListener){
        super();
        this.list = list;
        onItemClickListener = onClickListener;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatuser_list_item, parent, false);
        MywHolder holder = new MywHolder(view, onItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        mPosition = position;
        ChatInfo item = list.get(position);
        String textHint = item.getTextHint();
        int newMsgHint = item.getNewMsgHint();
        Bitmap iconPic = item.getUserIcon();
        String name = item.getChatUserName();
        MywHolder mywHolder = (MywHolder)holder;
        mywHolder.icon.setImageBitmap(iconPic);
        mywHolder.userName.setText(name);
        if(newMsgHint != 0) {
            mywHolder.newMsgHint.setImageResource(newMsgHint);
        }
        mywHolder.textHint.setText(textHint);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MywHolder extends RecyclerView.ViewHolder{

        private ImageView icon;

        private ImageView newMsgHint;

        private TextView textHint;

        private TextView userName;

        public MywHolder(View view, final RecycleOnClickListener onClickListener){
            super(view);
            icon = (ImageView)view.findViewById(R.id.userIcon);
            newMsgHint = (ImageView)view.findViewById(R.id.newMsgHint);
            textHint = (TextView)view.findViewById(R.id.newMsgText);
            userName = (TextView)view.findViewById(R.id.chatUserName);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickListener != null) {
                        onClickListener.onClick(v, getPosition());
                    }
                }
            });
        }
    }


}
