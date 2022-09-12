package com.exercise.demojwt.models;

public class AuthResponse {
    private String token;
    private String roleName;

    
    public AuthResponse(String token, String roleName) {
        this.token = token;
        this.roleName = roleName;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getRoleName() {
        return roleName;
    }
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
