package com.ams.ams_app.controller;

import com.ams.ams_app.services.AspirationService;
import com.ams.ams_app.services.MajorService;
import com.ams.ams_app.services.StudentService;
import com.ams.ams_app.services.AdmissionService;
import com.ams.ams_app.dto.AdmissionPlanDetailDTO;
import com.ams.ams_app.dto.AspirationDTO;
import com.ams.ams_app.dto.StudentDTO;
import com.ams.ams_app.dto.StudentsReviewDTO;
import com.ams.ams_app.session.UserSession;
import com.ams.ams_app.util.SceneManager;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AdmissionController {
    StudentService studentService = new StudentService();
    AdmissionService admissionService = new AdmissionService();
    AspirationService aspirationService = new AspirationService();
    MajorService majorService = new MajorService();
    AdmissionPlanDetailDTO currentPlanDetail;

    @FXML private Label txtMajorName, txtQuota, txtFinalScore;
    @FXML private Button btnMajorStatus;
    @FXML private TableView<AspirationDTO> tableView;

    @FXML private TableColumn<AspirationDTO, String> colId;
    @FXML private TableColumn<AspirationDTO, String> colSID;
    @FXML private TableColumn<AspirationDTO, String> colFullName;
    @FXML private TableColumn<AspirationDTO, String> colCombination;
    @FXML private TableColumn<AspirationDTO, String> colPriorityOrder;
    @FXML private TableColumn<AspirationDTO, String> colAcademicYear;
    @FXML private TableColumn<AspirationDTO, String> colTotalScore;
    @FXML private TableColumn<AspirationDTO, String> colMatriculateStatus;

    @FXML private ProgressBar progressBar;
    @FXML private Button btnFinalize;
    @FXML private TextArea logPane;
    @FXML private Pagination pagination;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colSID.setCellValueFactory(cellData -> {
            StudentDTO s = cellData.getValue().getStudent();
            return new SimpleStringProperty(s != null ? s.getId() : "");
        });

        colFullName.setCellValueFactory(cellData -> {
            StudentDTO s = cellData.getValue().getStudent();
            return new SimpleStringProperty(s != null ? s.getFullName() : "");
        });

        colCombination.setCellValueFactory(cellData -> {
            if (cellData.getValue().getCombination() != null) {
                return new SimpleStringProperty(cellData.getValue().getCombination().getCode());
            }
            return new SimpleStringProperty("");
        });

        colPriorityOrder.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getPriorityOrder()))
        );

        colAcademicYear.setCellValueFactory(cellData -> {
            if (cellData.getValue().getAdmissionPlan() != null) {
                return new SimpleStringProperty(String.valueOf(cellData.getValue().getAdmissionPlan().getAcademicYear()));
            }
            return new SimpleStringProperty("");
        });

        colTotalScore.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getTotalScore()))
        );

        colMatriculateStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        setupTableStyles();
        pagination.setPageFactory(pageIndex -> {
            loadStudentsByMajor(pageIndex);
            return new VBox();
        });
    }

    public void setData(AdmissionPlanDetailDTO plan) {
        this.currentPlanDetail = plan;
        txtMajorName.setText(currentPlanDetail.getPlanInfor().getMajor().getName());
        txtQuota.setText(String.valueOf(currentPlanDetail.getPlanInfor().getQuota()));
        txtFinalScore.setText(String.valueOf(currentPlanDetail.getPlanInfor().getAvgFinalScore()));
        setMajorStatusUI();
        try {
            int totalPages = studentService.getTotalPagesByMajor(100,currentPlanDetail.getPlanInfor().getMajor().getId());
            pagination.setPageCount(totalPages > 0 ? totalPages : 1);
            loadStudentsByMajor(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadStudentsByMajor(int page) {
        if (currentPlanDetail == null) return;
        String majorId = currentPlanDetail.getPlanInfor().getMajor().getId();
        new Thread(() -> {
            try {
                List<AspirationDTO> aspirations = aspirationService.getAspirationListByMajor(page, 100, majorId,"","");
                Platform.runLater(() -> {
                    tableView.setItems(FXCollections.observableArrayList(aspirations));
                    logPane.appendText("Đã tải danh sách trang " + (page + 1) + "\n");
                });
            } catch (Exception e) {
                Platform.runLater(() -> logPane.appendText("Lỗi tải dữ liệu: " + e.getMessage() + "\n"));
            }
        }).start();
    }

    @FXML
    public void handleRun(ActionEvent event) {
        if (currentPlanDetail == null || tableView.getItems().isEmpty()) return;
        UserSession.getInstance().setProcessingAdmission(true);
        btnFinalize.setDisable(true);
        if(!currentPlanDetail.getPlanInfor().getMajor().isLocked()){
            logPane.appendText("Bắt đầu tiến trình xét tuyển...\n");
            progressBar.setProgress(0.1);
            String planId = currentPlanDetail.getPlanInfor().getId();
            ArrayList<String> aspirationIds = tableView.getItems().stream()
                    .map(AspirationDTO::getStudentId)
                    .collect(Collectors.toCollection(ArrayList::new));

            new Thread(() -> {
                try {
                    Platform.runLater(() -> {
                        logPane.appendText("Đang đồng bộ cấu hình máy chủ...\n");
                        progressBar.setProgress(0.3);
                    });
                    Thread.sleep(2000);
                    progressBar.setProgress(0.5);
                    Thread.sleep(1000);
                    progressBar.setProgress(0.7);
                    StudentsReviewDTO result = admissionService.runProcess(planId, aspirationIds);

                    Platform.runLater(() -> {
                        if (result != null) {
                            progressBar.setProgress(1.0);
                            logPane.appendText("--- Kết quả xét tuyển ---\n");
                            logPane.appendText("Trúng tuyển: " + result.getSummary().getTotalStudentsPassed() + "\n");
                            logPane.appendText("Ghi chú: " + result.getSummary().getNotes() + "\n");
                            loadStudentsByMajor(pagination.getCurrentPageIndex());
                            if (progressBar.getProgress() >= 1.0) {
                                btnFinalize.setVisible(true);
                                btnFinalize.setDisable(false);
                            }
                        }
                    });
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        progressBar.setProgress(0);
                        logPane.appendText("Lỗi: " + e.getMessage() + "\n");
                    });
                }
            }).start();
        }else{
            showAlert("Lỗi","Trạng thái ngành hiện tại đã đóng!");
        }
    }

    private void setupTableStyles() {
        colMatriculateStatus.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    Label badge = new Label();
                    badge.getStyleClass().add("badge");
                    if (item.equalsIgnoreCase("accepted")) {
                        badge.setText("Trúng tuyển");
                        badge.getStyleClass().add("badge-success");
                    } else if (item.equalsIgnoreCase("rejected")) {
                        badge.setText("Trượt");
                        badge.getStyleClass().add("badge-rejected");
                    } else {
                        badge.setText("Đang chờ");
                    }
                    setGraphic(badge);
                }
            }
        });
    }

    @FXML private void handleBack() { SceneManager.setCenter("/view/pages/Setting&Sub-criteriaView.fxml", null); }
    @FXML
    public void handleDone(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận hoàn thiện");
        alert.setHeaderText("Bạn có chắc chắn muốn chốt danh sách tuyển sinh này?");
        alert.setContentText("Hành động này sẽ cập nhật trạng thái kế hoạch và không thể hoàn tác.");

        if (alert.showAndWait().get() == ButtonType.OK) {
            try {
                if (currentPlanDetail != null) {
                    boolean success = admissionService.updatePlanStatus(currentPlanDetail.getPlanInfor());
                    currentPlanDetail.getPlanInfor().setClosed(true);
                    currentPlanDetail.getPlanInfor().getMajor().setLocked(true);
                    if (success) {
                        UserSession.getInstance().setProcessingAdmission(false);
                        showAlert("Thành công", "Danh sách đã được chốt thành công!");
                        handleBack();
                    } else {
                        showAlert("Lỗi", "Không thể cập nhật trạng thái kế hoạch trên hệ thống.");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Lỗi hệ thống", "Đã xảy ra lỗi khi chốt danh sách: " + e.getMessage());
            }
        }
    }
    @FXML
    public void changeMajorStatus() {
        try {
            if (currentPlanDetail == null) return;
            boolean success = majorService.updateMajorStatus(currentPlanDetail.getPlanInfor().getMajor());
            if (success) {
                boolean currentLocked = currentPlanDetail.getPlanInfor().getMajor().isLocked();
                currentPlanDetail.getPlanInfor().getMajor().setLocked(!currentLocked);
                setMajorStatusUI();
                logPane.appendText("Đã thay đổi trạng thái ngành thành: " +
                        (currentPlanDetail.getPlanInfor().getMajor().isLocked() ? "Đã khóa" : "Đang mở") + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logPane.appendText("Lỗi khi đổi trạng thái: " + e.getMessage() + "\n");
        }
    }
    private void setMajorStatusUI() {
        if (currentPlanDetail == null || currentPlanDetail.getPlanInfor().getMajor() == null) return;

        boolean isLocked = currentPlanDetail.getPlanInfor().getMajor().isLocked();
        btnMajorStatus.getStyleClass().removeAll("btn-open", "btn-close");

        if (isLocked) {
            btnMajorStatus.setText("Đã khóa");
            btnMajorStatus.getStyleClass().add("btn-close");
        } else {
            btnMajorStatus.setText("Đang mở");
            btnMajorStatus.getStyleClass().add("btn-open");
        }
    }
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}