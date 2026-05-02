package com.ams.ams_app.session;

import com.ams.ams_app.dto.UserDTO;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class SessionStorage {
    private static final String FILE = "session.json";
    public static void save(UserSession session) {
        try {
            // Parse
            JSONObject userJson = new JSONObject();
            userJson.put("id", session.getUserInfor().getId());
            userJson.put("userName", session.getUserInfor().getUsername());
            userJson.put("fullName", session.getUserInfor().getFullName());
            userJson.put("isLocker", session.getUserInfor().getLocked());
            userJson.put("createdBy", session.getUserInfor().getCreatedBy());
            userJson.put("createdAt", session.getUserInfor().getCreatedAt());
            userJson.put("updatedAt", session.getUserInfor().getUpdatedAt());
            userJson.put("roleIds", session.getUserInfor().getRoleIds());
            JSONObject obj = new JSONObject();
            obj.put("user", userJson);
            obj.put("token", session.getToken());
            obj.put("refreshToken", session.getRefreshtoken());

            FileWriter file = new FileWriter(FILE);
            file.write(obj.toString());
            file.close();
            System.out.println("Đã lưu thông tin người dùng kèm token!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static UserSession load() {
        try {
            String content = new String(Files.readAllBytes(Paths.get("session.json")));
            JSONObject obj = new JSONObject(content);
            JSONObject userJson = obj.getJSONObject("user");
            JSONArray roleArray = userJson.optJSONArray("roleIds");
            ArrayList<String> roleIds = new ArrayList<>();

            if (roleArray != null) {
                for (int i = 0; i < roleArray.length(); i++) {
                    roleIds.add(roleArray.optString(i));
                }
            }
            UserDTO createdByUser = new UserDTO(userJson.optString("createdBy"));
            UserDTO user = new UserDTO(
                    userJson.optString("id"),
                    userJson.optString("userName"),
                    userJson.optString("fullName"),
                    userJson.optBoolean("isLocker"),
                    createdByUser,
                    userJson.optString("createdAt"),
                    userJson.optString("updatedAt"),
                    roleIds
            );
            return new UserSession(
                    user,
                    obj.getString("token"),
                    obj.getString("refreshToken")
            );

        } catch (Exception e) {
            return null;
        }
    }
}
