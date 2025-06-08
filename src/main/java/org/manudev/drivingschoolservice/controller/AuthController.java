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

    @PostMapping(name = "/login")
    public ResponseEntity<RegisterResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping(name = "/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        if (authService.register(request) != null) {
            return ResponseEntity.ok(authService.register(request));
        }
        return ResponseEntity.internalServerError().build();
    }
}
