package com.ams.ams_app.dto;

import java.util.List;
import java.util.Map;

public class ApiResponseDTO<T> {
    private Map<String, List<String>> headers;
    private int statusCode;
    private boolean success;
    private String message;
    private T data;

    public ApiResponseDTO(Map<String, List<String>> headers, int statusCode, boolean success, String message, T data) {
        this.headers = headers;
        this.statusCode = statusCode;
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, List<String>> headers) {
        this.headers = headers;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
