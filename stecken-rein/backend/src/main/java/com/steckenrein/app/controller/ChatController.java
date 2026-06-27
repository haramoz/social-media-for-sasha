package com.steckenrein.app.controller;

import com.steckenrein.app.dto.ChatMessageResponse;
import com.steckenrein.app.entity.AppUser;
import com.steckenrein.app.entity.ChatMessage;
import com.steckenrein.app.repository.AppUserRepository;
import com.steckenrein.app.repository.ChatMessageRepository;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatMessageRepository chatRepository;
    private final AppUserRepository userRepository;

    public ChatController(ChatMessageRepository chatRepository, AppUserRepository userRepository) {
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/{neighborId}")
    public List<ChatMessageResponse> getConversation(
            @PathVariable Long neighborId,
            Authentication authentication
    ) {
        AppUser currentUser = (AppUser) authentication.getPrincipal();

        return chatRepository
                .findBySenderIdAndReceiverIdOrReceiverIdAndSenderIdOrderByCreatedAtAsc(
                        currentUser.getId(),
                        neighborId,
                        currentUser.getId(),
                        neighborId
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @PostMapping("/{neighborId}")
    public ChatMessageResponse sendMessage(
            @PathVariable Long neighborId,
            @RequestParam(value = "text", required = false) String text,
            @RequestParam(value = "image", required = false) MultipartFile image,
            Authentication authentication
    ) throws Exception {
        AppUser currentUser = (AppUser) authentication.getPrincipal();

        if ((text == null || text.isBlank()) && (image == null || image.isEmpty())) {
            throw new RuntimeException("Text or image is required");
        }

        ChatMessage message = new ChatMessage();
        message.setSenderId(currentUser.getId());
        message.setReceiverId(neighborId);
        message.setText(text == null ? "" : text);

        if (image != null && !image.isEmpty()) {
            Path uploadDir = Path.of("uploads/chat");
            Files.createDirectories(uploadDir);

            String originalFilename = image.getOriginalFilename();
            String extension = "";

            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            String filename = UUID.randomUUID() + extension;
            Path filePath = uploadDir.resolve(filename);

            image.transferTo(filePath.toAbsolutePath().toFile());

            message.setImagePath("/uploads/chat/" + filename);
        }

        return toResponse(chatRepository.save(message));
    }

    private ChatMessageResponse toResponse(ChatMessage message) {
        AppUser sender = userRepository.findById(message.getSenderId()).orElse(null);

        String senderName = sender == null
                ? "Unknown neighbor"
                : sender.getFirstName() + " " + sender.getLastName();

        return new ChatMessageResponse(
                message.getId(),
                message.getSenderId(),
                message.getReceiverId(),
                senderName,
                message.getText(),
                message.getImagePath(),
                message.getCreatedAt()
        );
    }
}