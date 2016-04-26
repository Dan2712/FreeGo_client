package com.freego.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.avos.avoscloud.AVUser;
import com.freego.R;

public class MyHotelActivity extends Activity implements View.OnTouchListener {

    private FrameLayout home;

    private FrameLayout follower;

    private FrameLayout recruitment;

    private FrameLayout message;

    private ImageView homeImage;

    private ImageView followerImage;

    private ImageView recruitmentImage;

    private ImageView messageImage;

    private Intent intentRec;

    private ImageButton hostLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_my_hotel);

        home = (FrameLayout) findViewById(R.id.hotel_home);
        follower = (FrameLayout) findViewById(R.id.hotel_follower);
        recruitment = (FrameLayout) findViewById(R.id.hotel_recruitment);
        message = (FrameLayout) findViewById(R.id.hotel_message);
        hostLogout = (ImageButton) findViewById(R.id.host_logout);

        homeImage = (ImageView) findViewById(R.id.hotel_home_image);
        followerImage = (ImageView) findViewById(R.id.hotel_follower_image);
        recruitmentImage = (ImageView) findViewById(R.id.hotel_recruitment_image);
        messageImage = (ImageView) findViewById(R.id.hotel_message_image);

        home.setOnTouchListener(this);
        follower.setOnTouchListener(this);
        recruitment.setOnTouchListener(this);
        message.setOnTouchListener(this);

        homeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyHotelActivity.this, HomepageActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                finish();
            }
        });

        recruitmentImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentRec = new Intent(MyHotelActivity.this, HostEditActivity.class);
                startActivity(intentRec);
                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                finish();
            }
        });

        messageImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyHotelActivity.this, ChatUserActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                finish();
            }
        });

        hostLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AVUser.logOut();
                Intent intent = new Intent(MyHotelActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                finish();
            }
        });


    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.hotel_home:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    homeImage.setImageResource(R.drawable.public_home2);
                    Intent intent = new Intent(MyHotelActivity.this, HomepageActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                    finish();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    homeImage.setImageResource(R.drawable.personal_home_buttonstates);
                }
                return true;
            case R.id.hotel_follower:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    followerImage.setImageResource(R.drawable.public_follower2);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    followerImage.setImageResource(R.drawable.personal_follower_buttonstates);
                }
                return true;
            case R.id.hotel_recruitment:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    recruitmentImage.setImageResource(R.drawable.public_profile2);
                    intentRec = new Intent(MyHotelActivity.this, HostEditActivity.class);
                    startActivity(intentRec);
                    overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                    finish();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    recruitmentImage.setImageResource(R.drawable.personal_profile_buttonstates);
                }
                return true;
            case R.id.hotel_message:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    messageImage.setImageResource(R.drawable.public_message2);
                    Intent intent = new Intent(MyHotelActivity.this, ChatUserActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                    finish();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    messageImage.setImageResource(R.drawable.personal_message_buttonstates);
                }
                return true;
            default:
                return false;
        }
    }
}
