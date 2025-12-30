package com.server.dto;

public class AuthRequest {
    private String username;
    private String password;

    // 必须有无参构造函数
    public AuthRequest() {
    }

    public AuthRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getter 和 Setter 方法
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}