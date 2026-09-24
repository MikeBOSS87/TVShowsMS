package com.tv.maze.domain.port.outbound;

import java.util.Optional;

import com.tv.maze.domain.model.ShowDetail;

public interface TvShowRepositoryPort {
	
	Optional<ShowDetail> findById( Long showId );
    ShowDetail save( ShowDetail showDetail );
}