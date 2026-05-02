package com.ams.ams_app.dto;

public class AspirationDTO {
    private AdmissionPlanDTO admissionPlan;
    private CombinationDTO combination;
    private String createdAt;
    private String    id;
    private int        priorityOrder;
    private String        status;
    private StudentDTO      student;
    private String    studentId;
    private Double        totalScore;
    private String       updatedAt;
    private String     type;

    public AspirationDTO(AdmissionPlanDTO admissionPlan, CombinationDTO combination, String createdAt, String id, int priorityOrder, String status, StudentDTO student, Double totalScore, String studentId, String updatedAt, String type) {
        this.admissionPlan = admissionPlan;
        this.combination = combination;
        this.createdAt = createdAt;
        this.id = id;
        this.priorityOrder = priorityOrder;
        this.status = status;
        this.student = student;
        this.totalScore = totalScore;
        this.studentId = studentId;
        this.updatedAt = updatedAt;
        this.type = type;
    }

    public AspirationDTO(int priorityOrder, String type, Double totalScore, String status, CombinationDTO combination, AdmissionPlanDTO admissionPlan) {
        this.priorityOrder = priorityOrder;
        this.type = type;
        this.totalScore = totalScore;
        this.status = status;
        this.combination = combination;
        this.admissionPlan = admissionPlan;
    }

    public AdmissionPlanDTO getAdmissionPlan() {
        return admissionPlan;
    }

    public void setAdmissionPlan(AdmissionPlanDTO admissionPlan) {
        this.admissionPlan = admissionPlan;
    }

    public CombinationDTO getCombination() {
        return combination;
    }

    public void setCombination(CombinationDTO combination) {
        this.combination = combination;
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

    public int getPriorityOrder() {
        return priorityOrder;
    }

    public void setPriorityOrder(int priorityOrder) {
        this.priorityOrder = priorityOrder;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public StudentDTO getStudent() {
        return student;
    }

    public void setStudent(StudentDTO student) {
        this.student = student;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public Double getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Double totalScore) {
        this.totalScore = totalScore;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
