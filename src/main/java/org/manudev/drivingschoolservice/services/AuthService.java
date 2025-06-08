package org.manudev.drivingschoolservice.services;

import lombok.extern.slf4j.Slf4j;
import org.manudev.drivingschoolservice.dtos.AuthResponse;
import org.manudev.drivingschoolservice.dtos.LoginRequest;
import org.manudev.drivingschoolservice.dtos.RegisterRequest;
import org.manudev.drivingschoolservice.dtos.RegisterResponse;
import org.manudev.drivingschoolservice.entities.Role;
import org.manudev.drivingschoolservice.entities.User;
import org.manudev.drivingschoolservice.repository.IRoleRepository;
import org.manudev.drivingschoolservice.repository.IUserRepository;
import org.manudev.drivingschoolservice.security.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthService(IUserRepository userRepository,
                       IRoleRepository roleRepository,
                       JwtService jwtService,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUser(request.getUser())) {
            logger.warn("Intento de registro fallido: el usuario '{}' ya existe", request.getUser());
            return null;
        }

        User user = new User();
        user.setUser(request.getUser());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEnabled(true);

        Role studentRole = roleRepository.findByName("STUDENT")
                .orElseThrow(() -> new RuntimeException("Rol 'STUDENT' no encontrado"));

        user.setRoles(Set.of(studentRole));

        String token;
        try {
            token = jwtService.generateToken(user);
        } catch (Exception e) {
            logger.error("Error generando el token para el usuario '{}': {}", request.getUser(), e.getMessage());
            return null;
        }

        userRepository.save(user);
        logger.info("Usuario '{}' registrado exitosamente", request.getUser());

        return new AuthResponse(token);
    }

    public RegisterResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = (User) authentication.getPrincipal();
        String token = jwtService.generateToken(user);
        return new RegisterResponse(token, user.getUser(), user.getUsername(), user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet()));
    }
}
