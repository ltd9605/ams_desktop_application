package com.ams.ams_app.services;

import com.ams.ams_app.dto.AcademicPrizeDTO;
import com.ams.ams_app.dto.ApiResponseDTO;
import com.ams.ams_app.dto.CertificateDTO;
import com.ams.ams_app.network.ApiClient;
import com.ams.ams_app.session.UserSession;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class CertificateAndAcademicPrizeService {
    public ArrayList<CertificateDTO> getCertificateList() throws Exception{
        try {
            String url = "/english-certificate-grades";
            ApiResponseDTO<?> response = ApiClient.get(url);
            if (response.isSuccess()) {
                JSONObject data = (JSONObject) response.getData();
                ArrayList<CertificateDTO> items = new ArrayList<>();
                JSONArray itemArray = data.getJSONArray("items");
                if (itemArray != null) {
                    for (int i = 0; i < itemArray.length() ; i++) {
                        JSONObject itemObj = itemArray.getJSONObject(i);
                        CertificateDTO item = new CertificateDTO(
                                itemObj.optString("id"),
                                itemObj.optString("name"),
                                itemObj.optString("code")
                        );
                        items.add(item);
                    }
                }
                return items;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<CertificateDTO>();
    }
    public ArrayList<AcademicPrizeDTO> getAcademicPrizeList() throws Exception{
        try {
            String url = "/academic-prize-grades";
            ApiResponseDTO<?> response = ApiClient.get(url);
            if (response.isSuccess()) {
                JSONObject data = (JSONObject) response.getData();
                ArrayList<AcademicPrizeDTO> items = new ArrayList<>();
                JSONArray itemArray = data.getJSONArray("items");
                if (itemArray != null) {
                    for (int i = 0; i < itemArray.length(); i++) {
                        JSONObject itemObj = itemArray.getJSONObject(i);
                        AcademicPrizeDTO item = new AcademicPrizeDTO(
                                itemObj.optString("id"),
                                itemObj.optString("academicPrizeId"),
                                itemObj.optString("name"),
                                itemObj.optString("code")
                        );
                        items.add(item);
                    }
                }
                return items;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  new ArrayList<AcademicPrizeDTO>();
    }
    public void addCertification(String studentId , String certificateId ) throws Exception{
        try {
            String url = "/students/"+studentId+"/english-certificate-grades";
            JSONObject body = new JSONObject();
            body.put("englishCertificateGradeId",certificateId);
            ApiResponseDTO<?> response = ApiClient.postWithToken(url,body, UserSession.getInstance().getToken());
            System.out.println("SERVICE : Respones : "+ response.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void addAcademicPrize(String studentId , String prizeId)throws Exception{
        try {
            String url  = "/students/"+studentId+"/academic-prize-grades";
            JSONObject body = new JSONObject();
            body.put("academicPrizeGradeId",prizeId);
            ApiResponseDTO<?> response = ApiClient.postWithToken(url,body, UserSession.getInstance().getToken());
            System.out.println("SERVICE : Respones : "+ response.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void popCertification(String studentId, String cerId)throws Exception{
        try {
            String url = "/students/"+studentId+"/english-certificate-grades/"+cerId;
            ApiResponseDTO<?> response = ApiClient.delete(url);
            System.out.println("SERVICE : Respones : "+ response.getMessage());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void popAcademicPrize(String studentId, String prizeId) throws Exception{
        try {
            String url = "/students/"+studentId+"/academic-prize-grades/"+prizeId;
            ApiResponseDTO<?> response = ApiClient.delete(url);
            System.out.println("SERVICE : Respones : "+ response.getMessage());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
