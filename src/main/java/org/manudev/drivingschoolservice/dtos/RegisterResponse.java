package org.manudev.drivingschoolservice.dtos;

import lombok.AllArgsConstructor;

import java.util.Set;

public class RegisterResponse {
    private String token;
    private String username;
    private String email;
    private Set<String> roles;

    public RegisterResponse(String token, String username, String email, Set<String> roles) {
        this.token = token;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }
}
