package com.ams.ams_app.dto;

import java.util.ArrayList;

public class RoleDTO {
    private String id;
    private String name;
    private ArrayList<String> permissionIds;
    private String createdBy;
    private String createdAt;
    private String updatedAt;

    public RoleDTO(String id, String name, ArrayList<String> permissionIds, String createdBy, String createdAt, String updatedAt) {
        this.id = id;
        this.name = name;
        this.permissionIds = permissionIds;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getPermissionIds() {
        return permissionIds;
    }

    public void setPermissionIds(ArrayList<String> permissionIds) {
        this.permissionIds = permissionIds;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
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
}
