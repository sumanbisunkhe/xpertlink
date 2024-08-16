package com.example.xpertlink.jwt;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthenticationRequest {
    // Getters and Setters
    private String username;
    private String password;
    private String email;

    // Constructors
    public AuthenticationRequest() {
    }

    public AuthenticationRequest(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public AuthenticationRequest(String password, String email) {
        this.password = password;
        this.email = email;
    }

}