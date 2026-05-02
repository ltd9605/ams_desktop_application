package com.ams.ams_app.dto;

import java.util.ArrayList;

public class AdmissionPlanDTO {
    private int academicYear;
    private double        avgFinalScore;
    private double        avgMinScore;
    private String        code;
    private String        createdAt;
    private String        id;
    private Boolean isClosed;
    private Boolean isRoot;
    private MajorDTO major;
    private String        name;
    private int quota;
    private double      scoreOffset;
    private String updatedAt;

    public AdmissionPlanDTO(int academicYear, double avgFinalScore, double avgMinScore, String code, String createdAt, String id, Boolean isClosed, Boolean isRoot, MajorDTO major, String name, int quota, double scoreOffset, String updatedAt) {
        this.academicYear = academicYear;
        this.avgFinalScore = avgFinalScore;
        this.avgMinScore = avgMinScore;
        this.code = code;
        this.createdAt = createdAt;
        this.id = id;
        this.isClosed = isClosed;
        this.isRoot = isRoot;
        this.major = major;
        this.name = name;
        this.quota = quota;
        this.scoreOffset = scoreOffset;
        this.updatedAt = updatedAt;
    }

    public AdmissionPlanDTO(String id, String name, int academicYear, MajorDTO major) {
        this.id = id;
        this.name = name;
        this.academicYear = academicYear;
        this.major = major;
    }

    public int getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(int academicYear) {
        this.academicYear = academicYear;
    }

    public double getAvgFinalScore() {
        return avgFinalScore;
    }

    public void setAvgFinalScore(double avgFinalScore) {
        this.avgFinalScore = avgFinalScore;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getAvgMinScore() {
        return avgMinScore;
    }

    public void setAvgMinScore(double avgMinScore) {
        this.avgMinScore = avgMinScore;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getClosed() {
        return isClosed;
    }

    public void setClosed(Boolean closed) {
        isClosed = closed;
    }

    public Boolean getRoot() {
        return isRoot;
    }

    public void setRoot(Boolean root) {
        isRoot = root;
    }

    public MajorDTO getMajor() {
        return major;
    }

    public void setMajor(MajorDTO major) {
        this.major = major;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuota() {
        return quota;
    }

    public void setQuota(int quota) {
        this.quota = quota;
    }

    public double getScoreOffset() {
        return scoreOffset;
    }

    public void setScoreOffset(double scoreOffset) {
        this.scoreOffset = scoreOffset;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "AdmissionPlanDTO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
