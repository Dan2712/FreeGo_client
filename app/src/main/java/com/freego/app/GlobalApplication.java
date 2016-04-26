package com.freego.app;

import android.app.Application;
import android.app.FragmentManager;
import android.content.Context;

import com.avos.avoscloud.AVOSCloud;

public class GlobalApplication extends Application {

    private static Context context;

    public static FragmentManager fragmentManager;

    private static boolean isPressed = false;

    public static boolean isMsg_flag() {
        return msg_flag;
    }

    public static void setMsg_flag(boolean msg_flag) {
        GlobalApplication.msg_flag = msg_flag;
    }

    private static boolean msg_flag = false;

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
