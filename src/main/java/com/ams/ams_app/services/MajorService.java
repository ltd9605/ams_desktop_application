package com.ams.ams_app.services;

import com.ams.ams_app.dto.ApiResponseDTO;
import com.ams.ams_app.dto.MajorDTO;
import com.ams.ams_app.network.ApiClient;
import org.json.JSONObject;

public class MajorService {
    public int getTotal() throws Exception {
        String url = "/majors";
        ApiResponseDTO<?> response = ApiClient.get(url);
        int total = 0;
        if (response.isSuccess()) {

            JSONObject data = (JSONObject) response.getData();
            return data.optInt("total");
        }
        return total;
    }
    public boolean updateMajorStatus(MajorDTO major)throws Exception{
        String url = "/majors/" + major.getId() ;
        JSONObject body = new JSONObject();
        boolean status = !major.isLocked();
        body.put("isLocked", status);
        ApiResponseDTO<?> response = ApiClient.patch(url,body);
        System.out.println("SERVICE : Response message :  " +response.getMessage());
        return response.isSuccess();
    }
}
