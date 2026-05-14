module com.ams.ams_app {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;

    requires java.desktop;
    requires org.json;
    requires jdk.httpserver;
    requires javafx.graphics;
    requires io.github.cdimascio.dotenv.java;
    requires org.apache.poi.ooxml;
    opens com.ams.ams_app.dto to javafx.base, javafx.fxml;
    opens com.ams.ams_app.controller to javafx.fxml;
    exports com.ams.ams_app;
}