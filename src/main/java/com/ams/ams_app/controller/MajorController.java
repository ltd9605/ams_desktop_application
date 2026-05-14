package com.ams.ams_app.controller;

import com.ams.ams_app.dto.AdmissionPlanDTO;
import com.ams.ams_app.dto.AdmissionPlanDetailDTO;
import com.ams.ams_app.dto.CombinationDTO;
import com.ams.ams_app.services.AdmissionService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import java.util.ArrayList;

public class MajorController {
    @FXML private GridPane majorGrid;
    @FXML private TextField searchField;
    @FXML private TextField yearField;

    private final AdmissionService admissionService = new AdmissionService();

    @FXML
    public void initialize() {
        loadMajorData(null, null);
    }

    @FXML
    private void handleSearch() {
        String search = searchField.getText();
        String year = yearField.getText();
        loadMajorData(search, year);
    }

    private void loadMajorData(String search, String year) {
        majorGrid.getChildren().clear();
        Thread loadDataThread = new Thread(() -> {
            try {
                ArrayList<AdmissionPlanDTO> plans = admissionService.getAdmissionPlansList(search, year);

                for (AdmissionPlanDTO plan : plans) {
                    AdmissionPlanDetailDTO detail = admissionService.getDetail(plan.getId());
                    VBox card = createMajorCard(plan, detail.getCombinations());
                    Platform.runLater(() -> {
                        int row = majorGrid.getChildren().size() / 2;
                        int col = majorGrid.getChildren().size() % 2;
                        majorGrid.add(card, col, row);
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> {
                    System.err.println("Lỗi tải dữ liệu ngành đào tạo");
                });
            }
        });

        loadDataThread.setDaemon(true);
        loadDataThread.start();
    }

    private VBox createMajorCard(AdmissionPlanDTO plan, ArrayList<CombinationDTO> combinations) {
        VBox card = new VBox(10);
        card.getStyleClass().add("childrenPane");
        card.setPrefWidth(400);

        Label nameLabel = new Label(plan.getName());
        nameLabel.getStyleClass().add("title-label");
        nameLabel.setWrapText(true);

        Label codeLabel = new Label("Mã ngành: " + plan.getMajor().getCode());
        codeLabel.setStyle("-fx-text-fill: #666; -fx-font-style: italic;");

        VBox comboContainer = new VBox(5);
        Label titleCombo = new Label("Tổ hợp môn xét tuyển:");
        titleCombo.setStyle("-fx-font-weight: bold;");
        comboContainer.getChildren().add(titleCombo);

        if (combinations != null) {
            for (CombinationDTO combo : combinations) {
                Label lb = new Label("• " + combo.getCode() + " - " + combo.getName());
                lb.getStyleClass().add("subtitle-label");
                lb.setWrapText(true);
                comboContainer.getChildren().add(lb);
            }
        }

        card.getChildren().addAll(nameLabel, codeLabel, comboContainer);
        return card;
    }
}