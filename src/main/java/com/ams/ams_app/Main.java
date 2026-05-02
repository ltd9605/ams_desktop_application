package com.ams.ams_app;

import com.ams.ams_app.util.SceneManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/welcomePage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),1100,760);

        primaryStage.setTitle("Tra cứu Kết quả tuyển sinh");
        primaryStage.setScene(scene);

        SceneManager.setStage(primaryStage);

        primaryStage.show();
    }
}