package com.ams.ams_app.dto;

public class AdmittedAspiration {
    private String combinationCode;
    private String majorCode;
    private String majorName;
    private int priorityOrder;
    private double totalScore;
    private String type;
    
    public String getCombinationCode() {
        return combinationCode;
    }

    public void setCombinationCode(String combinationCode) {
        this.combinationCode = combinationCode;
    }

    public String getMajorCode() {
        return majorCode;
    }

    public void setMajorCode(String majorCode) {
        this.majorCode = majorCode;
    }

    public String getMajorName() {
        return majorName;
    }

    public void setMajorName(String majorName) {
        this.majorName = majorName;
    }

    public int getPriorityOrder() {
        return priorityOrder;
    }

    public void setPriorityOrder(int priorityOrder) {
        this.priorityOrder = priorityOrder;
    }

    public double getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(double totalScore) {
        this.totalScore = totalScore;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}