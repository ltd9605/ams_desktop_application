package com.ams.ams_app.services;

import com.ams.ams_app.dto.AdmittedAspiration;
import com.ams.ams_app.dto.ApiResponseDTO;
import com.ams.ams_app.dto.StatusCheckDTO;
import com.ams.ams_app.network.ApiClient;
import org.json.JSONObject;

public class PublicService {
    public ApiResponseDTO<StatusCheckDTO> statusCheck(String registrationNumber, String citizenIdCard) throws Exception {
        JSONObject json = new JSONObject();
        json.put("registrationNumber", registrationNumber);
        json.put("citizenIdCard", citizenIdCard);

        ApiResponseDTO<?> response = ApiClient.post("/public/admissions/status-check", json.toString());

        System.out.println("SERVICE : Response message : " + response.getMessage());

        if (!response.isSuccess()) {
            return new ApiResponseDTO<>(
                    response.getHeaders(),
                    response.getStatusCode(),
                    false,
                    response.getMessage(),
                    null
            );
        }

        // Parse data
        JSONObject data = new JSONObject(response.getData().toString());

        StatusCheckDTO dto = new StatusCheckDTO();
        dto.setFullName(data.optString("fullName"));
        dto.setMatriculateStatus(data.optString("matriculateStatus"));
        dto.setNotes(data.optString("notes"));
        dto.setRegistrationNumber(data.optString("registrationNumber"));

        // admittedAspiration
        JSONObject aspJson = data.optJSONObject("admittedAspiration");
        if (aspJson != null) {
            AdmittedAspiration asp = new AdmittedAspiration();
            asp.setCombinationCode(aspJson.optString("combinationCode"));
            asp.setMajorCode(aspJson.optString("majorCode"));
            asp.setMajorName(aspJson.optString("majorName"));
            asp.setPriorityOrder(aspJson.optInt("priorityOrder"));
            asp.setTotalScore(aspJson.optDouble("totalScore"));
            asp.setType(aspJson.optString("type"));

            dto.setAdmittedAspiration(asp);
        }

        return new ApiResponseDTO<>(
                response.getHeaders(),
                response.getStatusCode(),
                true,
                response.getMessage(),
                dto
        );
    }
}
