package com.ams.ams_app.services;

import com.ams.ams_app.dto.ApiResponseDTO;
import com.ams.ams_app.network.ApiClient;
import java.io.File;

public class ImportService {
    private String getEndpointByType(String importType) {
        switch (importType) {
            case "Hồ sơ Thí sinh": return "/import-logs/students";
            case "Nguyện vọng": return "/import-logs/aspirations";
            case "Điểm giải thưởng": return "/import-logs/academic-prize-grades";
            case "Giải thưởng thí sinh": return "/import-logs/student-academic-prize-grades";
            default: return "/import-logs/students";
        }
    }

    public ApiResponseDTO<?> uploadExcelFile(File file, String importType) throws Exception {
        String endpoint = getEndpointByType(importType);
        return ApiClient.uploadFile(endpoint, file);
    }
}