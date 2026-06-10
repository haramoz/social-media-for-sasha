package com.steckenrein.app.controller;


import com.steckenrein.app.dto.LoginRequest;
import com.steckenrein.app.dto.LoginResponse;
import com.steckenrein.app.dto.RegisterRequest;
import com.steckenrein.app.entity.AppUser;
import com.steckenrein.app.repository.AppUserRepository;
import com.steckenrein.app.security.JwtService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(AppUserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;

    }

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        AppUser user = new AppUser();
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        user.setPasswordHash(passwordEncoder.encode(request.password()));

        userRepository.save(user);

        return "Registration successful. Waiting for admin approval.";
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        AppUser user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid email or password");
        }

        if (!user.isApproved()) {
            throw new RuntimeException("Your account is waiting for admin approval.");
        }

        String token = jwtService.generateToken(
        user.getId(),
        user.getEmail());

        return new LoginResponse(
                token,
                user.getId(),
                user.getFirstName(),
                user.getEmail()
        );
    }
}