package com.tv.maze.application.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.tv.maze.application.port.inbound.GetTvShowByIdUseCase;
import com.tv.maze.application.port.inbound.SearchTvShowsUseCase;
import com.tv.maze.domain.ShowSummary;
import com.tv.maze.domain.model.ShowDetail;
import com.tv.maze.domain.port.outbound.TvShowClientPort;

@Service
public class TvShowService implements SearchTvShowsUseCase, GetTvShowByIdUseCase{

	private final TvShowClientPort tvShowClientPort;

    public TvShowService( TvShowClientPort tvShowClientPort ) {
        this.tvShowClientPort = tvShowClientPort;
    }

    @Override
    public List< ShowSummary > searchShows( String searchQuery ) {
        if (searchQuery == null || searchQuery.isBlank()) {
            return List.of();
        }
        return tvShowClientPort.searchShows( searchQuery );
    }

    @Override
    public Optional<ShowDetail> getShowById( Long showId ) {
        if( showId == null || showId <= 0 ){
            return Optional.empty();
        }
        return tvShowClientPort.getShowById(showId);
    }
}