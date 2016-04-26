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

public class MyStoryActivity extends Activity implements View.OnTouchListener {

    private FrameLayout home;

    private FrameLayout favourite;

    private FrameLayout notes;

    private FrameLayout message;

    private ImageView homeImage;

    private ImageView favouriteImage;

    private ImageView notesImage;

    private ImageView messageImage;

    private ImageButton volunteerLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_my_story);

        home = (FrameLayout) findViewById(R.id.story_home);
        favourite = (FrameLayout) findViewById(R.id.story_favourite);
        notes = (FrameLayout) findViewById(R.id.story_notes);
        message = (FrameLayout) findViewById(R.id.story_message);

        homeImage = (ImageView) findViewById(R.id.story_home_image);
        favouriteImage = (ImageView) findViewById(R.id.story_favourite_image);
        notesImage = (ImageView) findViewById(R.id.story_notes_image);
        messageImage = (ImageView) findViewById(R.id.story_message_image);
        volunteerLogout = (ImageButton) findViewById(R.id.volunteer_logout);

        home.setOnTouchListener(this);
        favourite.setOnTouchListener(this);
        notes.setOnTouchListener(this);
        message.setOnTouchListener(this);

        homeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyStoryActivity.this, HomepageActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                finish();
            }
        });

        messageImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyStoryActivity.this, ChatUserActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                finish();
            }
        });

        volunteerLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AVUser.logOut();
                Intent intent = new Intent(MyStoryActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                finish();
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.story_home:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    homeImage.setImageResource(R.drawable.public_home2);
                    Intent intent = new Intent(MyStoryActivity.this, HomepageActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                    finish();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    homeImage.setImageResource(R.drawable.personal_home_buttonstates);
                }
                return true;
            case R.id.story_favourite:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    favouriteImage.setImageResource(R.drawable.public_favorite2);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    favouriteImage.setImageResource(R.drawable.personal_favourivte_buttonstates);
                }
                return true;
            case R.id.story_notes:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    notesImage.setImageResource(R.drawable.public_profile2);
                    Intent intent = new Intent(MyStoryActivity.this, NotesActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                    finish();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    notesImage.setImageResource(R.drawable.personal_profile_buttonstates);
                }
                return true;
            case R.id.story_message:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    messageImage.setImageResource(R.drawable.public_message2);
                    Intent intent = new Intent(MyStoryActivity.this, ChatUserActivity.class);
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
