package com.tv.maze.application.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tv.maze.application.port.inbound.GetTvShowByIdUseCase;
import com.tv.maze.application.port.inbound.SearchTvShowsUseCase;
import com.tv.maze.domain.model.Comment;
import com.tv.maze.domain.ShowSummary;
import com.tv.maze.domain.model.ShowDetail;
import com.tv.maze.domain.port.inbound.AddCommentUseCase;
import com.tv.maze.domain.port.outbound.CommentRepositoryPort;
import com.tv.maze.domain.port.outbound.TvShowClientPort;
import com.tv.maze.domain.port.outbound.TvShowRepositoryPort;

@Service
public class TvShowService implements SearchTvShowsUseCase, GetTvShowByIdUseCase, AddCommentUseCase {

	private static final Logger log = LoggerFactory.getLogger(TvShowService.class);
	
	private final TvShowClientPort tvShowClientPort;
    private final TvShowRepositoryPort tvShowRepositoryPort;
    private final CommentRepositoryPort commentRepositoryPort;

    public TvShowService( 
    		TvShowClientPort tvShowClientPort,
    		TvShowRepositoryPort tvShowRepositoryPort,
            CommentRepositoryPort commentRepositoryPort
    		) {
        this.tvShowClientPort = tvShowClientPort;
        this.tvShowRepositoryPort = tvShowRepositoryPort;
        this.commentRepositoryPort = commentRepositoryPort;
    }

    @Override
    public List< ShowSummary > searchShows( String searchQuery ) {
        if (searchQuery == null || searchQuery.isBlank()) {
        	log.warn("El parámetro 'searchQuery' es nulo o está vacío.");
            return List.of();
        }
        log.debug("Iniciando búsqueda de shows en cliente externo con query='{}'", searchQuery);
        return tvShowClientPort.searchShows( searchQuery );
    }

    //Estrategia de Caché
    @Override
    public Optional<ShowDetail> getShowById( Long showId ) {
        if( showId == null || showId <= 0 ){
        	log.warn("ID de show no válido: {}", showId);
            return Optional.empty();
        }
        
        // 1. Buscar primero en MongoDB (Caché)
        log.debug("Verificando existencia del show_id={} en la base de datos (Cache)...", showId);
        
        Optional<ShowDetail> cachedShow = tvShowRepositoryPort.findById(showId);
        if (cachedShow.isPresent()) {
        	log.info("[CACHE HIT] Show encontrado en DB para show_id={}", showId);
            return cachedShow;
        }

        // 2. Si no está en MongoDB, consumir API externa
        log.info("[CACHE MISS] Show no encontrado en MongoDB para show_id={}. Consultando API externa de TVMaze...", showId);
        
        Optional<ShowDetail> externalShow = tvShowClientPort.getShowById(showId);

        // 3. Guardar en MongoDB antes de retornar
        externalShow.ifPresent( show -> {
            log.info("Guardando show con ID={} en MongoDB para futuras consultas de caché.", showId);
            tvShowRepositoryPort.save( show );
        });

        return externalShow;
    }

    //Guardar comentario
	@Override
	public void addComment( Long showId, String comment, Integer rating ){
		if (rating < 0 || rating > 5) {
			log.warn( "Validación fallida: Rating debe estar entre 0 y 5. Rating recibido: {}", rating );
            throw new IllegalArgumentException( "El rating debe estar entre 0 y 5" );
        }
		
		log.debug("Creando modelo de dominio para el comentario en show_id={}", showId);
        Comment comentario = new Comment( showId, comment, rating, LocalDateTime.now() );
        
        commentRepositoryPort.save( comentario );
        log.info("Comentario persistido correctamente para el show_id={}", showId);
	}
}