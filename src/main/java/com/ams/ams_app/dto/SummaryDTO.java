package com.ams.ams_app.dto;

public class SummaryDTO{
        private int totalStudentsReviewed;
        private int totalStudentsSkipped;
        private int totalStudentsPassed;
        private int totalStudentsFailed;
        private String notes;

        public SummaryDTO(int totalStudentsReviewed, int totalStudentsSkipped, int totalStudentsPassed, int totalStudentsFailed, String notes) {
            this.totalStudentsReviewed = totalStudentsReviewed;
            this.totalStudentsSkipped = totalStudentsSkipped;
            this.totalStudentsPassed = totalStudentsPassed;
            this.totalStudentsFailed = totalStudentsFailed;
            this.notes = notes;
        }

        public int getTotalStudentsReviewed() {
            return totalStudentsReviewed;
        }

        public void setTotalStudentsReviewed(int totalStudentsReviewed) {
            this.totalStudentsReviewed = totalStudentsReviewed;
        }

        public int getTotalStudentsSkipped() {
            return totalStudentsSkipped;
        }

        public void setTotalStudentsSkipped(int totalStudentsSkipped) {
            this.totalStudentsSkipped = totalStudentsSkipped;
        }

        public int getTotalStudentsPassed() {
            return totalStudentsPassed;
        }

        public void setTotalStudentsPassed(int totalStudentsPassed) {
            this.totalStudentsPassed = totalStudentsPassed;
        }

        public int getTotalStudentsFailed() {
            return totalStudentsFailed;
        }

        public void setTotalStudentsFailed(int totalStudentsFailed) {
            this.totalStudentsFailed = totalStudentsFailed;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }
    }
