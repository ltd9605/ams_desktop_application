package com.ams.ams_app.controller;

import com.ams.ams_app.dto.AdmittedAspiration;
import com.ams.ams_app.dto.ApiResponseDTO;
import com.ams.ams_app.dto.StatusCheckDTO;
import com.ams.ams_app.services.AuthService;
import com.ams.ams_app.services.PublicService;
import com.ams.ams_app.session.SessionStorage;
import com.ams.ams_app.session.UserSession;
import com.ams.ams_app.util.NetworkUtil;
import com.ams.ams_app.util.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;


public class WelcomeController {
    private AuthService authService;
    private PublicService publicService = new PublicService();
    @FXML
    private Button login_btn;
    @FXML private Label lblInstructionTitle;
    @FXML private VBox vboxInstructionContent;
    @FXML private TextField txtRegistrationNumber, txtCitizenId;
    @FXML
    public void handleLogin(ActionEvent event) {
        if (NetworkUtil.isOnline()) {
            UserSession savedSession = SessionStorage.load(); 
            if (savedSession != null && savedSession.getToken() != null) {
                if (AuthService.isTokenExpired(savedSession.getToken())) {
                    if (AuthService.refreshToken(savedSession)) {
                        UserSession.setInstance(savedSession);
                        SceneManager.gotoMainLayout(savedSession);
                    } else {
                        UserSession.clean();
                        SceneManager.gotoLogin();
                    }
                } else {
                    UserSession.setInstance(savedSession);
                    SceneManager.gotoMainLayout(savedSession);
                }
            } else {
                SceneManager.gotoLogin();
            }
        } else {
            // Offline mode
            UserSession.initOffline();
            SceneManager.gotoMainLayout(UserSession.getInstance());
        }
    }
    @FXML
    private void handleSearch(ActionEvent event) {
        String regNum = txtRegistrationNumber.getText().trim();
        String citizenId = txtCitizenId.getText().trim();

        // Validation
        if (regNum.isEmpty() || citizenId.isEmpty()) {
            displayNotice("Thông báo", "Vui lòng nhập đầy đủ thông tin để tra cứu.", "red");
            return;
        }

        try {
            ApiResponseDTO<StatusCheckDTO> response = publicService.statusCheck(regNum, citizenId);

            if (response.isSuccess() && response.getData() != null) {
                StatusCheckDTO data = response.getData();
                processStatusResult(data);
            } else {
                // Not found
                displayNotice("Không tìm thấy dữ liệu",
                        "Hệ thống không tìm thấy thông tin thí sinh khớp với dữ liệu bạn cung cấp. Vui lòng kiểm tra lại.",
                        "orange");
            }
        } catch (Exception e) {
            displayNotice("Lỗi kết nối", "Không thể kết nối tới máy chủ lúc này.", "red");
        }
    }
    private void processStatusResult(StatusCheckDTO data) {
        vboxInstructionContent.getChildren().clear();
        vboxInstructionContent.setSpacing(12.0);

        String status = data.getMatriculateStatus();

        if ("accepted".equalsIgnoreCase(status)) {
            lblInstructionTitle.setText("🎉 CHÚC MỪNG TRÚNG TUYỂN!");
            lblInstructionTitle.setStyle("-fx-text-fill: #27ae60; -fx-font-size: 22px; -fx-font-weight: bold;");

            AdmittedAspiration asp = data.getAdmittedAspiration();

            Label lblName = new Label("Chúc mừng thí sinh: " + data.getFullName().toUpperCase());
            lblName.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

            Label lblMajor = new Label(String.format("• Ngành trúng tuyển: %s (%s)",
                    asp.getMajorName(), asp.getMajorCode()));

            Label lblCombination = new Label(String.format("• Tổ hợp xét tuyển: %s | Thử tự NV: %d",
                    asp.getCombinationCode(), asp.getPriorityOrder()));

            Label lblNote = new Label(data.getNotes());
            lblNote.setWrapText(true);
            lblNote.setStyle("-fx-font-style: italic; -fx-text-fill: #2c3e50; -fx-padding: 10 0 0 0;");
            vboxInstructionContent.getChildren().addAll(lblName, lblMajor, lblCombination, lblNote);

        } else if ("rejected".equalsIgnoreCase(status)) {
            displayNotice("THÔNG BÁO KẾT QUẢ",
                    "Rất tiếc thí sinh " + data.getFullName() + ", bạn không trúng tuyển trong đợt này.",
                    "#c0392b");
        } else {
            displayNotice("THÔNG BÁO",
                    "Hệ thống không tìm thấy kết quả khớp với thông tin bạn cung cấp.",
                    "#f39c12");
        }
    }
    private void displayNotice(String title, String content, String color) {
        vboxInstructionContent.getChildren().clear();

        lblInstructionTitle.setText(title);
        lblInstructionTitle.setStyle("-fx-text-fill: " + color + "; -fx-font-size: 20px;");

        Label lblContent = new Label(content);
        lblContent.setWrapText(true);
        lblContent.setStyle("-fx-font-size: 14px; -fx-text-fill: #555555;");

        vboxInstructionContent.getChildren().add(lblContent);
    }
}
