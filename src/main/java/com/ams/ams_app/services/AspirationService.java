package com.ams.ams_app.services;

import com.ams.ams_app.dto.*;
import com.ams.ams_app.network.ApiClient;
import com.ams.ams_app.session.UserSession;
import com.ams.ams_app.util.CacheManager;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class AspirationService {
    public ArrayList<AspirationDTO> getAspirationListByMajor(int page, int limit, String majorId, String status, String searchTerm) throws Exception {
        int offset = page * limit;
        String cacheKey = "admitted_major_" + majorId;
        ArrayList<AspirationDTO> aspirationList = new ArrayList<>();
        UserSession session = UserSession.getInstance();
            StringBuilder urlBuilder = new StringBuilder("/aspirations?limit=" + limit + "&offset=" + offset);
            if (majorId != null && !majorId.trim().isEmpty()) {
                urlBuilder.append("&majorId=").append(URLEncoder.encode(majorId, StandardCharsets.UTF_8.toString()));
            }
            if (status != null && !status.isEmpty() && !status.equalsIgnoreCase("Tất cả trạng thái")) {
                String apiStatus = mapStatusToApi(status);
                urlBuilder.append("&status=").append(apiStatus.toUpperCase());
            }
            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                urlBuilder.append("&search=").append(URLEncoder.encode(searchTerm.trim(), StandardCharsets.UTF_8.toString()));
            }

            String url = urlBuilder.toString();
            ApiResponseDTO<?> response = ApiClient.get(url);

            if (response != null && response.isSuccess() && response.getData() != null) {
                JSONObject data = (JSONObject) response.getData();
                if ((searchTerm == null || searchTerm.trim().isEmpty()) &&
                        (status == null || status.equalsIgnoreCase("Tất cả trạng thái"))) {
                    CacheManager.saveCache(cacheKey, data.toString());
                }

                if (data.has("items")) {
                    aspirationList = parseAspirationArray(data.getJSONArray("items"));
                }
            }
        System.out.println("SERVICE: Retrieved " + aspirationList.size() + " aspirations.");
        return aspirationList;
    }
    // HELPER
    private ArrayList<AspirationDTO> parseAspirationArray(JSONArray itemsArray) {
        ArrayList<AspirationDTO> list = new ArrayList<>();
        for (int i = 0; i < itemsArray.length(); i++) {
            JSONObject itemJson = itemsArray.getJSONObject(i);

            AdmissionPlanDTO admissionPlan = null;
            if (itemJson.has("admissionPlan")) {
                JSONObject planJson = itemJson.getJSONObject("admissionPlan");
                MajorDTO major = null;
                if (planJson.has("major")) {
                    JSONObject majorJson = planJson.getJSONObject("major");
                    major = new MajorDTO(
                            majorJson.optString("id"),
                            majorJson.optString("name"),
                            majorJson.optString("code")
                    );
                }
                admissionPlan = new AdmissionPlanDTO(
                        planJson.optString("id"),
                        planJson.optString("name"),
                        planJson.optInt("academicYear"),
                        major
                );
            }

            CombinationDTO combination = null;
            if (itemJson.has("combination")) {
                JSONObject combiJson = itemJson.getJSONObject("combination");
                combination = new CombinationDTO(
                        combiJson.optString("id"),
                        combiJson.optString("name"),
                        combiJson.optString("code"),
                        combiJson.optJSONArray("subjectIds")
                );
            }

            StudentDTO student = null;
            if (itemJson.has("student")) {
                JSONObject studentJson = itemJson.getJSONObject("student");
                student = new StudentDTO(
                        studentJson.optString("fullName"),
                        studentJson.optString("id")
                );
            }

            AspirationDTO aspiration = new AspirationDTO(
                    admissionPlan,
                    combination,
                    itemJson.optString("createdAt"),
                    itemJson.optString("id"),
                    itemJson.optInt("priorityOrder"),
                    itemJson.optString("status"),
                    student,
                    itemJson.optDouble("totalScore"),
                    itemJson.optString("studentId"),
                    itemJson.optString("updatedAt"),
                    itemJson.optString("type")
            );

            list.add(aspiration);
        }
        return list;
    }
    public int getTotalPages() throws Exception {
        UserSession session = UserSession.getInstance();
        if (session != null && session.isOfflineMode()) {
            return 1;
        }
        String url = "/students";
        ApiResponseDTO<?> response = ApiClient.get(url);
        JSONObject data = (JSONObject) response.getData();
        if (response.isSuccess()) {
            return data.optInt("totalPages", 1);
        }
        return 1;
    }

    // HELPER
    private String mapStatusToApi(String status) {
        return switch (status.toLowerCase()) {
            case "trúng tuyển", "đậu", "accepted" -> "ACCEPTED";
            case "đang chờ", "chờ", "pending" -> "PENDING";
            case "đã trượt", "trượt", "rejected" -> "REJECTED";
            default -> status;
        };
    }
}