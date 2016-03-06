package com.freego.activity;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.freego.R;

public class HostInfoActivity extends Activity implements ViewSwitcher.ViewFactory, View.OnTouchListener {

    private ImageSwitcher mImageSwitcher;

    private int[] imgIds;

    private int currentPosition;

    private float downX;

    private LinearLayout linearLayout;

    private ImageView[] tips;

    private int flag;

    private TextView nameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hostinfo);

        initSwitcher();

        nameText =(TextView)findViewById(R.id.hostinfo_hotelName);
        Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/Helvetica LT Light.ttf");
        nameText.setTypeface(typeFace);


    }

    public void initSwitcher() {
        imgIds = new int[]{R.drawable.host01,R.drawable.host02,R.drawable.host03,R.drawable.host04,
                R.drawable.host05};

        mImageSwitcher  = (ImageSwitcher) findViewById(R.id.hostinfo_imageSwitcher1);

        mImageSwitcher.setFactory(this);

        mImageSwitcher.setOnTouchListener(this);

        linearLayout = (LinearLayout) findViewById(R.id.hostinfo_viewGroup);

        tips = new ImageView[imgIds.length];
        for(int i=0; i<imgIds.length; i++){
            ImageView mImageView = new ImageView(this);
            tips[i] = mImageView;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT));
            layoutParams.rightMargin = 3;
            layoutParams.leftMargin = 3;

            mImageView.setBackgroundResource(R.drawable.hostinfo_indicator_unfocused);
            linearLayout.addView(mImageView, layoutParams);
        }

        currentPosition = getIntent().getIntExtra("position", 0);
        mImageSwitcher.setImageResource(imgIds[currentPosition]);

        setImageBackground(currentPosition);

        final ImageView imageView = (ImageView) findViewById(R.id.hostinfo_mark);
        flag = 1;
        imageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                switch (flag)
                {
                    case 1:
                        imageView.setImageResource(R.drawable.public_marked);
                        flag = 0;
                        break;
                    case 0:
                        imageView.setImageResource(R.drawable.public_mark);
                        flag = 1;
                        break;
                }
            }
        });

    }

    private void setImageBackground(int selectItems){
        for(int i=0; i<tips.length; i++){
            if(i == selectItems){
                tips[i].setBackgroundResource(R.drawable.hostinfo_indicator_focused);
            }else{
                tips[i].setBackgroundResource(R.drawable.hostinfo_indicator_unfocused);
            }
        }
    }

    @Override
    public View makeView() {
        final ImageView i = new ImageView(this);
        i.setBackgroundColor(0xff000000);
        i.setScaleType(ImageView.ScaleType.CENTER_CROP);
        i.setLayoutParams(new ImageSwitcher.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        return i ;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:{
                //手指按下的X坐标
                downX = event.getX();
                break;
            }
            case MotionEvent.ACTION_UP:{
                float lastX = event.getX();
                //抬起的时候的X坐标大于按下的时候就显示上一张图片
                if(lastX > downX){
                    if(currentPosition > 0){
                        //设置动画
                        mImageSwitcher.setInAnimation(AnimationUtils.loadAnimation(getApplication(), R.anim.hostinfo_left_in));
                        mImageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(getApplication(), R.anim.hostinfo_right_out));
                        currentPosition --;
                        mImageSwitcher.setImageResource(imgIds[currentPosition % imgIds.length]);
                        setImageBackground(currentPosition);
                    }else{
                        Toast.makeText(getApplication(), "First Picture", Toast.LENGTH_SHORT).show();
                    }
                }

                if(lastX < downX){
                    if(currentPosition < imgIds.length - 1){
                        mImageSwitcher.setInAnimation(AnimationUtils.loadAnimation(getApplication(), R.anim.hostinfo_right_in));
                        mImageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(getApplication(), R.anim.hostinfo_left_out));
                        currentPosition ++ ;
                        mImageSwitcher.setImageResource(imgIds[currentPosition]);
                        setImageBackground(currentPosition);
                    }else{
                        Toast.makeText(getApplication(), "Last Picture", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }
        return true;
    }
}
