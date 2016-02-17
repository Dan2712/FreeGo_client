package com.freego;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.freego.homepage.HomepageActivity;
import com.freego.util.HttpUtil;
import com.freego.util.ProgressUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

public class LoginActivity extends Activity implements View.OnClickListener{

    private static final int RECEIVE = 0;

    private static final int TOAST_INVALID = 1;

    private static final int TOAST_FORMAT = 2;

    private static String MAIL_FORMAT = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";

    private EditText nameText;

    private EditText pwdText;

    private ImageButton loginButton;

    private ImageButton signButton;

    private StringBuilder builder;

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

        nameText = (EditText) findViewById(R.id.usernameInput);
        pwdText = (EditText) findViewById(R.id.passwordInput);
        loginButton = (ImageButton) findViewById(R.id.login);
        signButton = (ImageButton) findViewById(R.id.signup);

        loginButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                if (nameText.getText().toString().matches(MAIL_FORMAT)) {
                    ProgressUtil.showProgress(LoginActivity.this);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            HttpURLConnection connection = null;

                            try {
                                String pageAction = "login/check";
                                String content = "user.mail=" + nameText.getText() + "&user.password=" + pwdText.getText();

                                connection = new HttpUtil(pageAction).getConnection();
                                DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                                outputStream.writeBytes(content);
                                outputStream.flush();
                                outputStream.close();

                                if (connection.getResponseCode() == 200) {
                                    InputStream in = connection.getInputStream();
                                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                                    String line = "";
                                    builder = new StringBuilder();
                                    while ((line = reader.readLine()) != null)
                                        builder.append(line);

                                }

                                JSONObject object = new JSONObject(builder.toString());
                                boolean founded = object.getBoolean("founded");
                                if (founded) {
                                    int id = object.getInt("id");
                                    int type = object.getInt("type");
                                    Intent intent = new Intent(LoginActivity.this, HomepageActivity.class);
                                    intent.putExtra("id", id);
                                    intent.putExtra("type", type);
                                    startActivity(intent);
                                    ProgressUtil.dismissProgress();
                                } else {
                                    Message msg = new Message();
                                    msg.what = TOAST_INVALID;
                                    handler.sendMessage(msg);
                                    ProgressUtil.dismissProgress();
                                }

                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                } else {
                    Message msg = new Message();
                    msg.what = TOAST_FORMAT;
                    handler.sendMessage(msg);
                }
                break;
            case R.id.signup:
        }
    }
}
