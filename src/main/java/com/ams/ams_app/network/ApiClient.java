package com.ams.ams_app.network;

import com.ams.ams_app.config.Config;
import com.ams.ams_app.services.AuthService;
import com.ams.ams_app.dto.ApiResponseDTO;
import com.ams.ams_app.session.UserSession;
import com.ams.ams_app.util.SceneManager;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.function.Supplier;

public class ApiClient {

    private static final HttpClient client = HttpClient.newHttpClient();
    public static final String BASE_URL = Config.get("API_BASE_URL");
    private static ApiResponseDTO<?> executeRequest(Supplier<HttpRequest> requestSupplier) throws Exception {
       try {
           // Send request
           HttpRequest request = requestSupplier.get();
           HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

           // Auto REFRESH: If status code 401 (Unauthorized)
           if ((response.statusCode() == 401) && !request.uri().getPath().contains("/auth/refresh")) {
               UserSession session = UserSession.getInstance();
               if (session != null && session.getRefreshtoken() != null) {
                   System.out.println("[LOG - API CLIENT]: Phát hiện lỗi 401. Đang gọi Refresh Token...");
                   boolean refreshSuccess = AuthService.refreshToken(session);
                   if (refreshSuccess) {
                       System.out.println("[LOG - API CLIENT]: Refresh Token thành công! Đang thử lại request cũ...");
                       // RETRY
                       request = requestSupplier.get();
                       response = client.send(request, HttpResponse.BodyHandlers.ofString());
                   } else {
                       System.err.println("[LOG - API CLIENT]: Refresh Token thất bại. Yêu cầu đăng nhập lại.");
                   }
               }
           }
           // Parse JSON
           JSONObject jsonResponse = new JSONObject(response.body());
           return new ApiResponseDTO<>(
                   response.headers().map(),
                   jsonResponse.optInt("statusCode"),
                   jsonResponse.optBoolean("success"),
                   jsonResponse.optString("message"),
                   jsonResponse.opt("data")
           );
       }catch (java.net.ConnectException | java.net.UnknownHostException e) {
           // CHECK connetion
           handleNetworkFailure();
           throw e;
       }
    }

