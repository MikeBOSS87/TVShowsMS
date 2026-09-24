package com.tv.maze.domain.port.inbound;

public interface AddCommentUseCase {
	
    void addComment( Long showId, String comment, Integer rating );
}