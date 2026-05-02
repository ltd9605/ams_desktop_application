package com.ams.ams_app.dto;

public class StatisticsDTO {
    private int totalStudents;
    private int acceptedCount;
    private int pendingCount;
    private int totalMajors;

    public StatisticsDTO(int totalStudents, int acceptedCount, int pendingCount, int totalMajors) {
        this.totalStudents = totalStudents;
        this.acceptedCount = acceptedCount;
        this.pendingCount = pendingCount;
        this.totalMajors = totalMajors;
    }

    public int getTotalStudents() {
        return totalStudents;
    }

    public void setTotalStudents(int totalStudents) {
        this.totalStudents = totalStudents;
    }

    public int getAcceptedCount() {
        return acceptedCount;
    }

    public void setAcceptedCount(int acceptedCount) {
        this.acceptedCount = acceptedCount;
    }

    public int getPendingCount() {
        return pendingCount;
    }

    public void setPendingCount(int pendingCount) {
        this.pendingCount = pendingCount;
    }

    public int getTotalMajors() {
        return totalMajors;
    }

    public void setTotalMajors(int totalMajors) {
        this.totalMajors = totalMajors;
    }

}
