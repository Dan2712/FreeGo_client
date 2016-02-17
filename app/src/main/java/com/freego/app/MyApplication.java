package com.freego.app;

import android.app.Application;
import android.content.Context;

/**
 * Created by dan on 16/2/15.
 */
public class MyApplication extends Application {

    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
}
