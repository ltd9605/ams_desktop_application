package com.ams.ams_app.dto;

import org.json.JSONArray;

import java.util.ArrayList;

public class CombinationDTO {
    private String     id;
    private String     name;
    private String     code;
    private JSONArray subjectIds;
    private String         createdAt;
    private String         updatedAt;

    public CombinationDTO(String id, String name, String code, JSONArray subjectIds, String createdAt, String updatedAt) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.subjectIds = subjectIds;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public CombinationDTO(String id, String name, String code, JSONArray subjectIds) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.subjectIds = subjectIds;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public JSONArray getSubjectIds() {
        return subjectIds;
    }

    public void setSubjectIds(JSONArray subjectIds) {
        this.subjectIds = subjectIds;
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
