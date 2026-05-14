package com.ams.ams_app.controller;

import com.ams.ams_app.services.ImportService;
import com.ams.ams_app.dto.ApiResponseDTO;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;

public class ImportController {

    private final ImportService importService = new ImportService();
    private File selectedExcelFile = null;

    @FXML private ComboBox<String> cbDataType;
    @FXML private Label lbFileName;
    @FXML private Button btnImport;
    @FXML private ProgressBar progressBar;

    @FXML
    public void initialize() {
        cbDataType.getItems().addAll(
                "Hồ sơ Thí sinh",
                "Nguyện vọng",
                "Điểm giải thưởng",
                "Giải thưởng thí sinh"
        );
        cbDataType.getSelectionModel().selectFirst();
        progressBar.setVisible(false);
    }

    @FXML
    private void handleChooseFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn file");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Excel Files", "*.xlsx", "*.xls", "*.csv")
        );

        Stage stage = (Stage) btnImport.getScene().getWindow();
        selectedExcelFile = fileChooser.showOpenDialog(stage);

        if (selectedExcelFile != null) {
            lbFileName.setText(selectedExcelFile.getName());
        }
    }

    @FXML
    private void handleImport() {
        if (selectedExcelFile == null) {
            showAlert("Lỗi", "Vui lòng chọn file trước khi Import!", Alert.AlertType.WARNING);
            return;
        }

        String dataType = cbDataType.getValue();
        btnImport.setDisable(true);
        progressBar.setVisible(true);

        new Thread(() -> {
            try {
                ApiResponseDTO<?> response = importService.uploadExcelFile(selectedExcelFile, dataType);

                Platform.runLater(() -> {
                    progressBar.setVisible(false);
                    btnImport.setDisable(false);

                    if (response.getStatusCode() == 200) {
                        showAlert("Thành công", "Đã Import thành công toàn bộ dữ liệu!", Alert.AlertType.INFORMATION);
                        resetForm();
                    }
                    // status code :  207 if some lines are faulty
                    else if (response.getStatusCode() == 207) {
                        showPartialSuccessAlert((JSONObject) response.getData());
                    }
                    else {
                        showAlert("Thất bại", response.getMessage(), Alert.AlertType.ERROR);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> {
                    progressBar.setVisible(false);
                    btnImport.setDisable(false);
                    showAlert("Lỗi kết nối", "Không thể gửi file lên server.", Alert.AlertType.ERROR);
                });
            }
        }).start();
    }

    private void showPartialSuccessAlert(JSONObject data) {
        int total = data.optInt("totalRows", 0);
        int success = data.optInt("successRows", 0);
        int error = data.optInt("errorRows", 0);
        String msg = String.format("Import hoàn tất với cảnh báo.\n- Thành công: %d dòng\n- Thất bại: %d dòng", success, error);
        showAlert("Cảnh báo Import", msg, Alert.AlertType.WARNING);
    }

    private void resetForm() {
        selectedExcelFile = null;
        lbFileName.setText("Chưa chọn file");
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}