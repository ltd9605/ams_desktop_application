package com.ams.ams_app.services;

import com.ams.ams_app.dto.ApiResponseDTO;
import com.ams.ams_app.network.ApiClient;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class AnalysisService {
    public Map<String, Integer> getApplicationsByMajor() throws Exception {
        String url = "/analysis/applications/by-major";
        ApiResponseDTO<?> response = ApiClient.get(url);
        Map<String, Integer> stats = new HashMap<>();
        if (response.isSuccess() && response.getData() != null) {
            JSONObject data = (JSONObject) response.getData();
            JSONArray majors = data.optJSONArray("majors");
            if (majors != null) {
                for (int i = 0; i < majors.length(); i++) {
                    JSONObject item = majors.getJSONObject(i);
                    stats.put(item.optString("majorName"), item.optInt("totalApplications"));
                }
            }
        }
        return stats;
    }
    public JSONArray getRawCutoffPredictions() throws Exception {
        String url = "/analysis/simulation/cutoff-predict";
        ApiResponseDTO<?> response = ApiClient.get(url);
        if (response.isSuccess() && response.getData() != null) {
            JSONObject data = (JSONObject) response.getData();
            return data.optJSONArray("predictions");
        }
        return new JSONArray();
    }
    public Map<String, Double> getAverageScoresByMajor() throws Exception {
        String url = "/analysis/majors/score-average";
        ApiResponseDTO<?> response = ApiClient.get(url);
        Map<String, Double> averages = new HashMap<>();
        if (response.isSuccess() && response.getData() != null) {
            JSONObject data = (JSONObject) response.getData();
            JSONArray items = data.optJSONArray("majors");
            if (items != null) {
                for (int i = 0; i < items.length(); i++) {
                    JSONObject item = items.getJSONObject(i);
                    averages.put(item.optString("majorName"), item.optDouble("averageScore", 0.0));
                }
            }
        }
        return averages;
    }
}