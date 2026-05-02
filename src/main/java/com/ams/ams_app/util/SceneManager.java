package com.ams.ams_app.util;

import com.ams.ams_app.controller.MainLayoutController;
import com.ams.ams_app.session.UserSession;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Stack;

public class SceneManager {
    private static Stage stage;
    private static final Stack<Scene> history = new Stack<>();

    public static void setStage(Stage s) {
        stage = s;
    }
    public static void push(Scene scene) {
        if (stage.getScene() != null) {
            history.push(stage.getScene());
        }
        stage.setScene(scene);
    }

    public static void pop() {
        if (!history.isEmpty()) {
            stage.setScene(history.pop());
        }
    }
    public static void setCenter(String fxmlPath, java.util.function.Consumer<Object> controllerConsumer) {
        runOnUI(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource("/view/MainLayout.fxml"));

                if (stage.getScene().getRoot() instanceof javafx.scene.layout.AnchorPane root) {
                    javafx.scene.layout.BorderPane mainPane = (javafx.scene.layout.BorderPane) root.lookup("#mainPane");

                    FXMLLoader childLoader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
                    javafx.scene.Node node = childLoader.load();
                    mainPane.setCenter(node);
                    if (controllerConsumer != null) {
                        controllerConsumer.accept(childLoader.getController());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    // ---(ROOT SWITCHING) ---
    public static void gotoLogin() {
        runOnUI(() -> {
            try {
                history.clear();
                FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource("/view/login.fxml"));
                Scene scene = new Scene(loader.load());
                stage.setMaximized(false); 
                stage.setResizable(false);
                stage.setWidth(1100);
                stage.setHeight(760);

                stage.setScene(scene);
                stage.setTitle("Đăng nhập hệ thống");
                stage.centerOnScreen(); 
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static void gotoWelcome() {
        runOnUI(() -> {
            try {
                history.clear();
                FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource("/view/welcomePage.fxml"));
                Scene scene = new Scene(loader.load());
                stage.setMaximized(false);
                stage.setResizable(false);
                stage.setWidth(1100);
                stage.setHeight(760);

                stage.setScene(scene);
                stage.setTitle("Chào mừng");
                stage.centerOnScreen();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static void gotoMainLayout(UserSession session) {
        runOnUI(() -> {
            try {
                history.clear();
                FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource("/view/MainLayout.fxml"));
                Scene scene = new Scene(loader.load());

                MainLayoutController controller = loader.getController();
                controller.setUserName(session.getUserInfor().getFullName());
                controller.setCenter("/view/pages/DashboardView.fxml");
                stage.setResizable(false);
                stage.setMaximized(true);

                stage.setScene(scene);
                stage.setTitle("Hệ thống quản lý");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    //HELPER
    private static void runOnUI(Runnable task) {
        if (Platform.isFxApplicationThread()) {
            task.run();
        } else {
            Platform.runLater(task);
        }
    }
}