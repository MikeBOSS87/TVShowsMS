package com.tv.maze.domain;

import java.util.List;

public record ShowSummary(
		Long id,
	    String name,
	    String channel,
	    String summary,
	    List<String> genres
){ }