package com.ams.ams_app.services;

import com.ams.ams_app.dto.*;
import com.ams.ams_app.network.ApiClient;
import com.ams.ams_app.session.UserSession;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class AdmissionService {
    public ArrayList<AdmissionPlanDTO> getAdmissionPlansList() throws Exception {
        try {
            int limit = getTotal();
            String url = "/admission-plans?limit="+limit+"&offset=0";
            ApiResponseDTO<?> response = ApiClient.get(url);
            if (response.isSuccess()) {
                JSONObject data = (JSONObject) response.getData();
                ArrayList<AdmissionPlanDTO> list = new ArrayList<>();
                JSONArray items = data.optJSONArray("items");
                for (int i = 0; i < items.length() ; i++) {
                    JSONObject item = items.getJSONObject(i);
                    MajorDTO major = new MajorDTO(item.getJSONObject("major").optString("id"),
                            item.getJSONObject("major").optString("name"),
                            item.getJSONObject("major").optString("code")
                            );
                    AdmissionPlanDTO admissionPlan = new AdmissionPlanDTO(
                            item.optInt("academicYear"),
                            item.optDouble("avgFinalScore",0.0),
                            item.optDouble("avgMinScore",0.0),
                            item.optString("code"),
                            item.optString("createdAt"),
                            item.optString("id"),
                            item.optBoolean("isClosed"),
                            item.optBoolean("isRoot"),
                            major,
                            item.optString("name"),
                            item.optInt("quota"),
                            item.optDouble("scoreOffset"),
                            item.optString("updatedAt")
                    );
                    list.add(admissionPlan);
                }
                return list;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<AdmissionPlanDTO>();
    }
    public AdmissionPlanDetailDTO getDetail(String id) throws Exception {
        try {
            String url = "/admission-plans/" + id + "/details";
            ApiResponseDTO<?> response = ApiClient.get(url);

            if (response.isSuccess()) {
                JSONObject data = (JSONObject) response.getData();

                JSONObject majorObj = data.getJSONObject("major");
                MajorDTO major = new MajorDTO(
                        majorObj.optString("id"),
                        majorObj.optString("name"),
                        majorObj.optString("code"),
                        majorObj.optBoolean("isLocked")
                );

                AdmissionPlanDTO planInfor = new AdmissionPlanDTO(
                        data.optInt("academicYear"),
                        data.optDouble("avgFinalScore", 0.0),
                        data.optDouble("avgMinScore", 0.0),
                        data.optString("code"),
                        data.optString("createdAt"),
                        data.optString("id"),
                        data.optBoolean("isClosed"),
                        data.optBoolean("isRoot"),
                        major,
                        data.optString("name"),
                        data.optInt("quota"),
                        data.optDouble("scoreOffset"),
                        data.optString("updatedAt")
                );

                ArrayList<CombinationDTO> combinations = new ArrayList<>();
                if (data.has("combinations")) {
                    JSONArray combinationArray = data.getJSONObject("combinations").optJSONArray("combinations");
                    if (combinationArray != null) {
                        for (int i = 0; i < combinationArray.length(); i++) {
                            JSONObject cObj = combinationArray.getJSONObject(i);
                            combinations.add(new CombinationDTO(
                                    cObj.optString("id"),
                                    cObj.optString("name"),
                                    cObj.optString("code"),
                                    cObj.optJSONArray("subjectIds")
                            ));
                        }
                    }
                }

                ArrayList<SubCriteriaDTO> subCriterias = new ArrayList<>();
                if (data.has("subCriteria")) {
                    JSONArray subCriteriaArray = data.getJSONObject("subCriteria").optJSONArray("subCriteria");
                    if (subCriteriaArray != null) {
                        for (int i = 0; i < subCriteriaArray.length(); i++) {
                            JSONObject scObj = subCriteriaArray.getJSONObject(i);
                            SubjectDTO subject = null;
                            if (scObj.has("subject")) {
                                subject = new SubjectDTO(
                                        scObj.getJSONObject("subject").optString("id"),
                                        scObj.getJSONObject("subject").optString("name")
                                );
                            }
                            subCriterias.add(new SubCriteriaDTO(
                                    scObj.optString("id"),
                                    scObj.optString("name"),
                                    scObj.optInt("priorityLevel"),
                                    scObj.optString("type"),
                                    subject
                            ));
                        }
                    }
                }
                return new AdmissionPlanDetailDTO(planInfor, combinations, subCriterias);
            }
        } catch (Exception e) {
            System.err.println("Lỗi parse JSON: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    private int getTotal() throws Exception{
            String url = "/admission-plans";
            ApiResponseDTO<?> response = ApiClient.get(url);
            if (response.isSuccess()) {
                JSONObject data = (JSONObject) response.getData();
                return data.optInt("total", 1);
            }
            return 1;
    }
    public void updateSubcriteriaLevel(String id , SubCriteriaDTO subCriteria) throws Exception{
        String url = "/admission-plan-sub-criteria/"+ id;
        JSONObject body = new JSONObject();
        body.put("priorityLevel",subCriteria.getPriorityLevel());
        ApiResponseDTO<?> response = ApiClient.patch(url,body);
        System.out.println("SERVICE : Response message :  " +response.getMessage());
    }
    public boolean updatePlanInfo(AdmissionPlanDTO plan) throws Exception {
        String url = "/admission-plans/" + plan.getId();
        JSONObject body = new JSONObject();
        body.put("quota", plan.getQuota());
        body.put("avgMinScore", plan.getAvgMinScore());

        ApiResponseDTO<?> response = ApiClient.patch(url, body);
        System.out.println("SERVICE : Response message :  " +response.getMessage());
        return response.isSuccess();
    }
    public StudentsReviewDTO runProcess(String admissionPlanId, ArrayList<String> studentIds) throws Exception {
        try {
            String url = "/students/review";
            JSONObject body = new JSONObject();
            body.put("admissionPlanId", admissionPlanId);
            body.put("action", "PROCESS");
            body.put("notes", "Chạy xét tuyển bằng Desktop App");
            JSONArray studentIdsArray = new JSONArray(studentIds);
            System.out.println("Danh sách id :"+ studentIdsArray.toString());
            body.put("studentIds", studentIdsArray);
            ApiResponseDTO<?> response = ApiClient.postWithToken(url, body, UserSession.getInstance().getToken());
            System.out.println("SERVICE : Response message :  " +response.getMessage());
            JSONObject data = (JSONObject) response.getData();
            System.out.println("RUN PROCESS DATA : " + data.toString());
            if (response.isSuccess()) {
                JSONObject summaryObj = data.getJSONObject("summary");
                SummaryDTO summary = new SummaryDTO(
                        summaryObj.optInt("totalStudentsReviewed"),
                        summaryObj.optInt("totalStudentsSkipped"),
                        summaryObj.optInt("totalStudentsPassed"),
                        summaryObj.optInt("totalStudentsFailed"),
                        summaryObj.optString("notes")
                );

                ArrayList<String> reviewedIds = new ArrayList<>();
                JSONArray reviewedArray = data.optJSONArray("reviewedStudentIds");
                if (reviewedArray != null) {
                    for (int i = 0; i < reviewedArray.length(); i++) {
                        reviewedIds.add(reviewedArray.getString(i));
                    }
                }

                ArrayList<String> skippedIds = new ArrayList<>();
                JSONArray skippedArray = data.optJSONArray("skippedStudentIds");
                if (skippedArray != null) {
                    for (int i = 0; i < skippedArray.length(); i++) {
                        skippedIds.add(skippedArray.getString(i));
                    }
                }

                return new StudentsReviewDTO(reviewedIds, skippedIds, summary);
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi chạy tiến trình xét tuyển: " + e.getMessage());
            e.printStackTrace();
        }
        return new StudentsReviewDTO();
    }
    public boolean updatePlanStatus( AdmissionPlanDTO plan) throws Exception{
        String url = "/admission-plans/" + plan.getId() ;
        JSONObject body = new JSONObject();
        boolean status = !plan.getClosed();
        body.put("isClosed", status);
        ApiResponseDTO<?> response = ApiClient.patch(url, body);
        System.out.println("SERVICE : Response message :  " +response.getMessage());
        return response.isSuccess();
    }
}
