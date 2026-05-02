package com.ams.ams_app.controller;

import com.ams.ams_app.services.AdmissionService;
import com.ams.ams_app.services.AspirationService;
import com.ams.ams_app.dto.AdmissionPlanDTO;
import com.ams.ams_app.dto.AspirationDTO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.print.PrinterJob;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.File;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class PrintController {

    private AdmissionService admissionService = new AdmissionService();
    private AspirationService aspirationService = new AspirationService();
    private ObservableList<AspirationDTO> masterList = FXCollections.observableArrayList();
    private AdmissionPlanDTO selectedPlan;
    private File selectedTemplate = null;
    private  String currentMajorId ="";
    @FXML private MenuButton planMenuButton;
    @FXML private MenuButton majorMenuButton;
    @FXML private TableView<AspirationDTO> tableView;
    @FXML private TextField searchField;
    @FXML private Button btnExportDocx,btnPrint;
    @FXML private Label lbTemplateName;
    @FXML private TableColumn<AspirationDTO, String> colSID;
    @FXML private TableColumn<AspirationDTO, String> colName;
    @FXML private TableColumn<AspirationDTO, String> colEnrollmentYear;
    @FXML private TableColumn<AspirationDTO, String> colMajor;
    @FXML private TableColumn<AspirationDTO, String> colScore;
    @FXML private TableColumn<AspirationDTO, String> colMatriculateStatus;

    @FXML
    public void initialize() {
        loadAdmissionPlans();
        colSID.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getStudent().getId()));
        colName.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getStudent().getFullName()));

        colEnrollmentYear.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getAdmissionPlan().getAcademicYear())));

        colMajor.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getAdmissionPlan().getMajor().getName()));

        colScore.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getTotalScore())));
        colMatriculateStatus.setCellValueFactory(cellData -> {
            String status = cellData.getValue().getStatus();
            return new SimpleStringProperty("ACCEPTED".equalsIgnoreCase(status) ? "Trúng tuyển" : status);
        });
        if (selectedTemplate == null) {
            btnExportDocx.setDisable(true);
            btnPrint.setDisable(true);
        }
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableView.setItems(masterList);
    }
    private void loadAdmissionPlans() {
        try {
            ArrayList<AdmissionPlanDTO> plans = admissionService.getAdmissionPlansList();
            planMenuButton.getItems().clear();

            for (AdmissionPlanDTO plan : plans) {
                if (plan.getClosed()) {
                    MenuItem item = new MenuItem(plan.getName() + " (" + plan.getAcademicYear() + ")");
                    item.setOnAction(e -> {
                        selectedPlan = plan;
                        planMenuButton.setText(plan.getName());
                        setupMajorMenu(plan);
                    });
                    planMenuButton.getItems().add(item);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void setupMajorMenu(AdmissionPlanDTO plan) {
        majorMenuButton.getItems().clear();
        majorMenuButton.setDisable(false);
        MenuItem majorItem = new MenuItem(plan.getMajor().getName());
        majorItem.setOnAction(e -> {
            majorMenuButton.setText(plan.getMajor().getName());
            loadData(plan.getMajor().getId(),""); 
        });
        majorMenuButton.getItems().add(majorItem);
    }
    private void loadData(String majorId, String searchTerm) {
        try {
            currentMajorId = majorId;
            ArrayList<AspirationDTO> acceptedAspirations = aspirationService.getAspirationListByMajor(
                    0, 1000, currentMajorId, "ACCEPTED", searchTerm
            );
            masterList.setAll(acceptedAspirations);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Lỗi", "Không thể tải danh sách: " + e.getMessage());
        }
    }

    @FXML
    private void handleSearch() {
        if (currentMajorId == null || currentMajorId.isEmpty()) {
            showAlert("Cảnh báo", "Vui lòng chọn ngành trước khi tìm kiếm!");
            return;
        }
        String keyword = searchField.getText().trim();
        loadData(currentMajorId, keyword);
    }

    @FXML
    private void handleReset() {
        masterList.clear();
        planMenuButton.setText("Chọn kế hoạch");
        majorMenuButton.setText("Chọn ngành");
        majorMenuButton.setDisable(true);
        searchField.clear();
        currentMajorId = "";

        selectedTemplate = null;
        lbTemplateName.setText("Chưa chọn mẫu");
        lbTemplateName.setStyle("-fx-text-fill: black;");
        btnExportDocx.setDisable(true);
        btnPrint.setDisable(true);
    }
    @FXML
    private void handleChooseTemplate() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn mẫu giấy báo trúng tuyển (Word)");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Word Documents", "*.docx"));

        Stage stage = (Stage) tableView.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            selectedTemplate = file;
            lbTemplateName.setText("Mẫu hiện tại: " + file.getName());
            lbTemplateName.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");

            btnExportDocx.setDisable(false);
            btnPrint.setDisable(false);

            showAlert("Thành công", "Đã chọn mẫu: " + file.getName());
        }
    }
    @FXML
    private void handleExportDocx() {
        ObservableList<AspirationDTO> selectedItems = tableView.getSelectionModel().getSelectedItems();
        if (selectedItems.isEmpty()) {
            showAlert("Thông báo", "Vui lòng chọn ít nhất một thí sinh!");
            return;
        }
        if (selectedTemplate == null) {
            showAlert("Thông báo", "Vui lòng 'Chọn mẫu' trước khi xuất!");
            return;
        }

        // Choses folder
        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setTitle("Chọn thư mục lưu giấy báo");
        Stage stage = (Stage) tableView.getScene().getWindow();
        File saveDir = dirChooser.showDialog(stage);
        if (saveDir != null) {
            int successCount = 0;
            for (AspirationDTO studentInfo : selectedItems) {
                try {
                    // Read template
                    FileInputStream fis = new FileInputStream(selectedTemplate);
                    XWPFDocument document = new XWPFDocument(fis);

                    // Replace placeholder
                    replaceTextInWord(document, "[HO_TEN]", studentInfo.getStudent().getFullName());
                    replaceTextInWord(document, "[SBD]", studentInfo.getStudent().getId());
                    replaceTextInWord(document, "[NGANH]", studentInfo.getAdmissionPlan().getMajor().getName());
                    replaceTextInWord(document, "[NAM_HOC]", String.valueOf(studentInfo.getAdmissionPlan().getAcademicYear()));

                    // Save new file
                    String fileName = "GiayBaoTrungTuyen_" + studentInfo.getStudentId() + ".docx";
                    File outputFile = new File(saveDir, fileName);
                    FileOutputStream fos = new FileOutputStream(outputFile);
                    document.write(fos);
                    fos.close();
                    document.close();
                    fis.close();
                    successCount++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            showAlert("Hoàn tất", "Đã xuất thành công " + successCount + " giấy báo vào thư mục:\n" + saveDir.getAbsolutePath());
        }
    }

    // HELPER
    private void replaceTextInWord(XWPFDocument doc, String findText, String replaceText) {
        if (replaceText == null) replaceText = "";
        for (XWPFParagraph p : doc.getParagraphs()) {
            List<XWPFRun> runs = p.getRuns();
            if (runs != null) {
                for (XWPFRun r : runs) {
                    String text = r.getText(0);
                    if (text != null && text.contains(findText)) {
                        text = text.replace(findText, replaceText);
                        r.setText(text, 0);
                    }
                }
            }
        }
    }
    @FXML
    private void handlePrint() {
        ObservableList<AspirationDTO> selectedItems = tableView.getSelectionModel().getSelectedItems();

        if (selectedItems.isEmpty()) {
            showAlert("Thông báo", "Vui lòng chọn ít nhất một thí sinh để in!");
            return;
        }

        PrinterJob printerJob = PrinterJob.createPrinterJob();
        if (printerJob != null && printerJob.showPrintDialog(tableView.getScene().getWindow())) {
            int successCount = 0;

            for (AspirationDTO studentInfo : selectedItems) {

                VBox printNode = createPrintableNode(studentInfo);
                boolean printed = printerJob.printPage(printNode);
                if (printed) {
                    successCount++;
                }
            }
            printerJob.endJob();
            showAlert("Hoàn tất", "Đã gửi " + successCount + " lệnh in tới máy in.");
        }
    }
    private VBox createPrintableNode(AspirationDTO studentInfo) {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 50; -fx-background-color: white;");
        Text title = new Text("GIẤY BÁO TRÚNG TUYỂN NĂM " + studentInfo.getAdmissionPlan().getAcademicYear());
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        Text t1 = new Text("Họ và tên: " + studentInfo.getStudent().getFullName());
        Text t2 = new Text("SBD: " + studentInfo.getStudent().getId());
        Text t3 = new Text("Ngành: " + studentInfo.getAdmissionPlan().getMajor().getName());
        Text t4 = new Text("Tổng điểm: " + studentInfo.getTotalScore());

        t1.setStyle("-fx-font-size: 14px;");
        t2.setStyle("-fx-font-size: 14px;");
        t3.setStyle("-fx-font-size: 14px;");
        t4.setStyle("-fx-font-size: 14px;");

        layout.getChildren().addAll(title, new Text("\n"), t1, t2, t3, t4);
        return layout;
    }
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}