package com.tv.maze.domain.model;

import java.time.LocalDateTime;

public record Comment(
		Long showId,
	    String comment,
	    Integer rating,
	    LocalDateTime createdAt
) {}