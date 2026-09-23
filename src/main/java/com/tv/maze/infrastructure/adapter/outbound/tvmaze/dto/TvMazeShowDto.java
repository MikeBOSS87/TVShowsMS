package com.tv.maze.infrastructure.adapter.outbound.tvmaze.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TvMazeShowDto(
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
	    NetworkDto network,
	    WebChannelDto webChannel,
	    String summary
) {
	@JsonIgnoreProperties( ignoreUnknown = true )
    public record NetworkDto( Long id, String name ) {}

    @JsonIgnoreProperties( ignoreUnknown = true )
    public record WebChannelDto( Long id, String name ) {}

    public String getChannelName() {
        if( network != null && network.name() != null && !network.name().isBlank() ){
            return network.name();
        }
        if( webChannel != null && webChannel.name() != null && !webChannel.name().isBlank() ){
            return webChannel.name();
        }
        return "N/A";
    }
}