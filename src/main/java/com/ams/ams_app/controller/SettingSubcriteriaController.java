package com.ams.ams_app.controller;

import com.ams.ams_app.services.AdmissionService;
import com.ams.ams_app.services.MajorService;
import com.ams.ams_app.dto.AdmissionPlanDTO;
import com.ams.ams_app.dto.AdmissionPlanDetailDTO;
import com.ams.ams_app.dto.CombinationDTO;
import com.ams.ams_app.dto.SubCriteriaDTO;
import com.ams.ams_app.session.UserSession;
import com.ams.ams_app.util.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javax.print.DocFlavor;
import java.util.ArrayList;

public class SettingSubcriteriaController {
    private AdmissionPlanDetailDTO currentPlanDetail;
    private AdmissionService admissionService = new AdmissionService();
    private MajorService majorService = new MajorService();
    @FXML private MenuButton planMenuButton;
    @FXML private TextField txtPlanName, txtYear, txtQuota, txtMinScore, txtMajorName,txtFinalScore;
    @FXML private ListView<SubCriteriaDTO> lvSubCriteria;
    @FXML private Button btnSave,btnEdit,btnStatus;
    @FXML private VBox combinationContainer;

    @FXML
    public void initialize() {
        loadAdmissionPlans();
        setupDraggableListView();
        AdmissionPlanDetailDTO savedPlan = UserSession.getInstance().getSelectedPlan();
        if (savedPlan != null) {
            try {
                displayPlanDetails(savedPlan.getPlanInfor().getId());
                planMenuButton.setText(savedPlan.getPlanInfor().getName());
            } catch (Exception e) { e.printStackTrace(); }
        }
    }
    private void loadAdmissionPlans() {
        try {
            ArrayList<AdmissionPlanDTO> plans = admissionService.getAdmissionPlansList();
            planMenuButton.getItems().clear();

            for (AdmissionPlanDTO plan : plans) {
                MenuItem item = new MenuItem(plan.getName());
                item.setOnAction(event -> {
                    planMenuButton.setText(plan.getName());
                    try {
                        displayPlanDetails(plan.getId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                planMenuButton.getItems().add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void displayPlanDetails(String planId) throws Exception {
        currentPlanDetail = admissionService.getDetail(planId);
        UserSession.getInstance().setSelectedPlan(currentPlanDetail);
        txtPlanName.setText( currentPlanDetail.getPlanInfor().getName());
        txtYear.setText(String.valueOf(currentPlanDetail.getPlanInfor().getAcademicYear()));
        txtQuota.setText(String.valueOf(currentPlanDetail.getPlanInfor().getQuota()));
        txtMinScore.setText(String.valueOf(currentPlanDetail.getPlanInfor().getAvgMinScore()));
        txtMajorName.setText(currentPlanDetail.getPlanInfor().getMajor().getName());
        txtFinalScore.setText(String.valueOf(currentPlanDetail.getPlanInfor().getAvgFinalScore()));
        combinationContainer.getChildren().clear();
        for (CombinationDTO com : currentPlanDetail.getCombinations()){
            Label code = new Label(com.getCode());
            combinationContainer.getChildren().add(code);
        }
        lvSubCriteria.getItems().clear();
        setStatusUI();
    }
    // Custom drag&drop container
    private void setupDraggableListView() {
        lvSubCriteria.setCellFactory(lv -> {
            ListCell<SubCriteriaDTO> cell = new ListCell<>() {
                @Override
                protected void updateItem(SubCriteriaDTO item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        setText(item.getPriorityLevel() + ". " + item.getName());
                    }
                }
            };

            cell.setOnDragDetected(event -> {
                if (cell.getItem() == null) return;
                Dragboard db = cell.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putString(String.valueOf(cell.getIndex()));
                db.setContent(content);
                event.consume();
            });

            cell.setOnDragOver(event -> {
                if (event.getGestureSource() != cell && event.getDragboard().hasString()) {
                    event.acceptTransferModes(TransferMode.MOVE);
                }
                event.consume();
            });

            cell.setOnDragDropped(event -> {
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasString()) {
                    int draggedIndex = Integer.parseInt(db.getString());
                    int droppedIndex = cell.getIndex();
                    SubCriteriaDTO draggedItem = lvSubCriteria.getItems().remove(draggedIndex);
                    lvSubCriteria.getItems().add(droppedIndex, draggedItem);
                    updateAndSavePriorityOrder();
                    success = true;
                }
                event.setDropCompleted(success);
                event.consume();
            });

            return cell;
        });
    }
    // Handle update sub-criteria
    private void updateAndSavePriorityOrder() {
        try {
            for (int i = 0; i < lvSubCriteria.getItems().size(); i++) {
                SubCriteriaDTO item = lvSubCriteria.getItems().get(i);
                int newPriority = i + 1;
                if (item.getPriorityLevel() != newPriority) {
                    item.setPriorityLevel(newPriority);
                    admissionService.updateSubcriteriaLevel(item.getId(), item);
                }
            }
            lvSubCriteria.refresh();
            System.out.println("Đã cập nhật thứ tự ưu tiên mới thành công.");
        } catch (Exception e) {
            System.err.println("Lỗi khi cập nhật thứ tự ưu tiên: " + e.getMessage());
            e.printStackTrace();
        }
    }
    @FXML
    public void handleEdit() {
        if(btnStatus.getText() == "Đang mở"){
            if (btnEdit.getText().equals("Sửa")) {
                btnEdit.setText("Huỷ");
                txtQuota.setEditable(true);
                txtPlanName.setEditable(true);
                txtQuota.requestFocus();
            } else {
                btnEdit.setText("Sửa");
                txtQuota.setEditable(false);
                txtPlanName.setEditable(false);
                if (currentPlanDetail != null) {
                    txtQuota.setText(String.valueOf(currentPlanDetail.getPlanInfor().getQuota()));
                    txtMinScore.setText(String.valueOf(currentPlanDetail.getPlanInfor().getAvgMinScore()));
                }
            }
        }else{
            showAlert("Lỗi", "Kế hoạch đã đóng , vui lòng mở khoá kế hoạch trước khi sửa !");
        }
    }
    @FXML
    public void handleSave() {
        try {
            // Validation 
            String strQuota = txtQuota.getText().trim();
            String strMinScore = txtMinScore.getText().trim();

            if (strQuota.isEmpty() || strMinScore.isEmpty()) {
                showAlert("Lỗi", "Vui lòng nhập đầy đủ Chỉ tiêu và Điểm sàn.");
                return;
            }
            int newQuota;
            double newMinScore;
            try {
                newQuota = Integer.parseInt(strQuota);
                newMinScore = Double.parseDouble(strMinScore);

                if (newQuota <= 0 || newMinScore < 0) {
                    showAlert("Lỗi", "Số liệu nhập vào phải là số dương.");
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert("Lỗi", "Chỉ tiêu và Điểm sàn phải là định dạng số.");
                return;
            }
            boolean isQuotaChanged = newQuota != currentPlanDetail.getPlanInfor().getQuota();
            boolean isScoreChanged = newMinScore != currentPlanDetail.getPlanInfor().getAvgMinScore();

            if (isQuotaChanged || isScoreChanged) {
                currentPlanDetail.getPlanInfor().setQuota(newQuota);
                currentPlanDetail.getPlanInfor().setAvgMinScore(newMinScore);
                boolean success = admissionService.updatePlanInfo(currentPlanDetail.getPlanInfor());

                if (success) {
                    showAlert("Thông báo", "Cập nhật kế hoạch thành công!");
                    btnEdit.setText("Sửa");
                    txtQuota.setEditable(false);
                    txtMinScore.setEditable(false);
                } else {
                    showAlert("Lỗi", "Cập nhật thất bại trên hệ thống.");
                }
            } else {
                showAlert("Thông báo", "Không có thay đổi nào được thực hiện.");
                btnEdit.setText("Sửa");
                txtQuota.setEditable(false);
                txtMinScore.setEditable(false);
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Lỗi hệ thống", "Đã xảy ra lỗi: " + e.getMessage());
        }
    }
    // HELPER
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    public void gotoAdmission() {
        try {
            if (currentPlanDetail != null) {
               if( !currentPlanDetail.getPlanInfor().getClosed()){
                   SceneManager.setCenter("/view/component/AdmissionView.fxml", controller -> {
                       AdmissionController c = (AdmissionController) controller;
                       c.setData(currentPlanDetail);
                   });
            }else {
                showAlert("Lỗi" ,"Kế hoạch hiện tại đã đóng !");
                planMenuButton.requestFocus();
            }
        }
        } catch (Exception e) {
            showAlert("Lỗi" ,"Vui lòng chọn một kế hoạch tuyển sinh !");
            planMenuButton.requestFocus();
        }
    }
    @FXML
    public void changeStatus() {
        try {
            if (currentPlanDetail == null) {
                showAlert("Lỗi", "Vui lòng chọn kế hoạch trước!");
                return;
            }

            boolean success = admissionService.updatePlanStatus(currentPlanDetail.getPlanInfor());

            if (success) {
                boolean currentStatus = currentPlanDetail.getPlanInfor().getClosed();
                currentPlanDetail.getPlanInfor().setClosed(!currentStatus);
                setStatusUI();
                System.out.println("Cập nhật trạng thái thành công: " + !currentStatus);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Lỗi", "Không thể cập nhật trạng thái: " + e.getMessage());
        }
    }

    private void setStatusUI() {
        if (currentPlanDetail == null) return;

        boolean isClosed = currentPlanDetail.getPlanInfor().getClosed();

        btnStatus.getStyleClass().removeAll("btn-open", "btn-close");

        if (!isClosed) {
            btnStatus.setText("Đang mở");
            btnStatus.getStyleClass().add("btn-open"); 
        } else {
            btnStatus.setText("Đã đóng");
            btnStatus.getStyleClass().add("btn-close"); 
        }
    }
}