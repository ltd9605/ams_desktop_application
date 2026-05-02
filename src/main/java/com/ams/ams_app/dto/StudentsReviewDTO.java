package com.ams.ams_app.dto;

import java.util.ArrayList;

public class StudentsReviewDTO {
        private ArrayList<String>     reviewedStudentIds;
        private ArrayList<String>    skippedStudentIds;
        private SummaryDTO    summary;

    public StudentsReviewDTO(ArrayList<String> reviewedStudentIds, ArrayList<String> skippedStudentIds, SummaryDTO summary) {
        this.reviewedStudentIds = reviewedStudentIds;
        this.skippedStudentIds = skippedStudentIds;
        this.summary = summary;
    }

    public StudentsReviewDTO() {

    }

    public ArrayList<String> getReviewedStudentIds() {
        return reviewedStudentIds;
    }

    public void setReviewedStudentIds(ArrayList<String> reviewedStudentIds) {
        this.reviewedStudentIds = reviewedStudentIds;
    }

    public ArrayList<String> getSkippedStudentIds() {
        return skippedStudentIds;
    }

    public void setSkippedStudentIds(ArrayList<String> skippedStudentIds) {
        this.skippedStudentIds = skippedStudentIds;
    }

    public SummaryDTO getSummary() {
        return summary;
    }

    public void setSummary(SummaryDTO summary) {
        this.summary = summary;
    }
}
