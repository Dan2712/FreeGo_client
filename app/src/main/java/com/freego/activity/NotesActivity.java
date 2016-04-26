package com.freego.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.freego.R;

public class NotesActivity extends Activity {

    private int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_notes);

        final ImageView imageView = (ImageView) findViewById(R.id.hostinfo_mark);
        flag = 1;
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (flag) {
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
}
