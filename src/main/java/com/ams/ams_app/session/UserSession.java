package com.ams.ams_app.session;

import com.ams.ams_app.dto.AdmissionPlanDetailDTO;
import com.ams.ams_app.dto.UserDTO;

import java.util.ArrayList;

public class UserSession {
    private static volatile UserSession instance;
    private UserDTO userInfor;
    private String token;
    private  String refreshtoken;
    private boolean isOfflineMode = false;
    public boolean isOfflineMode() {
        return isOfflineMode;
    }
    private boolean isProcessingAdmission = false;
    private AdmissionPlanDetailDTO selectedPlan = null;

    public void setOfflineMode(boolean offlineMode) {
        isOfflineMode = offlineMode;
    }
    public UserSession(UserDTO userInfor, String token, String refreshtoken) {
        this.userInfor = userInfor;
        this.token = token;
        this.refreshtoken = refreshtoken;
    }

    // INIT SESSION
    public static void init(UserDTO user, String token, String refreshtoken) {
        instance = new UserSession(user, token, refreshtoken);
    }

    public static UserSession getInstance() {
        if (instance == null) {
            instance = SessionStorage.load();
        }
        return instance;
    }

    public static void setInstance(UserSession instance) {
        UserSession.instance = instance;
    }

    public static void clean() {
        instance = null;
    }
    public static void initOffline() {
        UserDTO guest = new UserDTO("guest", "offline_user", "Chế độ Ngoại tuyến", false, null, null, null, null);
        instance = new UserSession(guest, "offline_token", "offline_refresh");
        instance.setOfflineMode(true);
    }

    public UserDTO getUserInfor() {
        return userInfor;
    }

    public void setUserInfor(UserDTO userInfor) {
        this.userInfor = userInfor;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshtoken() {
        return refreshtoken;
    }

    public void setRefreshtoken(String refreshtoken) {
        this.refreshtoken = refreshtoken;
    }
    public boolean isProcessingAdmission() { return isProcessingAdmission; }
    public void setProcessingAdmission(boolean processing) { this.isProcessingAdmission = processing; }

    public AdmissionPlanDetailDTO getSelectedPlan() {
        return selectedPlan;
    }

    public void setSelectedPlan(AdmissionPlanDetailDTO selectedPlan) {
        this.selectedPlan = selectedPlan;
    }
}