package com.ams.ams_app.dto;

public class StatusCheckDTO {
    private AdmittedAspiration admittedAspiration;
    private String fullName;
    private String matriculateStatus;
    private String notes;
    private String registrationNumber;
    public AdmittedAspiration getAdmittedAspiration() {
        return admittedAspiration;
    }

    public void setAdmittedAspiration(AdmittedAspiration admittedAspiration) {
        this.admittedAspiration = admittedAspiration;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMatriculateStatus() {
        return matriculateStatus;
    }

    public void setMatriculateStatus(String matriculateStatus) {
        this.matriculateStatus = matriculateStatus;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }
}