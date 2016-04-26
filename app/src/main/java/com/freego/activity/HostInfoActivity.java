package com.freego.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.GetDataCallback;
import com.freego.R;
import com.freego.util.FileUtil;
import com.freego.view.CircleImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HostInfoActivity extends Activity implements ViewSwitcher.ViewFactory, View.OnTouchListener {

    private static final int AVATAR_UPDATE = 0;

    private static final int IMAGE2_DOWNLOAD = 1;

    private static final int IMAGE3_DOWNLOAD = 2;

    private static final int IMAGE4_DOWNLOAD = 3;

    private static final int IMAGE5_DOWNLOAD = 4;

    private ImageSwitcher mImageSwitcher;

    private Drawable[] imgIds;

    private int currentPosition;

    private float downX;

    private LinearLayout linearLayout;

    private ImageView[] tips;

    private int flag;

    private String hotelName;

    private String city;

    private CircleImageView circleImageView;

    private TextView nameText;

    private TextView addressText;

    private TextView introText;

    private ImageView genderImage;

    private TextView numText;

    private TextView periodText;

    private TextView startText;

    private TextView requireText;

    private TextView jobsText;

    private TextView provideText;

    private TextView expText;

    private Bitmap avatar;

    private JSONObject fileObject;

    private Bitmap image2;

    private Bitmap image3;

    private Bitmap image4;

    private Bitmap image5;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case AVATAR_UPDATE:
                    circleImageView.setImageBitmap(avatar);
                    break;
                case IMAGE2_DOWNLOAD:
                    imgIds[1] = new BitmapDrawable(image2);
                    mImageSwitcher.refreshDrawableState();
                    break;
                case IMAGE3_DOWNLOAD:
                    imgIds[2] = new BitmapDrawable(image3);
                    mImageSwitcher.refreshDrawableState();
                    break;
                case IMAGE4_DOWNLOAD:
                    imgIds[3] = new BitmapDrawable(image4);
                    mImageSwitcher.refreshDrawableState();
                    break;
                case IMAGE5_DOWNLOAD:
                    imgIds[4] = new BitmapDrawable(image5);
                    mImageSwitcher.refreshDrawableState();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hostinfo);

        Intent intent = getIntent();
        hotelName = intent.getStringExtra("hotelName");
        city = intent.getStringExtra("city");

        initSwitcher();
        initFile();
        new downloadAvatar().execute();
        new downloadImage().execute();

        nameText =(TextView)findViewById(R.id.hostinfo_hotel_name);
        Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/Helvetica LT Light.ttf");
        nameText.setTypeface(typeFace);
        addressText = (TextView) findViewById(R.id.hostinfo_hotel_location);
        introText = (TextView) findViewById(R.id.hostinfo_hotel_intro);
        genderImage = (ImageView) findViewById(R.id.hostinfo_person_gender);
        numText = (TextView) findViewById(R.id.hostinfo_person_number);
        periodText = (TextView) findViewById(R.id.hostinfo_periodEdit);
        startText = (TextView) findViewById(R.id.hostinfo_startEdit);
        requireText = (TextView) findViewById(R.id.hostinfo_requireEdit);
        jobsText = (TextView) findViewById(R.id.hostinfo_jobsEdit);
        provideText = (TextView) findViewById(R.id.hostinfo_provideEdit);
        expText = (TextView) findViewById(R.id.hostinfo_experiencesEdit);
        circleImageView = (CircleImageView) findViewById(R.id.circleImageView);

        try {
            nameText.setText(fileObject.getString("hotelName"));
            addressText.setText(fileObject.getString("address"));
            introText.setText(fileObject.getString("introduction"));
            if (fileObject.getInt("requireSex") == 1)
                genderImage.setImageResource(R.drawable.public_male_red);
            else if (fileObject.getInt("requireSex") == 2)
                genderImage.setImageResource(R.drawable.public_female_red);
            numText.setText(fileObject.getInt("requireNumber") + "");
            periodText.setText(fileObject.getInt("period") + "");

            SimpleDateFormat format = new SimpleDateFormat("yyMM");
            SimpleDateFormat format1 = new SimpleDateFormat("yy-MM");
            Date date = format.parse(fileObject.getString("startDate"));
            startText.setText("20" + format1.format(date));

            requireText.setText(fileObject.getString("requirement"));
            jobsText.setText(fileObject.getString("jobs"));
            provideText.setText(fileObject.getString("supplement"));
            expText.setText(fileObject.getString("experiences"));

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void initFile() {
        String file_url = "hotels" + File.separator + city + File.separator + hotelName + File.separator + hotelName + ".txt";

        BufferedReader reader = FileUtil.readFile(file_url);
        String line = "";
        StringBuilder content = new StringBuilder();

        try {
            while ((line = reader.readLine()) != null)
                content.append(line);

            fileObject = new JSONObject(content.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void initSwitcher() {
        imgIds = new Drawable[5];

        mImageSwitcher = (ImageSwitcher) findViewById(R.id.hostinfo_imageSwitcher1);

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

        Drawable first_image = new BitmapDrawable(FileUtil.readImage("hotels" + File.separator + city + File.separator
                + hotelName + File.separator + "image1"));
        imgIds[0] = first_image;
        currentPosition = getIntent().getIntExtra("position", 0);
        mImageSwitcher.setImageDrawable(imgIds[currentPosition]);

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
                downX = event.getX();
                break;
            }
            case MotionEvent.ACTION_UP:{
                float lastX = event.getX();
                if(lastX > downX){
                    if(currentPosition > 0){
                        mImageSwitcher.setInAnimation(AnimationUtils.loadAnimation(getApplication(), R.anim.hostinfo_left_in));
                        mImageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(getApplication(), R.anim.hostinfo_right_out));
                        currentPosition --;
                        mImageSwitcher.setImageDrawable(imgIds[currentPosition % imgIds.length]);
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
                        mImageSwitcher.setImageDrawable(imgIds[currentPosition]);
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

    private class downloadImage extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                JSONObject imageObject = fileObject.getJSONObject("image");
                String objId = imageObject.getString("objectId");
                AVQuery<AVObject> query = new AVQuery<>("image");
                final AVObject image = query.get(objId);
                AVFile sec_image = image.getAVFile("image2");
                AVFile thd_image = image.getAVFile("image3");
                AVFile f_image = image.getAVFile("image4");
                AVFile fifth_image = image.getAVFile("image5");
                sec_image.getDataInBackground(new GetDataCallback() {
                    @Override
                    public void done(byte[] bytes, AVException e) {
                        if (e == null) {
                            image2 = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            String url = "hotels" + File.separator + city + File.separator + hotelName + File.separator + "image2";

                            Message msg = new Message();
                            msg.what = IMAGE2_DOWNLOAD;
                            handler.sendMessage(msg);

                            FileUtil.writeImage(url, image2);
                        }
                    }
                });
                thd_image.getDataInBackground(new GetDataCallback() {
                    @Override
                    public void done(byte[] bytes, AVException e) {
                        if (e == null) {
                            image3 = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            String url = "hotels" + File.separator + city + File.separator + hotelName + File.separator + "image3";

                            Message msg = new Message();
                            msg.what = IMAGE3_DOWNLOAD;
                            handler.sendMessage(msg);

                            FileUtil.writeImage(url, image3);
                        }
                    }
                });
                f_image.getDataInBackground(new GetDataCallback() {
                    @Override
                    public void done(byte[] bytes, AVException e) {
                        if (e == null) {
                            image4 = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            String url = "hotels" + File.separator + city + File.separator + hotelName + File.separator + "image4";

                            Message msg = new Message();
                            msg.what = IMAGE4_DOWNLOAD;
                            handler.sendMessage(msg);

                            FileUtil.writeImage(url, image4);
                        }
                    }
                });
                fifth_image.getDataInBackground(new GetDataCallback() {
                    @Override
                    public void done(byte[] bytes, AVException e) {
                        if (e == null) {
                            image5 = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            String url = "hotels" + File.separator + city + File.separator + hotelName + File.separator + "image5";

                            Message msg = new Message();
                            msg.what = IMAGE5_DOWNLOAD;
                            handler.sendMessage(msg);
                            FileUtil.writeImage(url, image5);
                        }
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (AVException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class downloadAvatar extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                final AVQuery<AVObject> query = new AVQuery<>("host");
                AVObject hostObject = query.get(fileObject.getString("objectId"));
                AVFile avFile = hostObject.getAVFile("avatar");
                avFile.getDataInBackground(new GetDataCallback() {
                    @Override
                    public void done(byte[] bytes, AVException e) {
                        if (e == null) {
                            avatar = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            String url = "hotels" + File.separator + city + File.separator + hotelName + File.separator + "avatar_" + hotelName;

                            Message msg = new Message();
                            msg.what = AVATAR_UPDATE;
                            handler.sendMessage(msg);

                            FileUtil.writeImage(url, avatar);
                        }
                    }
                });
            } catch (AVException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
