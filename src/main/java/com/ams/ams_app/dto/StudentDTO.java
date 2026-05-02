package com.ams.ams_app.dto;

public class StudentDTO {
    private String citizenIdCard;
    private String createdAt;
    private String dob;
    private int   enrollmentYear;
    private String  fullName;
    private String  gender;
    private String id;
    private String matriculateStatus;
    private Double        priorityPoints;
    private String        registrationNumber;
    private String  updatedAt;

    public StudentDTO(String citizenIdCard, String createdAt, String dob, int enrollmentYear, String fullName, String gender, String id, String matriculateStatus, Double priorityPoints, String registrationNumber, String updatedAt) {
        this.citizenIdCard = citizenIdCard;
        this.createdAt = createdAt;
        this.dob = dob;
        this.enrollmentYear = enrollmentYear;
        this.fullName = fullName;
        this.gender = gender;
        this.id = id;
        this.matriculateStatus = matriculateStatus;
        this.priorityPoints = priorityPoints;
        this.registrationNumber = registrationNumber;
        this.updatedAt = updatedAt;
    }

    public StudentDTO(String fullName, String id) {
        this.fullName = fullName;
        this.id = id;
    }

    public String getCitizenIdCard() {
        return citizenIdCard;
    }

    public void setCitizenIdCard(String citizenIdCard) {
        this.citizenIdCard = citizenIdCard;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public int getEnrollmentYear() {
        return enrollmentYear;
    }

    public void setEnrollmentYear(int enrollmentYear) {
        this.enrollmentYear = enrollmentYear;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMatriculateStatus() {
        return matriculateStatus;
    }

    public void setMatriculateStatus(String matriculateStatus) {
        this.matriculateStatus = matriculateStatus;
    }

    public Double getPriorityPoints() {
        return priorityPoints;
    }

    public void setPriorityPoints(Double priorityPoints) {
        this.priorityPoints = priorityPoints;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
