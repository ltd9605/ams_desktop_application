package com.ams.ams_app.dto;

import java.util.ArrayList;

public class UserDTO {
     private String       id;
    private String        username;
    private String         fullName;
    private Boolean         isLocked;
    private UserDTO        createdBy;
    private String         createdAt;
    private String         updatedAt;
    private ArrayList<String>         roleIds;

    public UserDTO(String id, String username, String fullName, Boolean isLocked, UserDTO createdBy, String createdAt, String updatedAt, ArrayList<String> roleIds) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.isLocked = isLocked;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.roleIds = roleIds;
    }

    public UserDTO(UserDTO createdBy) {
        this.createdBy = createdBy;
    }

    public UserDTO(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Boolean getLocked() {
        return isLocked;
    }

    public void setLocked(Boolean locked) {
        isLocked = locked;
    }

    public UserDTO getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserDTO createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public ArrayList<String> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(ArrayList<String> roleIds) {
        this.roleIds = roleIds;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", fullName='" + fullName + '\'' +
                ", isLocked=" + isLocked +
                '}';
    }
}
