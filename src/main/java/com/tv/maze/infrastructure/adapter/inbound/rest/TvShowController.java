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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.tv.maze.application.port.inbound.GetTvShowByIdUseCase;
import com.tv.maze.application.port.inbound.SearchTvShowsUseCase;
import com.tv.maze.domain.ShowSummary;
import com.tv.maze.domain.model.ShowDetail;
import com.tv.maze.domain.port.inbound.AddCommentUseCase;
import com.tv.maze.infrastructure.adapter.inbound.rest.dto.CommentRequestDto;

@RestController
@RequestMapping( "/api/v1/shows" )
@Tag(name = "TV Shows API", description = "Endpoints para búsqueda, consulta detallada y retroalimentación de shows de televisión.")
public class TvShowController{
	
	private static final Logger log = LoggerFactory.getLogger( TvShowController.class );
	
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
    @Operation(summary = "Buscar programas de televisión", description = "Consulta programas consumiendo la API pública de TVMaze mediante un término de búsqueda.")
    @ApiResponse(responseCode = "200", description = "Búsqueda procesada con éxito.")
    @GetMapping( "/search" )
    public ResponseEntity< List < ShowSummary > > searchShows(
            @RequestParam( name = "search_query" ) String searchQuery
    ) {
    	log.info("Petición REST recibida: GET /api/v1/shows/search?search_query={}", searchQuery);
        List<ShowSummary> shows = searchTvShowsUseCase.searchShows( searchQuery );
        
        log.info("Búsqueda finalizada. Resultados obtenidos: {}", shows.size());
        return ResponseEntity.ok( shows );
    }

    // Show detail by ID
    // GET /api/v1/shows/1
    @Operation(summary = "Consultar show por ID (Estrategia Cache)", description = "Valida si el programa existe en MongoDB. Si no existe, lo consulta en TVMaze y persiste la información en MongoDB antes de responder.")
    @ApiResponse(responseCode = "200", description = "Show encontrado con éxito.")
    @ApiResponse(responseCode = "404", description = "Show no encontrado.")
    @GetMapping( "/{show_id}" )
    public ResponseEntity< ShowDetail > getShowById(
            @PathVariable( name = "show_id" ) Long showId
    ) {
    	log.info("Petición REST recibida: GET /api/v1/shows/{}", showId);
        return getTvShowByIdUseCase.getShowById(showId)
                .map( show -> {
                    log.info("Show con ID {} retornado exitosamente.", showId);
                    return ResponseEntity.ok( show );
                })
                .orElseGet(() -> {
                    log.warn("Show con ID {} no encontrado.", showId);
                    return ResponseEntity.notFound().build();
                });
    }
    
    // Endpoint comments
    // POST /api/v1/shows/comments
    @Operation(summary = "Registrar comentario y calificación", description = "Guarda una calificación (0 a 5) y un comentario en MongoDB Atlas vinculados al ID del show.")
    @ApiResponse(responseCode = "201", description = "Comentario registrado con éxito.")
    @ApiResponse(responseCode = "400", description = "Parámetros inválidos (ej. rating fuera de rango 0-5).")
    @PostMapping("/comments")
    public ResponseEntity<Map<String, String>> addComment(
            @RequestBody CommentRequestDto request
    ) {
        try {
            addCommentUseCase.addComment( request.show_id(), request.comment(), request.rating() );
            log.info("Comentario registrado con éxito para show_id={}", request.show_id());
            
            return ResponseEntity.status( HttpStatus.CREATED )
                    .body( Map.of( "status", "SUCCESS", "message", "Comentario guardado correctamente" ) );
        } catch (IllegalArgumentException e) {
        	
        	log.error("Error al procesar comentario para show_id={}: {}", request.show_id(), e.getMessage());
            return ResponseEntity.badRequest()
                    .body( Map.of( "status", "ERROR", "message", e.getMessage() ) ) ;
        }
    }
}