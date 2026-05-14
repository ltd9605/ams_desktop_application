package com.ams.ams_app.util;

import com.ams.ams_app.config.Config;

import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkUtil {
    // BACKEND URL
    private static final String SERVER_URL = Config.get("API_BASE_URL");
    
    public static boolean isOnline() {
        try {
            URL url = new URL(SERVER_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(Integer.valueOf(Config.get("API_TIMEOUT")));
            connection.setReadTimeout(Integer.valueOf(Config.get("API_TIMEOUT")));
            connection.setRequestMethod("HEAD");

            int responseCode = connection.getResponseCode();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}