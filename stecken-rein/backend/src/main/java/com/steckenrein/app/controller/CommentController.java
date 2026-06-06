package com.steckenrein.app.controller;

import com.steckenrein.app.dto.CommentResponse;
import com.steckenrein.app.dto.CreateCommentRequest;
import com.steckenrein.app.entity.AppUser;
import com.steckenrein.app.entity.Comment;
import com.steckenrein.app.repository.AppUserRepository;
import com.steckenrein.app.repository.CommentRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {

    private final CommentRepository commentRepository;
    private final AppUserRepository userRepository;

    public CommentController(CommentRepository commentRepository, AppUserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<CommentResponse> getComments(@PathVariable Long postId) {
        return commentRepository.findByPostIdOrderByCreatedAtAsc(postId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @PostMapping
    public CommentResponse createComment(
            @PathVariable Long postId,
            @RequestBody CreateCommentRequest request
    ) {
        Comment comment = new Comment();
        comment.setPostId(postId);
        comment.setAuthorId(request.authorId());
        comment.setText(request.text());

        return toResponse(commentRepository.save(comment));
    }

    private CommentResponse toResponse(Comment comment) {
        AppUser author = userRepository.findById(comment.getAuthorId()).orElse(null);

        String authorName = author == null
                ? "Unknown neighbor"
                : author.getFirstName() + " " + author.getLastName();

        return new CommentResponse(
                comment.getId(),
                comment.getPostId(),
                comment.getAuthorId(),
                authorName,
                comment.getText(),
                comment.getCreatedAt()
        );
    }
}