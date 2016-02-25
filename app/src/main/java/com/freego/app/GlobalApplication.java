package com.freego.app;

import android.app.Application;
import android.app.FragmentManager;
import android.content.Context;

import com.avos.avoscloud.AVOSCloud;

/**
 * Created by dan on 16/2/15.
 */
public class GlobalApplication extends Application {

    private static Context context;

    public static FragmentManager fragmentManager;

    private static boolean isPressed = false;

    public static Context getContext() {
        return context;
    }

    public static boolean isPressed() {
        return isPressed;
    }

    public static void setIsPressed(boolean Pressed) {
        isPressed = Pressed;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AVOSCloud.initialize(this, "DhpWhBfp0xQmESRalJl7ECSv-gzGzoHsz", "DUjBB8zbIxi39JxzGpp2cVOo");
        context = getApplicationContext();
    }
}
