package com.learnreactivespring.controller;

import java.util.List;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureWebTestClient
@DirtiesContext
public class FluxAndMonoControllerTest {

	@Autowired
	WebTestClient webTestClient;

	@Test
	public void flux_approach1() {
		Flux<Integer> integerFlux = webTestClient.get().uri("/flux").accept(MediaType.APPLICATION_JSON_UTF8).exchange()
				.expectStatus().isOk().returnResult(Integer.class).getResponseBody();

		StepVerifier.create(integerFlux).expectSubscription().expectNext(1).expectNext(2).expectNext(3).expectNext(4)
				.verifyComplete();
	}

//	@Test
//	public void flux_approach2() {
//		webTestClient.get().uri("/flux").accept(MediaType.APPLICATION_JSON_UTF8).exchange().expectStatus().isOk()
//				.expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8).expectBodyList(Integer.class).hasSize(5);
//
//	}

	@Test
	public void flux_approach3() {
		List<Integer> expectedIntegerList = Arrays.asList(1, 2, 3, 4);
		webTestClient.get().uri("/flux").accept(MediaType.APPLICATION_JSON_UTF8).exchange().expectStatus().isOk()
				.expectBodyList(Integer.class).consumeWith((response) -> {
					assertEquals(expectedIntegerList, response.getResponseBody());
				});

	}

	@Test
	public void fluxInfiniteStream() {
		Flux<Long> longInfiniteStreamFlux = webTestClient.get().uri("/fluxinfinitestream")
				.accept(MediaType.APPLICATION_STREAM_JSON).exchange().expectStatus().isOk().returnResult(Long.class)
				.getResponseBody();

		StepVerifier.create(longInfiniteStreamFlux).expectNext(0l).expectNext(1l).expectNext(2l).thenCancel().verify();
	}

	@Test
	public void mono() {
		Integer expectedValue = new Integer(1);
		webTestClient.get().uri("/mono").accept(MediaType.APPLICATION_JSON_UTF8).exchange().expectStatus().isOk()
				.expectBody(Integer.class).consumeWith((response) -> {
					assertEquals(expectedValue, response.getResponseBody());
				});
	}

}
