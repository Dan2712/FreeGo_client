package com.freego.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.freego.R;
import com.freego.bean.ImagePlace;

import java.util.ArrayList;

/**
 * Created by dan on 16/2/19.
 */
public class Homepage_RecycleView_adapter extends RecyclerView.Adapter<Homepage_RecycleView_adapter.ViewHolder> {

    private ArrayList<ImagePlace> places = new ArrayList<ImagePlace>();

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;

        public ViewHolder(ImageView itemView) {
            super(itemView);
            imageView = itemView;
        }
    }

    public Homepage_RecycleView_adapter(ArrayList<ImagePlace> places) {
        this.places = places;
    }

    @Override
    public Homepage_RecycleView_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        ImageView imageView = (ImageView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.homepage_adapter_image, parent, false);
        ViewHolder viewHolder = new ViewHolder(imageView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(Homepage_RecycleView_adapter.ViewHolder holder, int position) {
        holder.imageView.setImageResource(places.get(position).getImageID());
    }

    @Override
    public int getItemCount() {
        return places.size();
    }
}
