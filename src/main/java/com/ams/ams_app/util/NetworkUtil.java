package com.ams.ams_app.util;

import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkUtil {
    // BACKEND URL
    private static final String SERVER_URL = "http://localhost:3000/api/v1";
    
    public static boolean isOnline() {
        try {
            URL url = new URL(SERVER_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(3000);
            connection.setRequestMethod("HEAD");

            int responseCode = connection.getResponseCode();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}