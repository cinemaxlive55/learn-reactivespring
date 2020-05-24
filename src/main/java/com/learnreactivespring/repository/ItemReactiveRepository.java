package com.learnreactivespring.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.learnreactivespring.document.Item;

import reactor.core.publisher.Mono;

public interface ItemReactiveRepository extends ReactiveMongoRepository<Item, String> {
	
	Mono<Item> findByDescription(String description);

}
