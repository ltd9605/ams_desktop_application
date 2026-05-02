package com.ams.ams_app.controller;

import com.ams.ams_app.dto.UserDTO;
import com.ams.ams_app.session.UserSession;
import com.ams.ams_app.util.NetworkUtil;
import com.ams.ams_app.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.File;

public class SystemController {

    @FXML private Label lblAppName, lblVersion, lblAppSpecs, lblBaseUrl, lblStatus, lblBackendSpecs;
    @FXML private Label lblUserId, lblUserFullName, lblUsername, lblUserStatus, lblUserCreatedAt, lblRole;
    @FXML private Label lblOS, lblJava, lblLang;
    @FXML private Button btnCheckConnection;
    @FXML private ToggleButton btnThemeMode;

    @FXML
    public void initialize() {
        loadBackendInfo();
        loadAppInfo();
        loadUserInfo();
        loadEnvironmentInfo();
        updateStatusUI();
    }

    private void loadBackendInfo() {
        lblBackendSpecs.setText("• Language: Java 25\n• Framework: Spring Boot 4.0.5\n• Security: JWT, Spring Security\n• DB: PostgreSQL\n• ORM: Hibernate\n• Tool: Docker, Maven");
        String baseUrl = System.getenv("BASE_URL");
        if (baseUrl == null) baseUrl = "http://localhost:3000/api/v1";
        lblBaseUrl.setText("URL: " + baseUrl);
    }

    private void loadAppInfo() {
        lblAppName.setText("Tên ứng dụng: AMS System");
        lblVersion.setText("Phiên bản: 1.0.0");
        lblAppSpecs.setText("• UI: JavaFX 25\n• Network: HttpClient\n• Parser: Jackson/JSON\n• Auth: Cookie-based JWT");
    }

    private void loadUserInfo() {
        UserSession session = UserSession.getInstance();
        if (session != null && session.getUserInfor() != null) {
            UserDTO user = session.getUserInfor();
            lblUserId.setText("ID: " + user.getId());
            lblUserFullName.setText("Họ tên: " + user.getFullName());
            lblUsername.setText("Username: " + user.getUsername());
            lblUserStatus.setText("Trạng thái: " + (user.getLocked() ? "Đã khoá" : "Hoạt động"));
            lblUserCreatedAt.setText("Ngày tạo: " + user.getCreatedAt());
            lblRole.setText("Vai trò: " + String.join(", ", user.getRoleIds()));
        }
    }

    private void loadEnvironmentInfo() {
        lblOS.setText("OS: " + System.getProperty("os.name") + " (v" + System.getProperty("os.version") + ")");
        lblJava.setText("Runtime: Java " + System.getProperty("java.version"));
        lblLang.setText("Language: " + System.getProperty("user.language").toUpperCase());
    }

    private void updateStatusUI() {
        boolean isOffline = UserSession.getInstance().isOfflineMode();
        lblStatus.setText("Kết nối: " + (isOffline ? "Ngoại tuyến" : "Trực tuyến"));
        lblStatus.setStyle("-fx-text-fill: " + (isOffline ? "red" : "green") + ";");
        btnCheckConnection.setVisible(isOffline);
        btnCheckConnection.setManaged(isOffline);
    }

    @FXML
    private void handleCheckConnection() {
        if (NetworkUtil.isOnline()) {
            UserSession.getInstance().setOfflineMode(false);
            SceneManager.gotoWelcome();
        } else {
            updateStatusUI();
            new Alert(Alert.AlertType.ERROR, "Không thể kết nối tới máy chủ!").show();
        }
    }

    @FXML
    private void handleClearCache() {
        File cacheDir = new File("local_cache/");
        if (cacheDir.exists() && cacheDir.isDirectory()) {
            File[] files = cacheDir.listFiles();
            if (files != null) {
                for (File f : files) f.delete();
            }
            new Alert(Alert.AlertType.INFORMATION, "Đã xoá bộ nhớ đệm thành công!").show();
        }
    }

    @FXML
    private void handleToggleTheme() {
        var scene = btnThemeMode.getScene();
        if (btnThemeMode.isSelected()) {
            btnThemeMode.setText("Dark Mode: ON");
            scene.getStylesheets().add(getClass().getResource("/css/dark-theme.css").toExternalForm());
        } else {
            btnThemeMode.setText("Dark Mode: OFF");
            scene.getStylesheets().removeIf(s -> s.contains("dark-theme.css"));
        }
    }
}