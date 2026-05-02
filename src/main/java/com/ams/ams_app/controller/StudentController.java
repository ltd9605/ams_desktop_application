package com.ams.ams_app.controller;

import com.ams.ams_app.services.StudentService;
import com.ams.ams_app.dto.StudentDTO;
import com.ams.ams_app.dto.StudentDetailDTO;
import com.ams.ams_app.util.SceneManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class StudentController {
    private final StudentService services = new StudentService();
    private final int limit = 10;

    @FXML private TableView<StudentDTO> tableView;
    @FXML private TableColumn<StudentDTO, String> colId,colCitizenIdCard, colName, colGender, colDob,
            colEnrollmentYear, colRegistrationNumber,
            colMatriculateStatus, colAction;
    @FXML private Pagination pagination;
    @FXML private MenuButton filterByStatusBtn;
    @FXML private TextField searchField;

    @FXML
    public void initialize() {
        // Setup TableView
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCitizenIdCard.setCellValueFactory(new PropertyValueFactory<>("citizenIdCard"));
        colName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colDob.setCellValueFactory(new PropertyValueFactory<>("dob"));
        colEnrollmentYear.setCellValueFactory(new PropertyValueFactory<>("enrollmentYear"));
        colRegistrationNumber.setCellValueFactory(new PropertyValueFactory<>("registrationNumber"));
        colMatriculateStatus.setCellValueFactory(new PropertyValueFactory<>("matriculateStatus"));

        setupCustomCells();

        // Init Pagination
        try {
            int totalPages = services.getTotalPages();
            pagination.setPageCount(totalPages > 0 ? totalPages : 1);
            pagination.setPageFactory(pageIndex -> {
                loadDataFromServer(pageIndex);
                return new VBox();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        searchField.setOnAction(event -> handleSearch());
    }

    private void loadDataFromServer(int page) {
        String searchTerm = searchField.getText().trim();
        String status = filterByStatusBtn.getText();
        new Thread(() -> {
            try {
                ArrayList<StudentDTO> students = services.getList(page, limit, searchTerm, status);
                Platform.runLater(() -> tableView.setItems(FXCollections.observableArrayList(students)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    @FXML
    private void handleSearch() {
        pagination.setCurrentPageIndex(0);
        loadDataFromServer(0);
    }

    @FXML
    private void handleFilter(javafx.event.ActionEvent event) {
        MenuItem item = (MenuItem) event.getSource();
        filterByStatusBtn.setText(item.getText());
        pagination.setCurrentPageIndex(0); 
        loadDataFromServer(0);
    }

    private void setupCustomCells() {
        // Gender Column
        colGender.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setGraphic(null);
                else {
                    String genderText = item.equalsIgnoreCase("male") ? "Nam" :
                            (item.equalsIgnoreCase("female") ? "Nữ" : item);
                    setGraphic(new Label(genderText));
                }
            }
        });

        // Status Column
        colMatriculateStatus.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setGraphic(null);
                else {
                    Label badge = new Label();
                    badge.getStyleClass().add("badge");
                    switch (item.toLowerCase()) {
                        case "accepted" -> { badge.setText("Trúng tuyển"); badge.getStyleClass().add("badge-success"); }
                        case "pending" -> { badge.setText("Đang chờ"); badge.getStyleClass().add("badge-pending"); }
                        case "rejected" -> { badge.setText("Đã trượt"); badge.getStyleClass().add("badge-rejected"); }
                        default -> { badge.setText(item); badge.getStyleClass().add("badge-info"); }
                    }
                    setGraphic(badge);
                }
            }
        });

        // Action Column (View Detail)
        colAction.setCellFactory(column -> new TableCell<>() {
            private final Button viewBtn = new Button();
            private final HBox container = new HBox(viewBtn);
            {
                ImageView viewIcon = new ImageView(new Image(getClass().getResourceAsStream("/image/viewdetail.png")));
                viewIcon.setFitWidth(16); viewIcon.setFitHeight(16);
                viewBtn.setGraphic(viewIcon);
                viewBtn.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
                container.setAlignment(Pos.CENTER);
                viewBtn.setOnAction(event -> {
                    StudentDTO student = getTableView().getItems().get(getIndex());
                    openViewPopup(student);
                });
            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : container);
            }
        });
    }
    private void openViewPopup(StudentDTO student) {
        try {
            student.getId();
            if (student.getId() != null) {
                SceneManager.setCenter("/view/component/StudentDetailView.fxml", (controller) -> {
                    StudentDetailController detailController = (StudentDetailController) controller;
                    try {
                        detailController.setStudentDetail(student.getId());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}