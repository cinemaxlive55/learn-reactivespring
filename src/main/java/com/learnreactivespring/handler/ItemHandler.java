package com.learnreactivespring.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.learnreactivespring.document.Item;
import com.learnreactivespring.repository.ItemReactiveRepository;

import reactor.core.publisher.Mono;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;

@Component
public class ItemHandler {

	@Autowired
	ItemReactiveRepository itemReactiveRepository;

	static Mono<ServerResponse> notFound = ServerResponse.notFound().build();

	public Mono<ServerResponse> getAllItems(ServerRequest serverRequest) {
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(itemReactiveRepository.findAll(),
				Item.class);
	}

	public Mono<ServerResponse> getOneItem(ServerRequest serverRequest) {

		String id = serverRequest.pathVariable("id");
		Mono<Item> itemMono = itemReactiveRepository.findById(id);

		return itemMono
				.flatMap(item -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(fromObject(item)))
				.switchIfEmpty(notFound);

	}

	public Mono<ServerResponse> createItem(ServerRequest serverRequest) {

		Mono<Item> insertItem = serverRequest.bodyToMono(Item.class);
		return insertItem.flatMap(item -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
				.body(itemReactiveRepository.save(item), Item.class));

	}

	public Mono<ServerResponse> deleteItem(ServerRequest serverRequest) {
		String id = serverRequest.pathVariable("id");
		Mono<Void> deleteData = itemReactiveRepository.deleteById(id);

		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(deleteData, Void.class);
	}

	public Mono<ServerResponse> updateItem(ServerRequest serverRequest) {
		String id = serverRequest.pathVariable("id");
		Mono<Item> updatedItem = serverRequest.bodyToMono(Item.class).flatMap((item) -> {
			Mono<Item> itemMono = itemReactiveRepository.findById(id).flatMap(currentItem -> {
				currentItem.setDescription(item.getDescription());
				currentItem.setPrice(item.getPrice());
				return itemReactiveRepository.save(currentItem);
			});
			return itemMono;
		});
		return updatedItem
				.flatMap(item -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(fromObject(item)))
				.switchIfEmpty(notFound);

	}

}
