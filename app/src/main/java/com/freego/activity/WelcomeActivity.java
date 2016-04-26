package com.freego.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.avos.avoscloud.AVUser;
import com.freego.R;

public class WelcomeActivity extends Activity {

    private AnimationSet animationSet;

    private ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcome);

        AVUser currentUser = AVUser.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            logo = (ImageView) findViewById(R.id.welcome_logo);
            animationSet = new AnimationSet(true);
            AlphaAnimation alphaLogo = new AlphaAnimation(0,1);

            TranslateAnimation translateAnimation = new TranslateAnimation(logo.getX(),logo.getX(),logo.getY(),logo.getY()-250);

            translateAnimation.setDuration(4000);
            alphaLogo.setDuration(3000);
            animationSet.addAnimation(alphaLogo);
            animationSet.addAnimation(translateAnimation);
            animationSet.setFillAfter(true);

            logo.startAnimation(animationSet);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    Intent intent = new Intent(WelcomeActivity.this, HomepageActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.activity_fade, R.anim.activity_hold);
                    finish();
                }
            }).start();
        }
    }
}
