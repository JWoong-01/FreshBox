package com.example.server.user.web.dto;

public class AuthResponse {

    private final boolean success;
    private final String userId;
    private final String userName;
    private final Integer age;
    private final String message;

    private AuthResponse(boolean success, String userId, String userName, Integer age, String message) {
        this.success = success;
        this.userId = userId;
        this.userName = userName;
        this.age = age;
        this.message = message;
    }

    public static AuthResponse success(String userId, String userName, Integer age) {
        return new AuthResponse(true, userId, userName, age, null);
    }

    public static AuthResponse failure(String message) {
        return new AuthResponse(false, null, null, null, message);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public Integer getAge() {
        return age;
    }

    public String getMessage() {
        return message;
    }
}
