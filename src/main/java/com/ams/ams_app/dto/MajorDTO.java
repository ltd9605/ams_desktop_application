package com.ams.ams_app.dto;

import java.util.ArrayList;

public class MajorDTO {
    private ArrayList<String> admissionPlanIds;
    private String        code;
    private String         createdAt;
    private String         id;
    private boolean        isLocked;
    private String         name;
    private String         updatedAt;

    public MajorDTO(ArrayList<String> admissionPlanIds, String code, String createdAt, String id, boolean isLocked, String name, String updatedAt) {
        this.admissionPlanIds = admissionPlanIds;
        this.code = code;
        this.createdAt = createdAt;
        this.id = id;
        this.isLocked = isLocked;
        this.name = name;
        this.updatedAt = updatedAt;
    }

    public MajorDTO(String id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }

    public MajorDTO(String id, String name, String code, boolean isLocked) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.isLocked = isLocked;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ArrayList<String> getAdmissionPlanIds() {
        return admissionPlanIds;
    }

    public void setAdmissionPlanIds(ArrayList<String> admissionPlanIds) {
        this.admissionPlanIds = admissionPlanIds;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
