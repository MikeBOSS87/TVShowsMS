package com.tv.maze.infrastructure.adapter.outbound.persistence.mongo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.tv.maze.infrastructure.adapter.outbound.persistence.mongo.document.ShowDocument;

public interface SpringDataMongoShowRepository extends MongoRepository< ShowDocument, Long >{}