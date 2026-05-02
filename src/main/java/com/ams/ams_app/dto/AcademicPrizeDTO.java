package com.ams.ams_app.dto;

public class AcademicPrizeDTO {
    private String id;
    private String academicPrizeId;
    private String        name;
    private String        code;
    private String        level;
    private String        prizeCategory;
    private Double        includedAwardedScoreBonus;
    private Double       notIncludedAwardedScoreBonus;
    private PrizeDTO     prize;
    private Boolean        isActive;
    private String        createdBy;
    private String         createdAt;
    private String        updatedAt;

    public AcademicPrizeDTO(String id, String academicPrizeId, String name, String code, String level, String prizeCategory, Double includedAwardedScoreBonus, Double notIncludedAwardedScoreBonus, PrizeDTO prize) {
        this.id = id;
        this.academicPrizeId = academicPrizeId;
        this.name = name;
        this.code = code;
        this.level = level;
        this.prizeCategory = prizeCategory;
        this.includedAwardedScoreBonus = includedAwardedScoreBonus;
        this.notIncludedAwardedScoreBonus = notIncludedAwardedScoreBonus;
        this.prize = prize;
    }

    public AcademicPrizeDTO(String id, String academicPrizeId, String name, String code, String level, String prizeCategory, Double includedAwardedScoreBonus, Double notIncludedAwardedScoreBonus, Boolean isActive, String createdBy, String createdAt, String updatedAt) {
        this.id = id;
        this.academicPrizeId = academicPrizeId;
        this.name = name;
        this.code = code;
        this.level = level;
        this.prizeCategory = prizeCategory;
        this.includedAwardedScoreBonus = includedAwardedScoreBonus;
        this.notIncludedAwardedScoreBonus = notIncludedAwardedScoreBonus;
        this.isActive = isActive;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public AcademicPrizeDTO(String id, String academicPrizeId, String name, String code) {
        this.id = id;
        this.academicPrizeId = academicPrizeId;
        this.name = name;
        this.code = code;
    }

    public PrizeDTO getPrize() {
        return prize;
    }

    public void setPrize(PrizeDTO prize) {
        this.prize = prize;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAcademicPrizeId() {
        return academicPrizeId;
    }

    public void setAcademicPrizeId(String academicPrizeId) {
        this.academicPrizeId = academicPrizeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getPrizeCategory() {
        return prizeCategory;
    }

    public void setPrizeCategory(String prizeCategory) {
        this.prizeCategory = prizeCategory;
    }

    public Double getIncludedAwardedScoreBonus() {
        return includedAwardedScoreBonus;
    }

    public void setIncludedAwardedScoreBonus(Double includedAwardedScoreBonus) {
        this.includedAwardedScoreBonus = includedAwardedScoreBonus;
    }

    public Double getNotIncludedAwardedScoreBonus() {
        return notIncludedAwardedScoreBonus;
    }

    public void setNotIncludedAwardedScoreBonus(Double notIncludedAwardedScoreBonus) {
        this.notIncludedAwardedScoreBonus = notIncludedAwardedScoreBonus;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
