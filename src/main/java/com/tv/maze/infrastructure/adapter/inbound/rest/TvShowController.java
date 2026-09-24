package com.tv.maze.infrastructure.adapter.inbound.rest;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tv.maze.application.port.inbound.GetTvShowByIdUseCase;
import com.tv.maze.application.port.inbound.SearchTvShowsUseCase;
import com.tv.maze.domain.ShowSummary;
import com.tv.maze.domain.model.ShowDetail;
import com.tv.maze.domain.port.inbound.AddCommentUseCase;
import com.tv.maze.infrastructure.adapter.inbound.rest.dto.CommentRequestDto;

@RestController
@RequestMapping( "/api/v1/shows" )
public class TvShowController{
	
	private final SearchTvShowsUseCase searchTvShowsUseCase;
    private final GetTvShowByIdUseCase getTvShowByIdUseCase;
    private final AddCommentUseCase addCommentUseCase;

    public TvShowController(
            SearchTvShowsUseCase searchTvShowsUseCase,
            GetTvShowByIdUseCase getTvShowByIdUseCase,
            AddCommentUseCase addCommentUseCase
    ) {
        this.searchTvShowsUseCase = searchTvShowsUseCase;
        this.getTvShowByIdUseCase = getTvShowByIdUseCase;
        this.addCommentUseCase = addCommentUseCase ;
    }
	
    // Search
    // GET /api/v1/shows/search?search_query=girls
    @GetMapping( "/search" )
    public ResponseEntity< List < ShowSummary > > searchShows(
            @RequestParam( name = "search_query" ) String searchQuery
    ) {
        List<ShowSummary> results = searchTvShowsUseCase.searchShows( searchQuery );
        return ResponseEntity.ok( results );
    }

    // Show detail by ID
    // GET /api/v1/shows/1
    @GetMapping( "/{show_id}" )
    public ResponseEntity< ShowDetail > getShowById(
            @PathVariable( name = "show_id" ) Long showId
    ) {
        return getTvShowByIdUseCase.getShowById(showId)
                .map( ResponseEntity::ok )
                .orElseGet( () -> ResponseEntity.notFound().build() );
    }
    
    // Endpoint comments
    // POST /api/v1/shows/comments
    @PostMapping("/comments")
    public ResponseEntity<Map<String, String>> addComment(
            @RequestBody CommentRequestDto request
    ) {
        try {
            addCommentUseCase.addComment( request.show_id(), request.comment(), request.rating() );
            return ResponseEntity.status( HttpStatus.CREATED )
                    .body( Map.of( "status", "SUCCESS", "message", "Comentario guardado correctamente" ) );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body( Map.of( "status", "ERROR", "message", e.getMessage() ) ) ;
        }
    }
}