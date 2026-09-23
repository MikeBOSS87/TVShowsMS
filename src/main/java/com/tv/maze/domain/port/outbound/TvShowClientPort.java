package com.tv.maze.domain.port.outbound;

import java.util.List;
import java.util.Optional;

import com.tv.maze.domain.ShowSummary;
import com.tv.maze.domain.model.ShowDetail;

public interface TvShowClientPort {
	
	List<ShowSummary> searchShows(String query);
    Optional<ShowDetail> getShowById(Long showId);
}