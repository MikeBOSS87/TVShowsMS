package com.tv.maze.application.port.inbound;

import java.util.List;

import com.tv.maze.domain.ShowSummary;

public interface SearchTvShowsUseCase {
	
	List<ShowSummary> searchShows( String searchQuery );
}