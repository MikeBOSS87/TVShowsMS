package com.tv.maze.infrastructure.adapter.outbound.persistence.mongo;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.tv.maze.domain.model.ShowDetail;
import com.tv.maze.domain.port.outbound.TvShowRepositoryPort;
import com.tv.maze.infrastructure.adapter.outbound.persistence.mongo.document.ShowDocument;
import com.tv.maze.infrastructure.adapter.outbound.persistence.mongo.repository.SpringDataMongoShowRepository;

@Component
public class TvShowMongoAdapter implements TvShowRepositoryPort{
	
	private final SpringDataMongoShowRepository repository;

    public TvShowMongoAdapter( SpringDataMongoShowRepository repository ){
        this.repository = repository;
    }

    @Override
    public Optional< ShowDetail > findById( Long showId ) {
        return repository.findById( showId ).map(doc -> new ShowDetail(
                doc.getId(),
                doc.getUrl(),
                doc.getName(),
                doc.getType(),
                doc.getLanguage(),
                doc.getGenres(),
                doc.getStatus(),
                doc.getRuntime(),
                doc.getPremiered(),
                doc.getOfficialSite(),
                doc.getSummary(),
                doc.getAdditionalAttributes()
        ));
    }

    @Override
    public ShowDetail save( ShowDetail detail ) {
        ShowDocument doc = new ShowDocument(
                detail.id(),
                detail.url(),
                detail.name(),
                detail.type(),
                detail.language(),
                detail.genres(),
                detail.status(),
                detail.runtime(),
                detail.premiered(),
                detail.officialSite(),
                detail.summary(),
                detail.additionalAttributes()
        );
        repository.save(doc);
        return detail;
    }
}