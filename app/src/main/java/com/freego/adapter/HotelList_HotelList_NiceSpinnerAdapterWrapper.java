package com.freego.adapter;

import android.content.Context;
import android.widget.ListAdapter;


public class HotelList_HotelList_NiceSpinnerAdapterWrapper extends HotelList_NiceSpinnerBaseAdapter {

    private final ListAdapter mBaseAdapter;

    public HotelList_HotelList_NiceSpinnerAdapterWrapper(Context context, ListAdapter toWrap) {
        super(context);
        mBaseAdapter = toWrap;
    }

    @Override
    public int getCount() {
        return mBaseAdapter.getCount() - 1;
    }

    @Override
    public Object getItem(int position) {
        if (position >= mSelectedIndex) {
            return mBaseAdapter.getItem(position + 1);
        } else {
            return mBaseAdapter.getItem(position);
        }
    }

    @Override
    public Object getItemInDataset(int position) {
        return mBaseAdapter.getItem(position);
    }
}