package com.ams.ams_app.services;
import com.ams.ams_app.dto.ApiResponseDTO;
import com.ams.ams_app.dto.UserDTO;
import com.ams.ams_app.session.SessionStorage;
import com.ams.ams_app.session.UserSession;
import com.ams.ams_app.network.ApiClient;
import com.ams.ams_app.util.TokenUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Base64;
public class AuthService {
    public static ApiResponseDTO<?> login(String username, String password) {
        try {
            JSONObject json = new JSONObject();
            json.put("username", username);
            json.put("password", password);
            // Send request
            ApiResponseDTO<?> response = ApiClient.post("/auth/login", json.toString());
            System.out.println("SERVICE : Response message :  " +response.getMessage());
            if (!response.isSuccess()) return response;
            // Get token
            String token = TokenUtil.getAccessToken(response.getHeaders());
            String refreshToken = TokenUtil.getRefreshToken(response.getHeaders());
            // Parse User
            JSONObject data = (JSONObject) response.getData();
            JSONArray roleArray = data.optJSONArray("roleIds");
            ArrayList<String> roleIds = new ArrayList<>();
            if (roleArray != null) {
                for (int i = 0; i < roleArray.length(); i++) {
                    roleIds.add(roleArray.optString(i));
                }
            }
            UserDTO createdByUser = new UserDTO(data.optString("createdBy"));
            UserDTO curentUser = new UserDTO(data.optString("id"),
                    data.optString("username"),
                    data.optString("fullName"),
                    data.optBoolean("isLocker"),
                    createdByUser,
                    data.optString("createdAt"),
                    data.optString("updatedAt"),
                    roleIds
                    );
            // Init session
            UserSession.init(
                    curentUser,
                    token,
                    refreshToken
            );
            SessionStorage.save(UserSession.getInstance());
            return response;
        } catch (Exception e) {
            System.err.println("SERVICE : LOGIN ERROR: " + e.getMessage());
            return null;
        }
    }
    public static boolean logout(UserSession userSession) {
        try {
            JSONObject body = new JSONObject();
            ApiResponseDTO<?> response = ApiClient.postWithToken("/auth/logout", body, userSession.getToken());
            if (response.isSuccess()) {
                java.nio.file.Files.deleteIfExists(java.nio.file.Paths.get("session.json"));
                UserSession.clean();
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("SERVICE : Lỗi đăng xuất: " + e.getMessage());
            return false;
        }
    }
    public static boolean isTokenExpired(String token) {
        if (token == null || token.isEmpty()) return true;
        try {
            String[] parts = token.split("\\.");
            if (parts.length < 2) return true;

            String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]));
            JSONObject payload = new JSONObject(payloadJson);

            if (!payload.has("exp")) return false;

            long expirationTime = payload.getLong("exp") * 1000;
            long currentTime = System.currentTimeMillis();

            return expirationTime < (currentTime + 5000);

        } catch (Exception e) {
            System.err.println("SERVICE :Lỗi khi parse token: " + e.getMessage());
            return true;
        }
    }
    public static boolean refreshToken(UserSession session) {
        try {
            // Send request
            ApiResponseDTO<?> response = ApiClient.callRefreshToken("/auth/refresh-token", session.getRefreshtoken());
            if (response.isSuccess()) {
                    String newAccessToken = TokenUtil.getAccessToken(response.getHeaders());
                    session.setToken(newAccessToken);
                    SessionStorage.save(session);
                System.out.println("SERVICE : Refresh message  :  " + response.getMessage());
                    return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}