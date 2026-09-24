package com.tv.maze.infrastructure.adapter.outbound.persistence.mongo;

import org.springframework.stereotype.Component;

import com.tv.maze.domain.model.Comment;
import com.tv.maze.domain.port.outbound.CommentRepositoryPort;
import com.tv.maze.infrastructure.adapter.outbound.persistence.mongo.document.CommentDocument;
import com.tv.maze.infrastructure.adapter.outbound.persistence.mongo.repository.SpringDataMongoCommentRepository;

@Component
public class CommentMongoAdapter implements CommentRepositoryPort {
	
	private final SpringDataMongoCommentRepository repository;

    public CommentMongoAdapter( SpringDataMongoCommentRepository repository ){
        this.repository = repository;
    }

	@Override
	public void save( Comment comment ) {
		CommentDocument doc = new CommentDocument(
                comment.showId(),
                comment.comment(),
                comment.rating(),
                comment.createdAt()
        );
        repository.save( doc );
	}
}