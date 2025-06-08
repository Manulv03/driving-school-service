package org.manudev.drivingschoolservice.controller;

import lombok.RequiredArgsConstructor;
import org.manudev.drivingschoolservice.dtos.AuthResponse;
import org.manudev.drivingschoolservice.dtos.LoginRequest;
import org.manudev.drivingschoolservice.dtos.RegisterRequest;
import org.manudev.drivingschoolservice.dtos.RegisterResponse;
import org.manudev.drivingschoolservice.services.AuthService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RegisterResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        if (response != null) {
            return ResponseEntity.ok().body(response);
        }
        return ResponseEntity.badRequest().build();
    }
}
