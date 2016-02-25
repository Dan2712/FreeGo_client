package com.freego.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.freego.R;
import com.freego.activity.HomepageActivity;
import com.freego.app.GlobalApplication;

/**
 * Created by dan on 16/2/19.
 */
public class Homepage_ViewSearch extends LinearLayout{

    private View width;
    private View round;
    private float scale;
    private View circle;

    public Homepage_ViewSearch(Context context) {
        super(context);
        initView(context);
    }

    public Homepage_ViewSearch(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.homepage_view_search, this, true);
        width = findViewById(R.id.search_width);
        round = findViewById(R.id.search_round);
        circle = findViewById(R.id.search_circle);
        circle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalApplication.setIsPressed(true);
                if (HomepageActivity.isExpand()) {
                    closeSearch();
                    HomepageActivity.setIsExpand(false);
                } else {
                    expandSearch();
                    HomepageActivity.setIsExpand(true);
                }
            }
        });
    }

    public void updateShow(boolean isExpand) {
        double serchWid = round.getWidth() / 1.0;
        double wid = width.getWidth() / 1.0;
        double fenshu = wid / serchWid;
        scale = (float) fenshu;

        if (isExpand) {
            expandSearch();
        } else {
            closeSearch();
        }
    }

    private void expandSearch() {

        ObjectAnimator anim2 = ObjectAnimator.ofFloat(round, "alpha", 0f, 1f);
        ObjectAnimator anim3 = ObjectAnimator.ofFloat(round, "scaleX", scale, 1f);
        round.setPivotX(0);
        AnimatorSet animSet2= new AnimatorSet();
        animSet2.play(anim2).with(anim3);
        animSet2.setDuration(200);
        animSet2.start();

    }

    private void closeSearch() {
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(round, "scaleX", 1f, scale);
        ObjectAnimator anim4 = ObjectAnimator.ofFloat(round, "alpha", 1f, 0f);
        round.setPivotX(0);
        round.setPivotY(round.getHeight() / 2);
        AnimatorSet animSet1 = new AnimatorSet();
        animSet1.play(anim2).with(anim4);
        animSet1.setDuration(200);
        animSet1.start();
    }
}
