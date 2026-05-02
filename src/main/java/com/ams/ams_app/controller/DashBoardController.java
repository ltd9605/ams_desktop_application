package com.ams.ams_app.controller;

import com.ams.ams_app.services.DashboardService;
import com.ams.ams_app.services.AnalysisService;
import com.ams.ams_app.dto.StatisticsDTO;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.*;
import java.util.stream.Collectors;

public class DashBoardController {
    @FXML private Label totalStudentsLbl, acceptedStudentsLbl, pendingStudentsLbl, totalMajorsLbl;

    @FXML private BarChart<String, Number> barChartTop3;
    @FXML private BarChart<String, Number> barChartEmptyQuota;
    @FXML private PieChart pieChartPassFail;
    @FXML private ListView<String> listAverageScores;

    @FXML private CategoryAxis xAxisTop3, xAxisQuota;

    private final DashboardService dashService = new DashboardService();
    private final AnalysisService analysisService = new AnalysisService();

    public void initialize() {
        xAxisTop3.setTickLabelRotation(-35);
        xAxisQuota.setTickLabelRotation(-35);

        loadSummaryData();
        loadTop3Applications();
        loadTop3QuotaVsActualChart();
        loadAverageScoresList();
    }

    private void loadSummaryData() {
        new Thread(() -> {
            try {
                StatisticsDTO stats = dashService.getStatistics();
                Platform.runLater(() -> {
                    totalStudentsLbl.setText(String.valueOf(stats.getTotalStudents()));
                    acceptedStudentsLbl.setText(String.valueOf(stats.getAcceptedCount()));
                    pendingStudentsLbl.setText(String.valueOf(stats.getPendingCount()));
                    totalMajorsLbl.setText(String.valueOf(stats.getTotalMajors()));

                    int passed = stats.getAcceptedCount();
                    int failed = stats.getTotalStudents() - passed;

                    pieChartPassFail.getData().setAll(
                            new PieChart.Data("Trúng tuyển (" + passed + ")", passed),
                    new PieChart.Data("Còn lại (" + failed + ")", failed)
                    );
                });
            } catch (Exception e) { e.printStackTrace(); }
        }).start();
    }

    private void loadTop3Applications() {
        new Thread(() -> {
            try {
                Map<String, Integer> data = analysisService.getApplicationsByMajor();
                List<Map.Entry<String, Integer>> top3 = data.entrySet().stream()
                        .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                        .limit(3)
                        .collect(Collectors.toList());

                Platform.runLater(() -> {
                    XYChart.Series<String, Number> series = new XYChart.Series<>();
                    series.setName("Số lượng hồ sơ");
                    for (Map.Entry<String, Integer> entry : top3) {
                        series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
                    }
                    barChartTop3.getData().setAll(series);
                });
            } catch (Exception e) { e.printStackTrace(); }
        }).start();
    }

    private void loadTop3QuotaVsActualChart() {
        new Thread(() -> {
            try {
                JSONArray predictions = analysisService.getRawCutoffPredictions();
                List<JSONObject> list = new ArrayList<>();
                for (int i = 0; i < predictions.length(); i++) {
                    list.add(predictions.getJSONObject(i));
                }
                // GET TOP 3 RESULT
                List<JSONObject> top3 = list.stream()
                        .sorted((a, b) -> Integer.compare(b.optInt("applicantCount"), a.optInt("applicantCount")))
                        .limit(3)
                        .collect(Collectors.toList());

                Platform.runLater(() -> {
                    XYChart.Series<String, Number> quotaSeries = new XYChart.Series<>();
                    quotaSeries.setName("Chỉ tiêu");

                    XYChart.Series<String, Number> actualSeries = new XYChart.Series<>();
                    actualSeries.setName("Số hồ sơ");

                    for (JSONObject item : top3) {
                        String label = item.optString("majorName");
                        quotaSeries.getData().add(new XYChart.Data<>(label, item.optInt("quota")));
                        actualSeries.getData().add(new XYChart.Data<>(label, item.optInt("applicantCount")));
                    }
                    barChartEmptyQuota.getData().setAll(quotaSeries, actualSeries);
                });
            } catch (Exception e) { e.printStackTrace(); }
        }).start();
    }

    private void loadAverageScoresList() {
        new Thread(() -> {
            try {
                Map<String, Double> averages = analysisService.getAverageScoresByMajor();
                List<String> items = averages.entrySet().stream()
                        .map(e -> String.format("• %s: %.2f điểm", e.getKey(), e.getValue()))
                        .collect(Collectors.toList());

                Platform.runLater(() -> listAverageScores.getItems().setAll(items));
            } catch (Exception e) { e.printStackTrace(); }
        }).start();
    }
}