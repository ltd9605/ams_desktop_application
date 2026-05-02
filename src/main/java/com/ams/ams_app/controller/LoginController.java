package com.ams.ams_app.controller;

import com.ams.ams_app.services.AuthService;
import com.ams.ams_app.dto.ApiResponseDTO;
import com.ams.ams_app.session.UserSession;
import com.ams.ams_app.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class LoginController {
    @FXML
    private TextField txtUsername;
    @FXML
    private Label lbUserErr;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private TextField txtShowPassword;
    @FXML
    private Label lbPassErr;
    @FXML
    private CheckBox ShowOrHide;
    @FXML
    private Label lbLoginErr;
    @FXML
    private Button btnLogin;
    @FXML
    public void initialize() {
        txtShowPassword.textProperty()
                .bindBidirectional(txtPassword.textProperty());

        ShowOrHide.selectedProperty().addListener((obs, oldVal, newVal) -> {

            txtShowPassword.setVisible(newVal);
            txtShowPassword.setManaged(newVal);

            txtPassword.setVisible(!newVal);
            txtPassword.setManaged(!newVal);
        });
    }

    @FXML
    private void handleLogin() {

        String username = txtUsername.getText();
        String password = txtPassword.getText();
        boolean isValid = true;
        if (username == null || username.isBlank()) {
            lbUserErr.setVisible(true);
            isValid = false;
        } else {
            lbUserErr.setVisible(false);
        }

        if (password == null || password.isBlank()) {
            lbPassErr.setVisible(true);
            isValid = false;
        } else {
            lbPassErr.setVisible(false);
        }

        if (isValid) {
            lbLoginErr.setVisible(false);
            new Thread(() -> {
                btnLogin.setDisable(true);
                ApiResponseDTO<?> res = AuthService.login(username, password);

                javafx.application.Platform.runLater(() -> {
                    btnLogin.setDisable(false);
                    if (res.isSuccess()) {
                        System.out.println("Đăng nhập thành công :"+ UserSession.getInstance().getUserInfor().getFullName());
                        SceneManager.gotoMainLayout(UserSession.getInstance());
                    } else {
                        lbLoginErr.setVisible(true);
                        lbLoginErr.setText(res.getMessage());
                    }
                });
            }).start();
        }
    }
    @FXML
    private void handleBack(){
        SceneManager.gotoWelcome();
    }

}
