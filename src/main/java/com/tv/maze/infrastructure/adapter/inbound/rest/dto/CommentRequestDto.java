package com.tv.maze.infrastructure.adapter.inbound.rest.dto;

public record CommentRequestDto(
	    Long show_id,
	    String comment,
	    Integer rating
){}