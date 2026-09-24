package com.tv.maze.infrastructure.adapter.outbound.persistence.mongo.document;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "tvShowComments")
public class CommentDocument {
	
	@Id
    private String id;
    private Long showId;
    private String comment;
    private Integer rating;
    private LocalDateTime createdAt;

    public CommentDocument() {}

    public CommentDocument(Long showId, String comment, Integer rating, LocalDateTime createdAt) {
        this.showId = showId;
        this.comment = comment;
        this.rating = rating;
        this.createdAt = createdAt;
    }

    // Getters y Setters
    public String getId() { return id; }
    public Long getShowId() { return showId; }
    public String getComment() { return comment; }
    public Integer getRating() { return rating; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}