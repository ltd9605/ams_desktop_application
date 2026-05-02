package com.ams.ams_app.services;

import com.ams.ams_app.dto.ApiResponseDTO;
import com.ams.ams_app.dto.RoleDTO;
import com.ams.ams_app.dto.UserDTO;
import com.ams.ams_app.network.ApiClient;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

public class UserService {
    public ArrayList<UserDTO> getAllUsers() throws Exception {
        ApiResponseDTO<?> response = ApiClient.get("/users?limit=100");
        ArrayList<UserDTO> list = new ArrayList<>();
        if (response.isSuccess()) {
            JSONObject data = (JSONObject) response.getData();
            JSONArray items = data.getJSONArray("items");
            for (int i = 0; i < items.length(); i++) {
                list.add(parseUser(items.getJSONObject(i)));
            }
        }
        return list;
    }
    public ArrayList<RoleDTO> getAllRoles() throws Exception {
        ApiResponseDTO<?> response = ApiClient.get("/roles?limit=50");
        ArrayList<RoleDTO> list = new ArrayList<>();
        if (response.isSuccess()) {
            JSONObject data = (JSONObject) response.getData();
            JSONArray items = data.getJSONArray("items");
            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                list.add(new RoleDTO(item.getString("id"), item.getString("name"), null, null, null, null));
            }
        }
        return list;
    }

    public boolean updateUser(String id, JSONObject userJson) throws Exception {
        return ApiClient.patch("/users/" + id, userJson).isSuccess();
    }

    public boolean deleteUser(String id) throws Exception {
        return ApiClient.delete("/users/" + id).isSuccess();
    }
    public boolean changeMyPassword(String oldPassword, String newPassword) throws Exception {
        JSONObject body = new JSONObject();
        body.put("currentPassword", oldPassword);
        body.put("newPassword", newPassword);
        ApiResponseDTO<?> response = ApiClient.put("/users/me/password", body);
        return response.isSuccess();
    }


    private UserDTO parseUser(JSONObject json) {
        JSONArray roleArray = json.optJSONArray("roleIds");
        ArrayList<String> roles = new ArrayList<>();
        if (roleArray != null) {
            for (int i = 0; i < roleArray.length(); i++) roles.add(roleArray.getString(i));
        }

        JSONObject creatorJson = json.optJSONObject("createdBy");
        UserDTO creator = (creatorJson != null) ? new UserDTO(creatorJson.optString("id"), null, creatorJson.optString("fullName"), null, null, null, null, null) : null;

        return new UserDTO(
                json.getString("id"),
                json.getString("username"),
                json.getString("fullName"),
                json.optBoolean("isLocked"),
                creator,
                json.optString("createdAt"),
                json.optString("updatedAt"),
                roles
        );
    }
}