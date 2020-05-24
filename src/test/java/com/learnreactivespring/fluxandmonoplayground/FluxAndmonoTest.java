package com.learnreactivespring.fluxandmonoplayground;

import org.junit.Test;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class FluxAndmonoTest {
	@Test
	public void fluxTest() {
		Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring Boot")
				.concatWith(Flux.error(new RuntimeException("Exception Occurred"))).log();
		stringFlux.subscribe(System.out::println, (e) -> System.err.println(e));

	}

	@Test
	public void fluxTestElements_WithoutError() {
		Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring Boot").log();
		StepVerifier.create(stringFlux).expectNext("Spring").expectNext("Spring Boot")
				.expectNext("Reactive Spring Boot").verifyComplete();
	}

	@Test
	public void fluxTestElements_WithError() {
		Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring Boot")
				.concatWith(Flux.error(new RuntimeException("Exception Occurred"))).log();
		StepVerifier.create(stringFlux).expectNext("Spring").expectNext("Spring Boot")
				.expectNext("Reactive Spring Boot").expectErrorMessage("Exception Occurred").verify();
	}

	@Test
	public void monoTest() {
		Mono<String> stringMono = Mono.just("Spring");
		StepVerifier.create(stringMono.log()).expectNext("Spring").verifyComplete();

	}

}