    // --- METHOD API ---
    // METHOD GET()
    public static ApiResponseDTO<?> get(String endpoint) throws Exception {
        return executeRequest(() -> {
            String token = (UserSession.getInstance() != null) ? UserSession.getInstance().getToken() : "";
            System.out.println("API CLIENT: Sending GET() to " + endpoint + " [Token: " + (token.isEmpty() ? "None" : "Present") + "]");
            return HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + endpoint))
                    .header("Cookie", "jwt=" + token)
                    .GET()
                    .build();
        });
    }
    // POST() -- add tokens directly
    public static ApiResponseDTO<?> postWithToken(String endpoint, JSONObject json, String token) throws Exception {
        return executeRequest(() -> {
            System.out.println("API CLIENT: Sending POSTWITHTOKEN() to " + endpoint);
            return HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + endpoint))
                    .header("Content-Type", "application/json")
                    .header("Cookie", "jwt=" + token)
                    .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                    .build();
        });
    }
    // METHOD DELETE()
    public static ApiResponseDTO<?> delete(String endpoint) throws Exception {
        return executeRequest(() -> {
            String token = (UserSession.getInstance() != null) ? UserSession.getInstance().getToken() : "";
            System.out.println("API CLIENT: Sending DELETE() to " + endpoint);
            return HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + endpoint))
                    .header("Cookie", "jwt=" + token)
                    .DELETE()
                    .build();
        });
    }
    // METHOD PATCH ()
    public static ApiResponseDTO<?> patch(String endpoint, JSONObject json) throws Exception {
        return executeRequest(() -> {
            String token = (UserSession.getInstance() != null) ? UserSession.getInstance().getToken() : "";
            System.out.println("API CLIENT: Sending PATCH() to " + endpoint);
            return HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + endpoint))
                    .header("Content-Type", "application/json")
                    .header("Cookie", "jwt=" + token)
                    .method("PATCH", HttpRequest.BodyPublishers.ofString(json.toString()))
                    .build();
        });
    }
    // METHOD PUT()
    public static ApiResponseDTO<?> put(String endpoint, JSONObject json) throws Exception {
        String token = (UserSession.getInstance() != null) ? UserSession.getInstance().getToken() : "";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .header("Content-Type", "application/json")
                .header("Cookie", "jwt=" + token)
                .PUT(HttpRequest.BodyPublishers.ofString(json.toString()))
                .build();
        return executeRequest(() -> request);
    }
    // METHOD POST() - Used for LOGIN REQUEST
    public static ApiResponseDTO<?> post(String endpoint, String json) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        System.out.println("API CLIENT: Sending POST() (No-Token) to " + endpoint);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JSONObject jsonResponse = new JSONObject(response.body());

        return new ApiResponseDTO<>(response.headers().map(),
                jsonResponse.optInt("statusCode"),
                jsonResponse.optBoolean("success"),
                jsonResponse.optString("message"),
                jsonResponse.opt("data"));
    }
    // METHOD POST() - Used for REFRESH TOKEN
    public static ApiResponseDTO<?> callRefreshToken(String endpoint, String refreshToken) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .header("Content-Type", "application/json")
                .header("Cookie", "refresh_token=" + refreshToken)
                .POST(HttpRequest.BodyPublishers.ofString("{}"))
                .build();
        System.out.println("API CLIENT: Sending callRefreshToken to " + endpoint + " với Cookie refresh_token");
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JSONObject jsonResponse = new JSONObject(response.body());
        return new ApiResponseDTO<>(response.headers().map(),
                jsonResponse.optInt("statusCode"),
                jsonResponse.optBoolean("success"),
                jsonResponse.optString("message"),
                jsonResponse.opt("data"));
    }
    // METHOD POST() - User for UPLOAD FILE
    public static ApiResponseDTO<?> uploadFile(String endpoint, File file) throws Exception {
        String token = (UserSession.getInstance() != null) ? UserSession.getInstance().getToken() : "";
        String boundary = "===" + System.currentTimeMillis() + "===";
        String LINE_FEED = "\r\n";

        URL url = new URL(BASE_URL + endpoint);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setUseCaches(false);
        httpConn.setDoOutput(true);  // request POST
        httpConn.setRequestMethod("POST");
        httpConn.setRequestProperty("Cookie", "jwt=" + token);
        httpConn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

        try (OutputStream outputStream = httpConn.getOutputStream()) {
            outputStream.write(("--" + boundary + LINE_FEED).getBytes());
            outputStream.write(("Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getName() + "\"" + LINE_FEED).getBytes());
            outputStream.write(("Content-Type: application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" + LINE_FEED).getBytes());
            outputStream.write((LINE_FEED).getBytes());
            try (FileInputStream inputStream = new FileInputStream(file)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
            outputStream.write((LINE_FEED).getBytes());
            outputStream.write(("--" + boundary + "--" + LINE_FEED).getBytes());
            outputStream.flush();
        }

        // READ Response from Backend
        int statusCode = httpConn.getResponseCode();

        //  200 (Success) và 207 (Partial) 
        InputStream is = (statusCode == 200 || statusCode == 207) ? httpConn.getInputStream() : httpConn.getErrorStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder responseStr = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            responseStr.append(line);
        }
        reader.close();

        JSONObject jsonResponse = new JSONObject(responseStr.toString());
        return new ApiResponseDTO<>(null, statusCode,
                statusCode == 200 || statusCode == 207,
                jsonResponse.optString("message"),
                jsonResponse.opt("data"));
    }
    private static void handleNetworkFailure() {
        Platform.runLater(() -> {
            UserSession session = UserSession.getInstance();
            if (session != null && !session.isOfflineMode()) {
                session.setOfflineMode(true);
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Thông báo kết nối");
                alert.setHeaderText("Mất kết nối tới máy chủ");
                alert.setContentText("Bạn hiện đang chuyển sang chế độ Ngoại tuyến. Một số tính năng sẽ bị hạn chế.");
                alert.show();
                SceneManager.gotoMainLayout(session);
            }
        });
    }
}