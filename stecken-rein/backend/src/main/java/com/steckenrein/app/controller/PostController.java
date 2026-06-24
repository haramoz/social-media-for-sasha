package com.steckenrein.app.controller;

import com.steckenrein.app.dto.CreatePostRequest;
import com.steckenrein.app.dto.PostResponse;
import com.steckenrein.app.entity.AppUser;
import com.steckenrein.app.entity.Post;
import com.steckenrein.app.repository.AppUserRepository;
import com.steckenrein.app.repository.PostRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostRepository postRepository;
    private final AppUserRepository userRepository;

    public PostController(PostRepository postRepository, AppUserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<PostResponse> getPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @PostMapping
    public PostResponse createPost(
            @RequestBody CreatePostRequest request,
            Authentication authentication
    ) {
        AppUser currentUser = (AppUser) authentication.getPrincipal();

        Post post = new Post();
        post.setAuthorId(currentUser.getId());
        post.setText(request.text());

        return toResponse(postRepository.save(post));
    }

    @PostMapping("/with-image")
    public PostResponse createPostWithImage(
            @RequestParam(value = "text", required = false) String text,
            @RequestParam(value = "image", required = false) MultipartFile image,
            Authentication authentication
    ) throws Exception {
        AppUser currentUser = (AppUser) authentication.getPrincipal();

        if ((text == null || text.isBlank()) && (image == null || image.isEmpty())) {
            throw new RuntimeException("Text or image is required");
        }

        Post post = new Post();
        post.setAuthorId(currentUser.getId());
        post.setText(text == null ? "" : text);

        if (image != null && !image.isEmpty()) {
            Path uploadDir = Path.of("uploads");
            Files.createDirectories(uploadDir);

            String originalFilename = image.getOriginalFilename();
            String extension = "";

            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            String filename = UUID.randomUUID() + extension;
            Path filePath = uploadDir.resolve(filename);

            image.transferTo(filePath.toAbsolutePath().toFile());

            post.setImagePath("/uploads/" + filename);
        }

        return toResponse(postRepository.save(post));
    }

    private PostResponse toResponse(Post post) {
        AppUser author = userRepository.findById(post.getAuthorId())
                .orElse(null);

        String authorName = author == null
                ? "Unknown neighbor"
                : author.getFirstName() + " " + author.getLastName();

        return new PostResponse(
        post.getId(),
        post.getAuthorId(),
        authorName,
        post.getText(),
        post.getImagePath(),
        post.getCreatedAt()
);
    }
}