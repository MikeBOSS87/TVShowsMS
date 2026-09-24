package com.tv.maze.domain.port.outbound;

import com.tv.maze.domain.model.Comment;

public interface CommentRepositoryPort {
	
	void save( Comment comment );
}