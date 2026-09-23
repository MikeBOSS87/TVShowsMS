package com.tv.maze.application.port.inbound;

import java.util.Optional;

import com.tv.maze.domain.model.ShowDetail;

public interface GetTvShowByIdUseCase {
	
	Optional<ShowDetail> getShowById( Long showId );
}