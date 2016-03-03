package com.freego.util;

import android.app.ProgressDialog;
import android.content.Context;

public class ProgressUtil {

    private static ProgressDialog dialog;

    public static void showProgress(Context context) {
        dialog = new ProgressDialog(context);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Loading");
        dialog.setMessage("Please wait!");
        dialog.setIndeterminate(false);
        dialog.setCancelable(false);
        dialog.show();
    }

    public static void dismissProgress() {
        dialog.dismiss();
    }
}
