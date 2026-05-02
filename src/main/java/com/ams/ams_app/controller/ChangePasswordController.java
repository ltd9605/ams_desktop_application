package com.ams.ams_app.controller;

import com.ams.ams_app.services.UserService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ChangePasswordController {

    private final UserService userService = new UserService();

    @FXML private PasswordField txtOldPassword;
    @FXML private TextField txtShowOldPassword;

    @FXML private PasswordField txtNewPassword;
    @FXML private TextField txtShowNewPassword;

    @FXML private CheckBox cbShowPassword;
    @FXML private Label lbError;
    @FXML private Button btnSave;

    @FXML
    public void initialize() {
        lbError.setVisible(false);
        txtShowOldPassword.textProperty().bindBidirectional(txtOldPassword.textProperty());
        txtShowNewPassword.textProperty().bindBidirectional(txtNewPassword.textProperty());

        cbShowPassword.selectedProperty().addListener((obs, oldVal, newVal) -> {
            txtShowOldPassword.setVisible(newVal);
            txtShowOldPassword.setManaged(newVal);
            txtOldPassword.setVisible(!newVal);
            txtOldPassword.setManaged(!newVal);

            txtShowNewPassword.setVisible(newVal);
            txtShowNewPassword.setManaged(newVal);
            txtNewPassword.setVisible(!newVal);
            txtNewPassword.setManaged(!newVal);
        });
        txtShowOldPassword.setVisible(false); txtShowOldPassword.setManaged(false);
        txtShowNewPassword.setVisible(false); txtShowNewPassword.setManaged(false);
    }

    @FXML
    private void handleSave(ActionEvent event) {
        String oldPass = txtOldPassword.getText();
        String newPass = txtNewPassword.getText();

        if (oldPass == null || oldPass.isBlank() || newPass == null || newPass.isBlank()) {
            showError("Vui lòng nhập đầy đủ mật khẩu cũ và mới!");
            return;
        }

        lbError.setVisible(false);
        btnSave.setDisable(true);

        new Thread(() -> {
            try {
                boolean success = userService.changeMyPassword(oldPass, newPass);
                Platform.runLater(() -> {
                    btnSave.setDisable(false);
                    if (success) {
                        showAlert("Thành công", "Đổi mật khẩu thành công!");
                        closeWindow(event);
                    } else {
                        showError("Mật khẩu cũ không chính xác hoặc lỗi hệ thống!");
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> {
                    btnSave.setDisable(false);
                    showError("Lỗi kết nối tới máy chủ!");
                });
            }
        }).start();
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        closeWindow(event);
    }

    private void showError(String message) {
        lbError.setText(message);
        lbError.setStyle("-fx-text-fill: red;");
        lbError.setVisible(true);
    }

    private void closeWindow(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}