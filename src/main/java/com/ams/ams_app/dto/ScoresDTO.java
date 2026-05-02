package com.ams.ams_app.dto;

public class ScoresDTO {
    private SubjectDTO subjects;
    private Double score;

    public ScoresDTO(SubjectDTO subjects, Double score) {
        this.subjects = subjects;
        this.score = score;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public SubjectDTO getSubjects() {
        return subjects;
    }

    public void setSubjects(SubjectDTO subjects) {
        this.subjects = subjects;
    }
}
