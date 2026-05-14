package com.ams.ams_app.controller;

import com.ams.ams_app.services.AuthService;
import com.ams.ams_app.session.UserSession;
import com.ams.ams_app.util.SceneManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

public class MainLayoutController {
    @FXML
    private BorderPane mainPane;
    public void setCenter(String fxmlPath) {
        try {
            Node node = FXMLLoader.load(getClass().getResource(fxmlPath));
            mainPane.setCenter(node);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void setCenterWithLock(String fxmlPath) {
        if (UserSession.getInstance().isProcessingAdmission()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Tiến trình xét tuyển đang chạy, vui lòng không chuyển trang!");
            alert.show();
            return;
        }
        setCenter(fxmlPath);
    }
    @FXML
    private void gotoDashboard() {
        setCenterWithLock("/view/pages/DashboardView.fxml");
    }
    @FXML
    private void gotoStudents() {
        setCenterWithLock("/view/pages/StudentView.fxml");
    }
    @FXML
    private void gotoMajors(){setCenterWithLock("/view/pages/MajorView.fxml");}
    @FXML
    private void gotoAdmission() {
        setCenterWithLock("/view/pages/Setting&Sub-criteriaView.fxml");
    }
    @FXML
    private void gotoPrint() {
        setCenterWithLock("/view/pages/PrintView.fxml");
    }
    @FXML
    private void gotoSystem() {
        setCenterWithLock("/view/pages/SystemView.fxml");
    }
    @FXML
    public void gotoUser() {
        setCenterWithLock("/view/pages/UserView.fxml");
    }
    @FXML
    public void gotoFileManagement() {
        setCenterWithLock("/view/pages/ImportView.fxml");
    }
    @FXML
    private Button btnLogout;
    @FXML
    private Label lbUsername;
    @FXML private Button btnAdmission,btnUserMgmt,btnImport,btnDashboard,btnSystem;
    @FXML private Button btnStatus,btnPrint;
    @FXML
    private void initialize(){
        UserSession session = UserSession.getInstance();
        if (session != null && session.isOfflineMode()) {
            btnAdmission.setVisible(false);
            btnAdmission.setManaged(false);
            btnUserMgmt.setVisible(false);
            btnUserMgmt.setManaged(false);
            btnImport.setVisible(false);
            btnImport.setManaged(false);
            btnDashboard.setVisible(false);
            btnDashboard.setManaged(false);
            btnPrint.setVisible(false);
            btnPrint.setManaged(false);
            btnStatus.setText("Offline");
            btnStatus.getStyleClass().add("btn-offline");
            System.out.println("UI: Đã ẩn các tính năng yêu cầu quyền Admin/Online.");
        }
        else {
            btnStatus.setText("Online");
            btnStatus.getStyleClass().add("btn-online");
        }
        final ImageView logoutIcon = new ImageView(
                new Image(getClass().getResourceAsStream("/image/logout.png"))
        );
        btnLogout.setGraphic(logoutIcon);
    }
    @FXML
private void handleLogout() {

    Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
    confirmAlert.setTitle("Xác nhận đăng xuất");
    confirmAlert.setHeaderText(null);
    confirmAlert.setContentText("Bạn có chắc chắn muốn đăng xuất?");

    boolean confirmed = confirmAlert.showAndWait()
            .filter(response -> response == javafx.scene.control.ButtonType.OK)
            .isPresent();

    if (!confirmed) {
        return;
    }

    UserSession current = UserSession.getInstance();

    System.out.println("Current User : " + current.getUserInfor().toString());

    new Thread(() -> {

        if (current == null) {
            System.out.println("Session null");
        } else {

            javafx.application.Platform.runLater(() ->
                    btnLogout.setDisable(true)
            );

            boolean result = AuthService.logout(current.getInstance());

            if (result) {
                javafx.application.Platform.runLater(SceneManager::gotoLogin);
            } else {
                javafx.application.Platform.runLater(() ->
                        btnLogout.setDisable(false)
                );
            }
        }

    }).start();
}
    public void setUserName(String fullName) {
        lbUsername.setText(fullName);
    }


}
