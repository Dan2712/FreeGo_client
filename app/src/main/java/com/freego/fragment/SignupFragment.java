package com.freego.fragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SignUpCallback;
import com.freego.R;
import com.freego.activity.HomepageActivity;
import com.freego.app.GlobalApplication;

/**
 * Created by henryye on 2/23/16.
 */
public class SignupFragment extends Fragment implements View.OnClickListener, View.OnFocusChangeListener{

    private static final int TOAST_DUPLI = 0;

    private static final int TOAST_INFOERROR = 1;

    private static String MAIL_FORMAT = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";

    private ImageView signupArrow;

    private EditText mailText;

    private EditText nameText;

    private EditText pwdText;

    private EditText pwdCfmText;

    private RadioGroup typeSelect;

    private RadioButton hostSelected;

    private RadioButton voluneerSelected;

    private Button submitButton;

    private Button resetButton;

    private ImageView emailCheck;

    private ImageView pwdCheck;

    private String mail;

    private String name;

    private String password;

    private int type;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TOAST_DUPLI:
                    Toast.makeText(getActivity(), "Duplicate email or name", Toast.LENGTH_SHORT).show();
                    break;
                case TOAST_INFOERROR:
                    Toast.makeText(getActivity(), "Information error, please check or fullfill", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        signupArrow = (ImageView)view.findViewById(R.id.signup_arrow);
        signupArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = GlobalApplication.fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.fragment_slide_down_enter, R.anim.fragment_slide_up_exit);
                fragmentTransaction.remove(SignupFragment.this).commit();
                getActivity().findViewById(R.id.login_loginDialog).setVisibility(View.VISIBLE);
                getActivity().findViewById(R.id.login_linearLayout).setBackgroundResource(R.drawable.welcome_login_background);
            }
        });

        mailText = (EditText) view.findViewById(R.id.signup_editEmail);
        nameText = (EditText) view.findViewById(R.id.signup_editName);

        pwdText = (EditText) view.findViewById(R.id.signup_inputPassword);
        pwdCfmText = (EditText) view.findViewById(R.id.signup_inputPwdConfirm);
        pwdText.setTypeface(Typeface.DEFAULT);
        pwdText.setTransformationMethod(new PasswordTransformationMethod());
        pwdCfmText.setTypeface(Typeface.DEFAULT);
        pwdCfmText.setTransformationMethod(new PasswordTransformationMethod());

        typeSelect = (RadioGroup) view.findViewById(R.id.signup_userType);
        hostSelected = (RadioButton) view.findViewById(R.id.signup_hostType);
        voluneerSelected = (RadioButton) view.findViewById(R.id.signup_volunteerType);

        emailCheck = (ImageView) view.findViewById(R.id.signup_emailCheckImage);
        pwdCheck = (ImageView) view.findViewById(R.id.signup_passwordCheckImage);
        submitButton = (Button) view.findViewById(R.id.signup_submit);
        resetButton = (Button) view.findViewById(R.id.signup_reset);

        typeSelect.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (voluneerSelected.getId() == checkedId)
                    type = 0;
                else if (hostSelected.getId() == checkedId)
                    type = 1;
            }
        });

        mailText.setOnFocusChangeListener(this);
        pwdCfmText.setOnFocusChangeListener(this);
        submitButton.setOnClickListener(this);
        resetButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signup_submit:
                String name_check = nameText.getText().toString();
                boolean checked = typeSelect.isSelected();
                if (mailText.getText().toString().matches(MAIL_FORMAT) && pwdText.getText().toString().equals(pwdCfmText.getText().toString())
                        && !name_check.equals("") && checked) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            mail = mailText.getText().toString();
                            name = nameText.getText().toString();
                            password = pwdText.getText().toString();
                            final AVUser user = new AVUser();
                            user.setEmail(mail);
                            user.setUsername(name);
                            user.setPassword(password);
                            user.put("type", type);
                            if (type == 0) {
                                AVObject volunteer = new AVObject("volunteer");
                                user.put("link_volunteer", volunteer);
                            } else if (type == 1) {
                                AVObject host = new AVObject("host");
                                user.put("link_host", host);
                            }

                            user.signUpInBackground(new SignUpCallback() {
                                @Override
                                public void done(AVException e) {
                                    if (e == null) {
                                        Intent intent = new Intent(getActivity(), HomepageActivity.class);
                                        startActivity(intent);
                                        getActivity().finish();
                                    } else {
                                        Message msg = new Message();
                                        msg.what = TOAST_DUPLI;
                                        handler.sendMessage(msg);
                                    }
                                }
                            });
                        }
                    }).start();
                } else {
                    Message msg = new Message();
                    msg.what = TOAST_INFOERROR;
                    handler.sendMessage(msg);
                }
                break;
            case R.id.signup_reset:
                mailText.setText("");
                nameText.setText("");
                pwdText.setText("");
                pwdCfmText.setText("");
                hostSelected.setChecked(false);
                voluneerSelected.setChecked(false);
                emailCheck.setVisibility(View.INVISIBLE);
                pwdCheck.setVisibility(View.INVISIBLE);
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.signup_editEmail:
                if (!hasFocus) {
                    emailCheck.setVisibility(View.VISIBLE);
                    if (mailText.getText().toString().matches(MAIL_FORMAT))
                        emailCheck.setImageResource(R.drawable.public_correct);
                    else
                        emailCheck.setImageResource(R.drawable.public_incorrect);
                } else
                    emailCheck.setVisibility(View.INVISIBLE);
                break;
            case R.id.signup_inputPwdConfirm:
                if (!hasFocus) {
                    pwdCheck.setVisibility(View.VISIBLE);
                    String password = pwdText.getText().toString();
                    String password_confirm = pwdCfmText.getText().toString();
                    if (password.equals(password_confirm))
                        pwdCheck.setImageResource(R.drawable.public_correct);
                    else
                        pwdCheck.setImageResource(R.drawable.public_incorrect);
                } else
                    pwdCheck.setVisibility(View.INVISIBLE);
                break;
        }
    }
}
