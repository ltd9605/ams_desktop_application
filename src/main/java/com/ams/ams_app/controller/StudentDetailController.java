package com.ams.ams_app.controller;

import com.ams.ams_app.services.CertificateAndAcademicPrizeService;
import com.ams.ams_app.services.StudentService;
import com.ams.ams_app.dto.*;
import com.ams.ams_app.util.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class StudentDetailController {
    CertificateAndAcademicPrizeService certificateAndAcademicPrizeService = new CertificateAndAcademicPrizeService();
    StudentService services = new StudentService();

    private StudentDetailDTO currentDetail;
    @FXML private TextField registrationNumberTxt,citizenIdTxt,studentIdTxt, dobTxt,genderTxt,priorityPointsTxt;
    @FXML private Label fullNameTxt;
    @FXML private VBox scoreContainer,certificateContainer,aspirationContainer;
    @FXML
    private void handleBack() {
        SceneManager.setCenter("/view/pages/StudentView.fxml", null);
    }

    public void setStudentDetail(String studentId) throws Exception {

        scoreContainer.getChildren().clear();
        certificateContainer.getChildren().clear();
        aspirationContainer.getChildren().clear();



        currentDetail = services.getStudentDetail(studentId);
        StudentDTO student = currentDetail.getStudentInfor();


        // Set data to textfield
        registrationNumberTxt.setText(student.getRegistrationNumber());
        citizenIdTxt.setText(student.getCitizenIdCard());
        studentIdTxt.setText(student.getId());
        fullNameTxt.setText(student.getFullName());
        dobTxt.setText(student.getDob());
        genderTxt.setText(mapGender(student.getGender()));
        priorityPointsTxt.setText(student.getPriorityPoints().toString());
        ArrayList<ScoresDTO>  scoresList = currentDetail.getScores();
        if(scoresList != null){
            for (ScoresDTO scoresDTO : scoresList) {
                SubjectDTO subject = scoresDTO.getSubjects();
                Label name = new Label(subject.getName());
                Label score = new Label(scoresDTO.getScore().toString());
                name.getStyleClass().add("subtitle-label");
                VBox item = new VBox();
                item.getChildren().addAll(name,score);
                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);
                HBox row = new HBox(item, spacer);
                row.getStyleClass().add("card-item");

                scoreContainer.getChildren().add(row);
            }
        }
        ArrayList<CertificateDTO> certificates = currentDetail.getCertificates();
        if (certificates != null) {
            for (CertificateDTO certificate : certificates) {

                Label name = new Label(
                        certificate.getGradeName() + " (" + certificate.getCertificate().getName() + ")"
                );
                name.getStyleClass().add("subtitle-label");

                Label score = new Label("Điểm: " + certificate.getConvertScore());
                VBox item = new VBox();
                item.getChildren().addAll(name,score);
                Button deleteBtn = new Button("Xoá");
                deleteBtn.getStyleClass().add("btn-danger");

                deleteBtn.setOnAction(e -> {
                    try {
                        certificateAndAcademicPrizeService.popCertification(
                                currentDetail.getStudentInfor().getId(),
                                certificate.getGradeId()
                        );
                        setStudentDetail(currentDetail.getStudentInfor().getId());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                HBox row = new HBox(item, spacer, deleteBtn);
                row.getStyleClass().add("card-item");

                certificateContainer.getChildren().add(row);
            }
        }
        ArrayList<AcademicPrizeDTO> academicPrizes = currentDetail.getAcademicPrizes();
        if (academicPrizes != null) {
            for (AcademicPrizeDTO prize : academicPrizes) {
                Label name = new Label(prize.getPrize().getName() + " (" + prize.getName() + ")");
                name.getStyleClass().add("subtitle-label");
                Label score = new Label("Điểm cộng : " + prize.getIncludedAwardedScoreBonus());
                VBox item = new VBox();
                item.getChildren().addAll(name,score);
                Button deleteBtn = new Button("Xoá");
                deleteBtn.getStyleClass().add("btn-danger");

                deleteBtn.setOnAction(e -> {
                    try {
                        certificateAndAcademicPrizeService.popAcademicPrize(
                                currentDetail.getStudentInfor().getId(),
                                prize.getId()
                        );
                        setStudentDetail(currentDetail.getStudentInfor().getId());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                HBox row = createRow( item, spacer, deleteBtn);
                row.getStyleClass().add("card-item");
                certificateContainer.getChildren().add(row);
            }
        }
        ArrayList<AspirationDTO> aspirations = currentDetail.getAspirations();
        if(aspirations != null){
            for (AspirationDTO aspiration : aspirations){
                Label major = new Label(aspiration.getAdmissionPlan().getMajor().getName());
                major.getStyleClass().add("subtitle-label");
                DecimalFormat df = new DecimalFormat("#.##");
                double totalScore = aspiration.getTotalScore();
                Label status = new Label(mapStatus(aspiration.getStatus()));
                status.getStyleClass().add("badge-info");
                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                HBox row = createRow(
                        new Label("Nguyện vọng :  " + aspiration.getPriorityOrder()),
                        new Label("Ngành :  " ),major,spacer,
                        new Label("Tổ hợp : " + aspiration.getCombination().getCode()),
                        new Label("Điểm :  " + df.format(totalScore)),
                        status
                );
                row.getStyleClass().add("card-item");
                aspirationContainer.getChildren().add(row);
            }
        }

    }
    // HELPER
    public String mapGender(String gender){
        return switch (gender) {
            case "male" -> "Nam";
            case "female" -> "Nữ";
            default -> gender;
        };
    }
    public String mapStatus(String status){
        return switch (status) {
            case "accepted" -> "Trúng tuyển";
            case "pending" -> "Đang chờ";
            case "rejected" -> "Đã trượt";
            default -> status;
        };
    }
    private HBox createRow(Node... nodes) {
        HBox row = new HBox(15);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getChildren().addAll(nodes);
        row.getStyleClass().add("item-row");
        return row;
    }
    @FXML
    public void openPopup() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/component/CertificateAndAcademicPrizeManager.fxml"));
            Parent root = loader.load();

            CertificateManagerController popupController = loader.getController();

            ArrayList<CertificateDTO> allAvailableCerts = certificateAndAcademicPrizeService.getCertificateList();
            ArrayList<AcademicPrizeDTO> allAvailablePrizes = certificateAndAcademicPrizeService.getAcademicPrizeList();

            popupController.setData(currentDetail, allAvailableCerts, allAvailablePrizes);
            popupController.setParentController(this);

            Stage stage = new Stage();
            stage.setTitle("Quản lý chứng chỉ & giải thưởng");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}