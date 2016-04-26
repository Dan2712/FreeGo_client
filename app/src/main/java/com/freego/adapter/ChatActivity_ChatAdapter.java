package com.freego.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.freego.R;
import com.freego.bean.MsgInfo;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity_ChatAdapter extends ArrayAdapter {

    private int resourceID;

    private ArrayList<MsgInfo> list = new ArrayList<>();

    private Context con;

    public ChatActivity_ChatAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);
        resourceID = resource;
        con = context;
        list = (ArrayList<MsgInfo>)objects;
    }


    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        Holder holder;
        MsgInfo msgInfo = (MsgInfo)getItem(position);

        if(convertView == null){
            holder = new Holder();
            if(msgInfo.getType() == 0) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.chatpage_list_item_me, null);
            }
            else{
                view = LayoutInflater.from(getContext()).inflate(R.layout.chatpage_list_item_other, null);
            }
            holder.chatItem = (TextView)view.findViewById(R.id.chatPageContent);
            holder.userIcon = (ImageView)view.findViewById(R.id.chatPageIcon);
            view.setTag(holder);

        }else {
            view = convertView;
            holder = (Holder)view.getTag();
        }

        holder.chatItem.setText(msgInfo.getContent());
        holder.userIcon.setImageBitmap(msgInfo.getUserIcon());
        return view;
    }

    class Holder{
        TextView chatItem;
        ImageView userIcon;
    }

}
