package com.steckenrein.app.controller;

import com.steckenrein.app.dto.UserResponse;
import com.steckenrein.app.entity.AppUser;
import com.steckenrein.app.repository.AppUserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    private final AppUserRepository userRepository;

    public UserController(AppUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/api/users")
    public List<UserResponse> users() {
        return userRepository.findAll()
                .stream()
                .filter(AppUser::isApproved)
                .map(this::toResponse)
                .toList();
    }

    private UserResponse toResponse(AppUser user) {
        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole(),
                user.isApproved()
        );
    }
}