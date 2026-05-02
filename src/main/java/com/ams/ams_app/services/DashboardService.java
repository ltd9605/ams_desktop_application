package com.ams.ams_app.services;

import com.ams.ams_app.dto.StatisticsDTO;

public class DashboardService {
    public StatisticsDTO getStatistics() throws Exception {
        MajorService majorServices = new MajorService();
        StudentService studentService =new StudentService();
        try {
            int totalMajors = majorServices.getTotal();
            int totalStudents = studentService.countAll();
            int acceptedCount = studentService.countByStatus("ACCEPTED");
            int pendingCount = studentService.countByStatus("PENDING");
            return new StatisticsDTO(totalStudents, acceptedCount, pendingCount, totalMajors);
        } catch (Exception e) {
            System.err.println("SERVICE : Lỗi getStatistic :  " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
