package com.javatechie.dto;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RefreshTokenRequest {

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
