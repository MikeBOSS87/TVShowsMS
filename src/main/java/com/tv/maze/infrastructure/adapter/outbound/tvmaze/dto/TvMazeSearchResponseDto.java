package com.tv.maze.infrastructure.adapter.outbound.tvmaze.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public record TvMazeSearchResponseDto(
		Double score,
	    TvMazeShowDto show
) {
}