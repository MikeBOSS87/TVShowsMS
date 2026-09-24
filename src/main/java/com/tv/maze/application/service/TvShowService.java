package com.tv.maze.application.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

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
            return List.of();
        }
        return tvShowClientPort.searchShows( searchQuery );
    }

    //Estrategia de Caché
    @Override
    public Optional<ShowDetail> getShowById( Long showId ) {
        if( showId == null || showId <= 0 ){
            return Optional.empty();
        }
        
        // 1. Buscar primero en MongoDB (Caché)
        Optional<ShowDetail> cachedShow = tvShowRepositoryPort.findById(showId);
        if (cachedShow.isPresent()) {
            return cachedShow;
        }

        // 2. Si no está en MongoDB, consumir API externa
        Optional<ShowDetail> externalShow = tvShowClientPort.getShowById(showId);

        // 3. Guardar en MongoDB antes de retornar
        externalShow.ifPresent( tvShowRepositoryPort::save );

        return externalShow;
    }

    //Guardar comentario
	@Override
	public void addComment( Long showId, String comment, Integer rating ){
		if (rating < 0 || rating > 5) {
            throw new IllegalArgumentException( "El rating debe estar entre 0 y 5" );
        }
		
        Comment comentario = new Comment( showId, comment, rating, LocalDateTime.now() );
        commentRepositoryPort.save( comentario );
	}
}