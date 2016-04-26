package com.freego.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.freego.R;
import com.freego.app.GlobalApplication;
import com.freego.fragment.SignupFragment;
import com.freego.util.ProgressUtil;

public class LoginActivity extends Activity implements View.OnClickListener{

    private static final int TOAST_INVALID = 0;

    private static final int TOAST_FORMAT = 1;

    private static String MAIL_FORMAT = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";

    private EditText nameText;

    private EditText pwdText;

    private ImageButton loginButton;

    private ImageButton signButton;

    private ImageView logo;

    private LinearLayout loginDialog;

    private AnimationSet alphaAnimationset;

    private AnimationSet animationSet;

    private LinearLayout linearLayout;

    private TextView forgotText;

    private SignupFragment signupFragment;

    private FragmentManager fragmentManager;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TOAST_INVALID:
                    Toast.makeText(LoginActivity.this, "Wrong name or password, please check again!", Toast.LENGTH_SHORT).show();
                    break;
                case TOAST_FORMAT:
                    Toast.makeText(LoginActivity.this, "Please enter a mail address correctly!", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        signupFragment = new SignupFragment();
        fragmentManager = getFragmentManager();

        nameText = (EditText) findViewById(R.id.login_usernameInput);
        pwdText = (EditText) findViewById(R.id.login_passwordInput);
        pwdText.setTypeface(Typeface.DEFAULT);
        pwdText.setTransformationMethod(new PasswordTransformationMethod());
        loginButton = (ImageButton) findViewById(R.id.login_loginButton);
        signButton = (ImageButton) findViewById(R.id.login_signupButton);
        logo = (ImageView) findViewById(R.id.login_logo);
        loginDialog = (LinearLayout) findViewById(R.id.login_loginDialog);
        linearLayout = (LinearLayout) findViewById(R.id.login_linearLayout);
        forgotText = (TextView) findViewById(R.id.forgotText);

        loginDialog.setAlpha(0);
        animationSet = new AnimationSet(true);
        alphaAnimationset = new AnimationSet(true);
        AlphaAnimation alphaLogo = new AlphaAnimation(0,1);
        AlphaAnimation alphaLogin = new AlphaAnimation(0,1);
        TranslateAnimation translateAnimation = new TranslateAnimation(logo.getX(),logo.getX(),logo.getY(),logo.getY()-250);
        alphaLogin.setDuration(3500);
        translateAnimation.setDuration(4000);
        alphaLogo.setDuration(3000);
        animationSet.addAnimation(alphaLogo);
        animationSet.addAnimation(translateAnimation);
        animationSet.setFillAfter(true);

        alphaAnimationset.addAnimation(alphaLogin);
        logo.startAnimation(animationSet);

        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                loginDialog.startAnimation(alphaAnimationset);
                loginDialog.setAlpha(1);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        loginButton.setOnClickListener(this);
        signButton.setOnClickListener(this);
        forgotText.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_loginButton:
                if (nameText.getText().toString().matches(MAIL_FORMAT)) {
                    ProgressUtil.showProgress(LoginActivity.this);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            AVUser.logInInBackground(nameText.getText().toString(), pwdText.getText().toString(), new LogInCallback<AVUser>() {
                                @Override
                                public void done(AVUser avUser, AVException e) {
                                    if (e == null) {
                                        Intent intent = new Intent(LoginActivity.this, HomepageActivity.class);
                                        startActivity(intent);
                                        ProgressUtil.dismissProgress();
                                        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                                        finish();
                                    } else {
                                        Message msg = new Message();
                                        msg.what = TOAST_INVALID;
                                        handler.sendMessage(msg);
                                        ProgressUtil.dismissProgress();
                                    }
                                }
                            });
                        }
                    }).start();
                } else {
                    Message msg = new Message();
                    msg.what = TOAST_FORMAT;
                    handler.sendMessage(msg);
                }
                break;
            case R.id.login_signupButton:
                GlobalApplication.fragmentManager = fragmentManager;
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.fragment_slide_down_enter, R.anim.fragment_slide_up_exit);
                fragmentTransaction.replace(R.id.login_frameLayout, signupFragment);
                loginDialog.setVisibility(View.INVISIBLE);
                logo.setVisibility(View.INVISIBLE);
                fragmentTransaction.commit();
                linearLayout.setBackgroundResource(R.drawable.signup_background);
                break;
            case R.id.forgotText:
                Toast.makeText(LoginActivity.this, "Try to remember", Toast.LENGTH_SHORT).show();
        }
    }
}
