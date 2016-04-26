package com.freego.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ListView;

public class HeaderContainedList extends ListView {


    private View mHeaderView;
    private int finalTopHeight;
    private View mHeaderView2;

    public HeaderContainedList(Context context) {
        super(context);
    }

    public HeaderContainedList(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HeaderContainedList(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    private void setHeaderHeight(int height) {
        LayoutParams layoutParams = (LayoutParams) mHeaderView.getLayoutParams();
        layoutParams.height = height;
        mHeaderView.setLayoutParams(layoutParams);
    }


    public void setFinalTopHeight(int height) {
        this.finalTopHeight = height;
    }
    @Override
    public void addHeaderView(View v) {


        if(mHeaderView != null){
            mHeaderView2 = v;
            super.addHeaderView(v);
            mHeaderView2.getViewTreeObserver().addOnGlobalLayoutListener(
                    new OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            if (finalTopHeight == 0) {
                                finalTopHeight = mHeaderView.getMeasuredHeight();
                            }
                            setHeaderHeight(finalTopHeight);
                        }
                    });
        }else {
            mHeaderView = v;
            super.addHeaderView(mHeaderView);

            mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(
                    new OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            if (finalTopHeight == 0) {
                                finalTopHeight = mHeaderView2.getMeasuredHeight();
                            }
                            setHeaderHeight(finalTopHeight);
                        }
                    });
        }


    }



}
