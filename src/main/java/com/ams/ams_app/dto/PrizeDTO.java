package com.ams.ams_app.dto;

public class PrizeDTO {
            private String id;
    private String subjectId;
    private String    name;
    private String     code;

    public PrizeDTO(String id, String subjectId, String name, String code) {
        this.id = id;
        this.subjectId = subjectId;
        this.name = name;
        this.code = code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
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
}
