package com.steckenrein.app.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long authorId;

    @Column(nullable = false, length = 2000)
    private String text;

    private Instant createdAt = Instant.now();

    public Long getId() { return id; }

    public Long getAuthorId() { return authorId; }
    public void setAuthorId(Long authorId) { this.authorId = authorId; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public Instant getCreatedAt() { return createdAt; }
}