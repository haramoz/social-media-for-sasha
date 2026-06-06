package com.steckenrein.app.controller;

import com.steckenrein.app.dto.CreatePostRequest;
import com.steckenrein.app.dto.PostResponse;
import com.steckenrein.app.entity.AppUser;
import com.steckenrein.app.entity.Post;
import com.steckenrein.app.repository.AppUserRepository;
import com.steckenrein.app.repository.PostRepository;
import org.springframework.web.bind.annotation.*;

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
    public PostResponse createPost(@RequestBody CreatePostRequest request) {
        Post post = new Post();
        post.setAuthorId(request.authorId());
        post.setText(request.text());

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
                post.getCreatedAt()
        );
    }
}