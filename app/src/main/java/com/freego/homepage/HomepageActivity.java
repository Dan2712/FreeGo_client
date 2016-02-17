package com.freego.homepage;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.widget.ListView;

import com.freego.R;
import com.freego.util.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;

public class HomepageActivity extends Activity {

    private ArrayList<imagePlace> places = new ArrayList<imagePlace>();

    private ListView places_image;

    public CircleImageView circleImageView;

    public int id;

    public int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_homepage);

        Intent intent = getIntent();
        id = intent.getIntExtra("id", -1);
        type = intent.getIntExtra("type", -1);

        initPlaces();
        PlaceAdapter adapter = new PlaceAdapter(this, R.layout.homepage_image_place, places);
        circleImageView = (CircleImageView) findViewById(R.id.circleImageView);
        places_image = (ListView)findViewById(R.id.placeListview);
        places_image.setAdapter(adapter);

        new DownloadTask().execute();
    }

    public void initPlaces(){
        imagePlace bj = new imagePlace("BJ", R.drawable.homepage_beijing);
        places.add(bj);
        imagePlace hk = new imagePlace("hk", R.drawable.homepage_hongkong);
        places.add(hk);
        imagePlace hn = new imagePlace("hn", R.drawable.homepage_hunan);
        places.add(hn);
        imagePlace sh = new imagePlace("sh", R.drawable.homepage_shanghai);
        places.add(sh);
        imagePlace tw = new imagePlace("tw", R.drawable.homepage_taiwan);
        places.add(tw);
        imagePlace xz = new imagePlace("xz", R.drawable.homepage_xizhang);
        places.add(xz);
        imagePlace yn = new imagePlace("yn", R.drawable.homepage_tunnan);
        places.add(yn);

    }

    class DownloadTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            String pageAction = "homepage/download";
            String content = "id=" + id + "&type=" + type;

            StringBuilder builder = null;

            try {
                HttpURLConnection connection = new HttpUtil(pageAction).getConnection();
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

                File file = new File(HomepageActivity.this.getFilesDir() + File.separator + "profile" + File.separator + id + File.separator + id + ".txt");
                if (!file.getParentFile().exists())
                    file.getParentFile().mkdirs();

                PrintStream out = new PrintStream(new FileOutputStream(file));
                out.print(object.toString());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            circleImageView.setImageResource(R.drawable.homepage_tunnan);
        }
    }
}
