package com.ams.ams_app.services;

import com.ams.ams_app.dto.*;
import com.ams.ams_app.network.ApiClient;
import com.ams.ams_app.session.UserSession;
import com.ams.ams_app.util.CacheManager;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class StudentService {
    // Get student list (&search,status )
    private final String CACHE_KEY = "student_list";
    // Get student list (&search, status)
    public ArrayList<StudentDTO> getList(int page, int limit, String searchTerm, String status) throws Exception {
        int offset = page * limit;
        ArrayList<StudentDTO> studentList = new ArrayList<>();
        UserSession session = UserSession.getInstance();

        //  OFFLINE
        if (session != null && session.isOfflineMode()) {
            String cachedJson = CacheManager.readCache(CACHE_KEY);
            if (cachedJson != null) {
                JSONObject data = new JSONObject(cachedJson);
                ArrayList<StudentDTO> allStudents = parseStudentArray(data.getJSONArray("items"));

                filterStudents(allStudents, searchTerm, status);

                int start = Math.min(offset, allStudents.size());
                int end = Math.min(offset + limit, allStudents.size());
                studentList = new ArrayList<>(allStudents.subList(start, end));
            } else {
                throw new Exception("Dữ liệu ngoại tuyến chưa sẵn sàng. Vui lòng đồng bộ khi có mạng.");
            }
        }
        // ONLINE
        else {
            String url = buildUrl("/students", limit, offset, searchTerm, status);
            ApiResponseDTO<?> response = ApiClient.get(url);

            if (response.isSuccess()) {
                JSONObject data = (JSONObject) response.getData();
                studentList = parseStudentArray(data.getJSONArray("items"));

                if (isGeneralView(searchTerm, status)) {
                    new Thread(() -> {
                        try { syncAllStudents(); } catch (Exception e) { e.printStackTrace(); }
                    }).start();
                }
            }
        }
        return studentList;
    }

    // HELPER
    private ArrayList<StudentDTO> parseStudentArray(JSONArray items) {
        ArrayList<StudentDTO> list = new ArrayList<>();
        for (int i = 0; i < items.length(); i++) {
            try {
                JSONObject item = items.getJSONObject(i);
                String createdAt = item.has("createdAt") ? LocalDateTime.parse(item.getString("createdAt"))
                        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "";
                String updatedAt = item.has("updatedAt") ? LocalDateTime.parse(item.getString("updatedAt"))
                        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "";

                list.add(new StudentDTO(
                        item.optString("citizenIdCard"),
                        createdAt,
                        item.optString("dob"),
                        item.optInt("enrollmentYear"),
                        item.optString("fullName"),
                        item.optString("gender"),
                        item.optString("id"),
                        item.optString("matriculateStatus"),
                        item.optDouble("priorityPoints", 0.0),
                        item.optString("registrationNumber"),
                        updatedAt
                ));
            } catch (Exception e) {
                System.err.println("Lỗi parse dữ liệu thí sinh: " + e.getMessage());
            }
        }
        return list;
    }
    // Get total page
    public int getTotalPages() throws Exception {
        UserSession session = UserSession.getInstance();
        if (session != null && session.isOfflineMode()) return 1;

        String url = "/students";
        ApiResponseDTO<?> response = ApiClient.get(url);
        if (response.isSuccess()) {
            JSONObject data = (JSONObject) response.getData();
            return data.optInt("totalPages", 1);
        }
        return 1;
    }

    // Get total by major
    public int getTotalPagesByMajor(int limit, String majorId) throws Exception {
        UserSession session = UserSession.getInstance();
        if (session != null && session.isOfflineMode()) return 1;

        String url = "/students?limit=" + limit + "&offset=0&majorId=" + majorId;
        ApiResponseDTO<?> response = ApiClient.get(url);
        if (response.isSuccess()) {
            JSONObject data = (JSONObject) response.getData();
            return data.optInt("totalPages", 1);
        }
        return 1;
    }

    // Get a student detail
    public StudentDetailDTO getStudentDetail(String id) throws Exception {
        UserSession session = UserSession.getInstance();
        if (session != null && session.isOfflineMode()) {
            throw new Exception("Tính năng xem chi tiết thí sinh không khả dụng trong chế độ Ngoại tuyến.");
        }

        try {
            String url = "/students/" + id + "/details";
            ApiResponseDTO<?> response = ApiClient.get(url);
            System.out.println("SERVICE : Response message :  " + response.getMessage());
            if (response.isSuccess()) {
                JSONObject data = (JSONObject) response.getData();
                StudentDTO studentInfor = new StudentDTO(
                        data.optString("citizenIdCard"),
                        data.optString("createdAt"),
                        data.optString("dob"),
                        data.optInt("enrollmentYear"),
                        data.optString("fullName"),
                        data.optString("gender"),
                        data.optString("id"),
                        data.optString("matriculateStatus"),
                        data.optDouble("priorityPoints"),
                        data.optString("registrationNumber"),
                        data.optString("updatedAt")
                );
                ArrayList<ScoresDTO> scores = new ArrayList<>();
                JSONArray scoreArray = data.getJSONObject("scores").optJSONArray("scores");
                if (scoreArray != null) {
                    for (int i = 0; i < scoreArray.length(); i++){
                        JSONObject scoreObj = scoreArray.getJSONObject(i);
                        JSONObject subjectObj = scoreObj.getJSONObject("subject");
                        SubjectDTO subject = new SubjectDTO(
                                subjectObj.optString("id"),
                                subjectObj.optString("name"),
                                subjectObj.optString("code"),
                                subjectObj.optString("createdBy"),
                                subjectObj.optString("createdAt"),
                                subjectObj.optString("updatedAt")
                        );
                        ScoresDTO score = new ScoresDTO(subject, scoreObj.optDouble("score"));
                        scores.add(score);
                    }
                }
                ArrayList<AspirationDTO> aspirations = new ArrayList<>();
                JSONArray aspirationArray = data.getJSONObject("aspirations").optJSONArray("aspirations");
                if(aspirationArray != null){
                    for (int i = 0; i < aspirationArray.length() ; i++) {
                        JSONObject aspirationObj = aspirationArray.getJSONObject(i);
                        JSONObject combinationObj = aspirationObj.getJSONObject("combination");
                        CombinationDTO combination = new CombinationDTO(
                                combinationObj.optString("id"),
                                combinationObj.optString("name"),
                                combinationObj.optString("code"),
                                combinationObj.getJSONObject("subjects").getJSONArray("subjects"),
                                combinationObj.optString("createdAt"),
                                combinationObj.optString("updatedAt")
                        );
                        JSONObject admissionPlanObj = aspirationObj.getJSONObject("admissionPlan");
                        MajorDTO major = new MajorDTO(admissionPlanObj.getJSONObject("major").getString("id"),
                                admissionPlanObj.getJSONObject("major").getString("name"),
                                admissionPlanObj.getJSONObject("major").getString("code"));
                        AdmissionPlanDTO admissionPlan = new AdmissionPlanDTO(admissionPlanObj.optString("id"),
                                admissionPlanObj.optString("name"), admissionPlanObj.optInt("academicYear"), major);
                        AspirationDTO aspiration = new AspirationDTO(
                                aspirationObj.optInt("priorityOrder"),
                                aspirationObj.optString("type"),
                                aspirationObj.optDouble("totalScore"),
                                aspirationObj.optString("status"),
                                combination,
                                admissionPlan
                        );
                        aspirations.add(aspiration);
                    }
                }
                ArrayList<CertificateDTO> certificates = new ArrayList<>();
                JSONArray certificateArray = data.getJSONObject("englishCertificates").optJSONArray("certificates");
                if (certificateArray != null) {
                    for (int i = 0; i < certificateArray.length() ; i++) {
                        JSONObject certificateObj = certificateArray.getJSONObject(i);
                        EnglishCertificateDTO En_certificate = new EnglishCertificateDTO(
                                certificateObj.getJSONObject("certificate").optString("id"),
                                certificateObj.getJSONObject("certificate").optString("name"),
                                certificateObj.getJSONObject("certificate").optString("code"));
                        CertificateDTO certificate = new CertificateDTO(
                                certificateObj.optString("gradeId"),
                                certificateObj.optString("gradeName"),
                                certificateObj.optString("gradeCode"),
                                certificateObj.optDouble("convertScore"),
                                certificateObj.optDouble("bonusScore"),
                                En_certificate
                        );
                        certificates.add(certificate);
                    }
                }
                ArrayList<AcademicPrizeDTO> academicPrizes = new ArrayList<>();
                JSONArray academicArray = data.getJSONObject("academicPrizes").optJSONArray("prizes");
                if (academicArray != null){
                    for (int i = 0; i < academicArray.length() ; i++) {
                        JSONObject academicPrizeObj = academicArray.getJSONObject(i);
                        PrizeDTO prize = new PrizeDTO(
                                academicPrizeObj.getJSONObject("prize").optString("id"),
                                academicPrizeObj.getJSONObject("prize").optString("subjectId"),
                                academicPrizeObj.getJSONObject("prize").optString("name"),
                                academicPrizeObj.getJSONObject("prize").optString("code"));
                        AcademicPrizeDTO academicPrize = new AcademicPrizeDTO(
                                academicPrizeObj.optString("gradeId"),
                                academicPrizeObj.optString("gradeId"),
                                academicPrizeObj.optString("gradeName"),
                                academicPrizeObj.optString("gradeCode"),
                                academicPrizeObj.optString("gradeLevel"),
                                academicPrizeObj.optString("prizeCategory"),
                                academicPrizeObj.optDouble("includedAwardedScoreBonus"),
                                academicPrizeObj.optDouble("notIncludedAwardedScoreBonus") ,
                                prize
                        );
                        academicPrizes.add(academicPrize);
                    }
                }

                return new StudentDetailDTO(studentInfor, scores, aspirations, certificates, academicPrizes);
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi xử lý dữ liệu học sinh: " + e.getMessage(), e);
        }
        return new StudentDetailDTO();
    }

    public int countAll() throws Exception {
        UserSession session = UserSession.getInstance();
        if (session != null && session.isOfflineMode()) {
            String cachedJson = CacheManager.readCache("student_list");
            if (cachedJson != null) return new JSONObject(cachedJson).optInt("total", 0);
            return 0;
        }

        String url = "/students";
        ApiResponseDTO<?> response = ApiClient.get(url);
        if (response.isSuccess()) {
            JSONObject data = (JSONObject) response.getData();
            return data.optInt("total", 1);
        }
        return 1;
    }
    public int countByStatus(String status) throws Exception {
        UserSession session = UserSession.getInstance();
        if (session != null && session.isOfflineMode()) return 0;

        String url = "/students?";
        if (status != null && !status.equalsIgnoreCase("Tất cả trạng thái")) {
            url += "&matriculateStatus=" + mapStatusToApi(status).toUpperCase();
        }
        ApiResponseDTO<?> response = ApiClient.get(url);

        if (response.isSuccess()) {
            JSONObject data = (JSONObject) response.getData();
            return data.optInt("total", 0);
        }
        return 0;
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
    private void filterStudents(ArrayList<StudentDTO> list, String searchTerm, String status) {
        if (status != null && !status.isEmpty() && !status.equalsIgnoreCase("Tất cả trạng thái")) {
            String apiStatus = mapStatusToApi(status);
            list.removeIf(s -> s.getMatriculateStatus() == null || !s.getMatriculateStatus().equalsIgnoreCase(apiStatus));
        }
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            String keyword = searchTerm.trim().toLowerCase();
            list.removeIf(s -> !s.getFullName().toLowerCase().contains(keyword) &&
                    !s.getRegistrationNumber().toLowerCase().contains(keyword));
        }
    }

    // HELPER
    private String buildUrl(String base, int limit, int offset, String search, String status) throws Exception {
        StringBuilder sb = new StringBuilder(base + "?limit=" + limit + "&offset=" + offset);
        if (search != null && !search.isEmpty()) {
            sb.append("&searchTerm=").append(java.net.URLEncoder.encode(search, "UTF-8"));
        }
        if (status != null && !status.isEmpty() && !status.equalsIgnoreCase("Tất cả trạng thái")) {
            sb.append("&matriculateStatus=").append(mapStatusToApi(status).toUpperCase());
        }
        return sb.toString();
    }

    private boolean isGeneralView(String search, String status) {
        return (search == null || search.isEmpty()) && (status == null || status.equalsIgnoreCase("Tất cả trạng thái"));
    }
    public void syncAllStudents() throws Exception {
        // limit = 10000 to get all of students
        String url = "/students?limit=10000&offset=0";
        ApiResponseDTO<?> response = ApiClient.get(url);

        if (response.isSuccess()) {
            JSONObject data = (JSONObject) response.getData();
            CacheManager.saveCache(CACHE_KEY, data.toString());
            System.out.println("SERVICE: Đã đồng bộ toàn bộ danh sách vào bộ nhớ đệm.");
        }
    }
}