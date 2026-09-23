package com.tv.maze.domain.model;

import java.util.List;
import java.util.Map;

public record ShowDetail(
		Long id,
	    String url,
	    String name,
	    String type,
	    String language,
	    List<String> genres,
	    String status,
	    Integer runtime,
	    String premiered,
	    String officialSite,
	    String summary,
	    Map<String, Object> additionalAttributes
) {
}