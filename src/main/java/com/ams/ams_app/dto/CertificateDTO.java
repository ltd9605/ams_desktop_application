package com.ams.ams_app.dto;

public class CertificateDTO {
     private String gradeId;
    private String        gradeName;
    private String         gradeCode;
    private Double         convertScore;
    private Double         bonusScore;
    private EnglishCertificateDTO         certificate;

    public CertificateDTO(String gradeId, String gradeName, String gradeCode, Double convertScore, Double bonusScore, EnglishCertificateDTO certificate) {
        this.gradeId = gradeId;
        this.gradeName = gradeName;
        this.gradeCode = gradeCode;
        this.convertScore = convertScore;
        this.bonusScore = bonusScore;
        this.certificate = certificate;
    }

    public CertificateDTO(String gradeId, String gradeName, String gradeCode) {
        this.gradeId = gradeId;
        this.gradeName = gradeName;
        this.gradeCode = gradeCode;
    }

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public Double getConvertScore() {
        return convertScore;
    }

    public void setConvertScore(Double convertScore) {
        this.convertScore = convertScore;
    }

    public String getGradeCode() {
        return gradeCode;
    }

    public void setGradeCode(String gradeCode) {
        this.gradeCode = gradeCode;
    }

    public Double getBonusScore() {
        return bonusScore;
    }

    public void setBonusScore(Double bonusScore) {
        this.bonusScore = bonusScore;
    }

    public EnglishCertificateDTO getCertificate() {
        return certificate;
    }

    public void setCertificate(EnglishCertificateDTO certificate) {
        this.certificate = certificate;
    }

}
