package com.freego.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.freego.app.GlobalApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

/**
 * Created by dan on 16/3/6.
 */
public class FileUtil {

    private static PrintStream printStream;

    private static FileOutputStream fileOutputStream;

    private static FileInputStream fileInputStream;

    private static BufferedReader reader;

    public static void writeFile(String url, String content) {
        File file = new File(GlobalApplication.getContext().getFilesDir() + File.separator + url);

        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();

        try {
            printStream = new PrintStream(new FileOutputStream(file));
            printStream.print(content);
            printStream.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (printStream != null)
                printStream.close();
        }

    }

    public static void writeImage(String url, Bitmap bitmap) {
        File file = new File(GlobalApplication.getContext().getFilesDir() + File.separator + url);

        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();

        try {
            fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null)
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    public static BufferedReader readFile(String url) {
        File file = new File(GlobalApplication.getContext().getFilesDir() + File.separator + url);

        try {
            fileInputStream = new FileInputStream(file);
            reader = new BufferedReader(new InputStreamReader(fileInputStream));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return reader;
    }

    public static Bitmap readImage(String url) {
        File file = new File(GlobalApplication.getContext().getFilesDir() + File.separator + url);
        Bitmap bitmap = null;

        if (file.exists())
            bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

        return bitmap;
    }
}
