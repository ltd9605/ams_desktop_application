package com.ams.ams_app.dto;

import java.util.ArrayList;

public class StudentDetailDTO {
    private StudentDTO studentInfor;
    private ArrayList<ScoresDTO> scores;
    private ArrayList<AspirationDTO> aspirations;
    private ArrayList<CertificateDTO> certificates;
    private ArrayList<AcademicPrizeDTO> academicPrizes;

    public StudentDetailDTO(StudentDTO studentInfor, ArrayList<ScoresDTO> scores, ArrayList<AspirationDTO> aspirations, ArrayList<CertificateDTO> certificates, ArrayList<AcademicPrizeDTO> academicPrizes) {
        this.studentInfor = studentInfor;
        this.scores = scores;
        this.aspirations = aspirations;
        this.certificates = certificates;
        this.academicPrizes = academicPrizes;
    }

    public StudentDetailDTO(StudentDTO studentInfor, ArrayList<ScoresDTO> scores, ArrayList<AspirationDTO> aspirations, ArrayList<CertificateDTO> certificates) {
        this.studentInfor = studentInfor;
        this.scores = scores;
        this.aspirations = aspirations;
        this.certificates = certificates;
    }

    public StudentDetailDTO() {

    }

    public ArrayList<AcademicPrizeDTO> getAcademicPrizes() {
        return academicPrizes;
    }

    public void setAcademicPrizes(ArrayList<AcademicPrizeDTO> academicPrizes) {
        this.academicPrizes = academicPrizes;
    }

    public StudentDTO getStudentInfor() {
        return studentInfor;
    }

    public void setStudentInfor(StudentDTO studentInfor) {
        this.studentInfor = studentInfor;
    }

    public ArrayList<ScoresDTO> getScores() {
        return scores;
    }

    public void setScores(ArrayList<ScoresDTO> scores) {
        this.scores = scores;
    }

    public ArrayList<AspirationDTO> getAspirations() {
        return aspirations;
    }

    public void setAspirations(ArrayList<AspirationDTO> aspirations) {
        this.aspirations = aspirations;
    }

    public ArrayList<CertificateDTO> getCertificates() {
        return certificates;
    }

    public void setCertificates(ArrayList<CertificateDTO> certificates) {
        this.certificates = certificates;
    }
}
