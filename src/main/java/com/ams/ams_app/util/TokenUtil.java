package com.ams.ams_app.util;

import java.util.List;
import java.util.Map;

public class TokenUtil {

    public static String getAccessToken(Map<String, List<String>> headers) {
        return extractToken(headers, "jwt");
    }

    public static String getRefreshToken(Map<String, List<String>> headers) {
        return extractToken(headers, "refresh_token");
    }

    /*
    Split the token and decrypt it to check still valid or not
    */
    private static String extractToken(Map<String, List<String>> headers, String key) {
        List<String> cookies = headers.getOrDefault("Set-Cookie", List.of());

        for (String cookie : cookies) {
            if (cookie.startsWith(key + "=")) {
                return cookie.split(";")[0].split("=")[1];
            }
        }
        return "";
    }
}
