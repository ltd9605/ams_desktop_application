package com.ams.ams_app.controller;

import com.ams.ams_app.services.UserService;
import com.ams.ams_app.dto.UserDTO;
import com.ams.ams_app.session.UserSession;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.json.JSONObject;

public class UserController {
    private final UserService userService = new UserService();
    private ObservableList<UserDTO> userList = FXCollections.observableArrayList();

    @FXML private TableView<UserDTO> userTable;
    @FXML private TableColumn<UserDTO, String> colId, colFullName, colUsername, colStatus, colCreatedAt, colCreatedBy, colAction;
    @FXML private Label lblUserId, lblUserFullName, lblUsername, lblUserStatus, lblUserCreatedAt, lblRole;
    @FXML
    public void initialize() {
        setupTable();
        loadUsers();
        loadCurrentUserInfo();
    }
    private void setupTable() {
        colId.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getId()));
        colFullName.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getFullName()));
        colUsername.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getUsername()));
        colStatus.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getLocked() != null && d.getValue().getLocked() ? "Đã khoá" : "Hoạt động"));
        colCreatedAt.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getCreatedAt()));
        colCreatedBy.setCellValueFactory(d -> {
            UserDTO creator = d.getValue().getCreatedBy();
            return new SimpleStringProperty(creator != null ? creator.getFullName() : "SYSTEM");
        });

        colAction.setCellFactory(param -> new TableCell<>() {
            private final Button islockedBtn = new Button();
            private final Button delBtn = new Button("Xóa");
            private final HBox container = new HBox(5, islockedBtn, delBtn);

            {
                container.setAlignment(Pos.CENTER);
                delBtn.getStyleClass().add("btn-delete");

                delBtn.setOnAction(e -> handleDelete(getTableView().getItems().get(getIndex())));
                islockedBtn.setOnAction(e -> handleToggleLock(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableView().getItems().get(getIndex()) == null) {
                    setGraphic(null);
                } else {
                    UserDTO userInRow = getTableView().getItems().get(getIndex());
                    String targetUsername = userInRow.getUsername();
                    String currentUser = UserSession.getInstance().getUserInfor().getUsername();
                    boolean isLocked = userInRow.getLocked() != null && userInRow.getLocked();
                    islockedBtn.setText(isLocked ? "Mở khóa" : "Khóa");

                    boolean isProtected = targetUsername.equalsIgnoreCase(currentUser)
                            || targetUsername.equalsIgnoreCase("Admin User")
                            || targetUsername.equalsIgnoreCase("System User");

                    if (isProtected) {
                        islockedBtn.setDisable(true);
                        delBtn.setDisable(true);
                        container.setVisible(false);
                    } else {
                        islockedBtn.setDisable(false);
                        delBtn.setDisable(false);
                        container.setVisible(true);
                    }

                    setGraphic(container);
                }
            }
        });
    }

    private void loadCurrentUserInfo() {
        UserSession session = UserSession.getInstance();
        if (session != null && session.getUserInfor() != null) {
            UserDTO user = session.getUserInfor();
            lblUserId.setText("ID: " + user.getId());
            lblUserId.getStyleClass().add("subtitle-label");
            lblUserFullName.setText("Họ tên: " + user.getFullName());
            lblUserFullName.getStyleClass().add("subtitle-label");
            lblUsername.setText("Username: " + user.getUsername());
            lblUsername.getStyleClass().add("subtitle-label");
            lblUserStatus.setText("Trạng thái: " + (user.getLocked() ? "Đã khoá" : "Hoạt động"));
            lblUserStatus.getStyleClass().add("subtitle-label");
            lblUserCreatedAt.setText("Ngày tạo: " + user.getCreatedAt());
            lblUserCreatedAt.getStyleClass().add("subtitle-label");
        }
    }
    private void loadUsers() {
        new Thread(() -> {
            try {
                var users = userService.getAllUsers();
                Platform.runLater(() -> userList.setAll(users));
                userTable.setItems(userList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
    private void handleToggleLock(UserDTO user) {
        boolean currentLockedStatus = user.getLocked() != null && user.getLocked();
        String actionName = currentLockedStatus ? "Mở khóa" : "Khóa";
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Bạn có chắc muốn " + actionName + " tài khoản " + user.getUsername() + "?",
                ButtonType.YES, ButtonType.NO);

        alert.showAndWait().ifPresent(type -> {
            if (type == ButtonType.YES) {
                new Thread(() -> {
                    try {
                        JSONObject body = new JSONObject();
                        body.put("isLocked", !currentLockedStatus);
                        if (userService.updateUser(user.getId(), body)) {
                            Platform.runLater(this::loadUsers);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Platform.runLater(() -> showAlert("Lỗi", "Không thể cập nhật trạng thái!"));
                    }
                }).start();
            }
        });
    }

    private void handleDelete(UserDTO user) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Xóa tài khoản " + user.getUsername() + "?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(type -> {
            if (type == ButtonType.YES) {
                new Thread(() -> {
                    try {
                        if (userService.deleteUser(user.getId())) {
                            Platform.runLater(this::loadUsers);
                        }
                    } catch (Exception e) { e.printStackTrace(); }
                }).start();
            }
        });
    }
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML
    private void openChangePasswordForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/component/ChangePassword.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Thay đổi mật khẩu");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}