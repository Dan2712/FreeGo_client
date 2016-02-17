package com.freego.homepage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.freego.R;

import java.util.List;

/**
 * Created by henryye on 1/22/16.
 */
public class PlaceAdapter extends ArrayAdapter {

    int resourceID;

    public PlaceAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);
        resourceID = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        imagePlace imageplace = (imagePlace)getItem(position);
        View view;

        if(convertView == null) {
            holder = new Holder();
            view = LayoutInflater.from(getContext()).inflate(resourceID, null);
            holder.image = (ImageView)view.findViewById(R.id.place);
            view.setTag(holder);

        }else {
            view = convertView;
            holder = (Holder)view.getTag();
        }

        holder.image.setImageResource(imageplace.getImageID());
        return view;
    }
    class Holder{
        ImageView image;
    }
}
