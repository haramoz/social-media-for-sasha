package com.steckenrein.app.controller;

import com.steckenrein.app.dto.UserResponse;
import com.steckenrein.app.entity.AppUser;
import com.steckenrein.app.repository.AppUserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AppUserRepository userRepository;

    public AdminController(AppUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/users/pending")
    public List<UserResponse> pendingUsers(Authentication authentication) {
        requireAdmin(authentication);

        return userRepository.findAll()
                .stream()
                .filter(user -> !user.isApproved())
                .map(this::toResponse)
                .toList();
    }

    @PostMapping("/users/{id}/approve")
    public UserResponse approveUser(
            @PathVariable Long id,
            Authentication authentication
    ) {
        requireAdmin(authentication);

        AppUser user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setApproved(true);

        return toResponse(userRepository.save(user));
    }

    @DeleteMapping("/users/{id}")
    public void rejectUser(
            @PathVariable Long id,
            Authentication authentication
    ) {
        requireAdmin(authentication);

        AppUser user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userRepository.delete(user);
    }

    private void requireAdmin(Authentication authentication) {
        AppUser currentUser = (AppUser) authentication.getPrincipal();

        if (!"ADMIN".equals(currentUser.getRole())) {
            throw new RuntimeException("Admin access required");
        }
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