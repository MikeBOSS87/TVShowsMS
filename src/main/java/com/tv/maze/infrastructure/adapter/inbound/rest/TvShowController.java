package com.tv.maze.infrastructure.adapter.inbound.rest;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tv.maze.application.port.inbound.GetTvShowByIdUseCase;
import com.tv.maze.application.port.inbound.SearchTvShowsUseCase;
import com.tv.maze.domain.ShowSummary;
import com.tv.maze.domain.model.ShowDetail;

@RestController
@RequestMapping("/api/v1/shows")
public class TvShowController{
	
	private final SearchTvShowsUseCase searchTvShowsUseCase;
    private final GetTvShowByIdUseCase getTvShowByIdUseCase;

    public TvShowController(
            SearchTvShowsUseCase searchTvShowsUseCase,
            GetTvShowByIdUseCase getTvShowByIdUseCase
    ) {
        this.searchTvShowsUseCase = searchTvShowsUseCase;
        this.getTvShowByIdUseCase = getTvShowByIdUseCase;
    }
	
    // Endpoint A: Search
    // GET /api/v1/shows/search?search_query=girls
    @GetMapping("/search")
    public ResponseEntity<List<ShowSummary>> searchShows(
            @RequestParam(name = "search_query") String searchQuery
    ) {
        List<ShowSummary> results = searchTvShowsUseCase.searchShows(searchQuery);
        return ResponseEntity.ok(results);
    }

    // Endpoint B: Show detail by ID
    // GET /api/v1/shows/1
    @GetMapping("/{show_id}")
    public ResponseEntity<ShowDetail> getShowById(
            @PathVariable(name = "show_id") Long showId
    ) {
        return getTvShowByIdUseCase.getShowById(showId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }   
}