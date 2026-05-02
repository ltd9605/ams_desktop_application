package com.ams.ams_app.dto;

public class SubCriteriaDTO {
     private AdmissionPlanDTO admissionPlan;
     private String       createdAt;
    private String       id;
    private String        name;
    private int        priorityLevel;
    private SubjectDTO        subject;
    private String        type;
    private String        updatedAt;
    public SubCriteriaDTO(String createdAt, AdmissionPlanDTO admissionPlan, String id, String name, int priorityLevel, SubjectDTO subject, String type, String updatedAt) {
        this.createdAt = createdAt;
        this.admissionPlan = admissionPlan;
        this.id = id;
        this.name = name;
        this.priorityLevel = priorityLevel;
        this.subject = subject;
        this.type = type;
        this.updatedAt = updatedAt;
    }

    public SubCriteriaDTO(String id, String name, int priorityLevel, String type, SubjectDTO subject) {
        this.id = id;
        this.name = name;
        this.priorityLevel = priorityLevel;
        this.type = type;
        this.subject = subject;
    }

    public AdmissionPlanDTO getAdmissionPlan() {
        return admissionPlan;
    }

    public void setAdmissionPlan(AdmissionPlanDTO admissionPlan) {
        this.admissionPlan = admissionPlan;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPriorityLevel() {
        return priorityLevel;
    }

    public void setPriorityLevel(int priorityLevel) {
        this.priorityLevel = priorityLevel;
    }

    public SubjectDTO getSubject() {
        return subject;
    }

    public void setSubject(SubjectDTO subject) {
        this.subject = subject;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
