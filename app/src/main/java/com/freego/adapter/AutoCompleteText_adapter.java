package com.freego.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.freego.R;
import com.freego.bean.HintPlace;

import java.util.List;

/**
 * Created by henryye on 2/23/16.
 */
public class AutoCompleteText_adapter extends ArrayAdapter{

    private int resourceID;

    public AutoCompleteText_adapter(Context context, int resource, List objects) {
        super(context, resource, objects);
        resourceID = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HintPlace hintPlace = (HintPlace)getItem(position);

        ImageView transparentHead;
        TextView places;
        View view;
        HintHolder hintHolder;

        if(convertView == null){
            hintHolder = new HintHolder();
            view = LayoutInflater.from(getContext()).inflate(R.layout.homepage_hint,null);
            view.setTag(hintHolder);

        }else {
            view = convertView;
            hintHolder = (HintHolder)view.getTag();
        }

        hintHolder.places.setText(hintPlace.getPlaces());
        return view;

    }

    class HintHolder{
        ImageView transparentHead;
        TextView places;
    }
}
