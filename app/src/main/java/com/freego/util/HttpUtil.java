package com.freego.util;

import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by dan on 16/2/15.
 */
public class HttpUtil {

    private static final String ROOT_URL = "http://10.0.2.2:8080/freego/";

    private static HttpURLConnection connection = null;

    private static URL mUrl;

    public static HttpURLConnection getConnection_HttpFileUtil(String pageAction) {

        try {
            mUrl = new URL(ROOT_URL + pageAction);
            Log.d("url", ROOT_URL + pageAction);
            connection = (HttpURLConnection) mUrl.openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return connection;
    }

}
