package com.learnreactivespring.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.learnreactivespring.handler.ItemHandler;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

import static com.learnreactivespring.constants.ItemConstants.ITEM_FUNCTIONAL_END_POINT_V1;

@Configuration
public class ItemsRouter {

	@Bean
	public RouterFunction<ServerResponse> itemRoute(ItemHandler itemHandler) {
		return RouterFunctions
				.route(GET(ITEM_FUNCTIONAL_END_POINT_V1).and(accept(MediaType.APPLICATION_JSON)),
						itemHandler::getAllItems)
				.andRoute(GET(ITEM_FUNCTIONAL_END_POINT_V1 + "/{id}").and(accept(MediaType.APPLICATION_JSON)),
						itemHandler::getOneItem)
				.andRoute(POST(ITEM_FUNCTIONAL_END_POINT_V1).and(accept(MediaType.APPLICATION_JSON)),
						itemHandler::createItem)
				.andRoute(DELETE(ITEM_FUNCTIONAL_END_POINT_V1 + "/{id}").and(accept(MediaType.APPLICATION_JSON)),
						itemHandler::deleteItem)
				.andRoute(PUT(ITEM_FUNCTIONAL_END_POINT_V1 + "/{id}").and(accept(MediaType.APPLICATION_JSON)),
						itemHandler::updateItem);
	}

}
