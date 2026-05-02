package com.ams.ams_app.controller;

import com.ams.ams_app.services.CertificateAndAcademicPrizeService;
import com.ams.ams_app.dto.*;
import com.ams.ams_app.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.util.List;
import java.util.stream.Collectors;

public class CertificateManagerController {
    CertificateAndAcademicPrizeService service = new CertificateAndAcademicPrizeService();
    StudentDetailDTO currentDetail = new StudentDetailDTO();
    StudentDetailController detailController ;
    @FXML private Button btnSave;
    @FXML private ComboBox<CertificateDTO> certificateCombo;
    @FXML private ComboBox<AcademicPrizeDTO> prizeCombo;
    public void setParentController(StudentDetailController controller) {
        this.detailController = controller;
    }
    public void setData(StudentDetailDTO detail, List<CertificateDTO> allCertificates, List<AcademicPrizeDTO> allPrizes) {
        currentDetail = detail;
        List<String> existingCertIds = detail.getCertificates().stream()
                .map(c -> c.getCertificate().getId())
                .collect(Collectors.toList());
        certificateCombo.getItems().setAll(
                allCertificates.stream()
                        .filter(c -> !existingCertIds.contains(c.getGradeId()))
                        .collect(Collectors.toList())
        );
        certificateCombo.setConverter(new StringConverter<CertificateDTO>() {
            @Override
            public String toString(CertificateDTO object) {
                return (object == null) ? "" : object.getGradeName();
            }

            @Override
            public CertificateDTO fromString(String s) {
                return null;
            }
        });
        List<String> existingPrizeIds = detail.getAcademicPrizes().stream()
                .map(p -> p.getPrize().getId())
                .collect(Collectors.toList());

        prizeCombo.getItems().setAll(
                allPrizes.stream()
                        .filter(p -> !existingPrizeIds.contains(p.getId()))
                        .collect(Collectors.toList())
        );
        prizeCombo.setConverter(new StringConverter<AcademicPrizeDTO>() {
            @Override
            public String toString(AcademicPrizeDTO object) {
                return (object == null) ? "" : object.getName();
            }

            @Override
            public AcademicPrizeDTO fromString(String s) {
                return null;
            }
        });
    }
    @FXML
    private void handleSave() {
        try {
            CertificateDTO selectedCert = certificateCombo.getSelectionModel().getSelectedItem();
            AcademicPrizeDTO selectedPrize = prizeCombo.getSelectionModel().getSelectedItem();

            String studentId = currentDetail.getStudentInfor().getId();

            if (selectedCert == null && selectedPrize == null) {
                System.out.println("Vui lòng chọn ít nhất 1 mục!");
                return;
            }

            if (selectedCert != null) {
                String cerId = selectedCert.getGradeId();
                System.out.println("Chứng chỉ: " + cerId + " - " + selectedCert.getGradeName());
                service.addCertification(studentId, cerId);
            }

            if (selectedPrize != null) {
                String prizeId = selectedPrize.getId();
                System.out.println("Giải thưởng: " + prizeId + " - " + selectedPrize.getName());
                service.addAcademicPrize(studentId, prizeId);
            }
            detailController.setStudentDetail(currentDetail.getStudentInfor().getId());
            closeWindow();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void closeWindow() {
        Stage stage = (Stage) btnSave.getScene().getWindow();
        stage.close();
    }
}