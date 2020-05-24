package com.learnreactivespring.fluxandmonoplayground;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import org.junit.Test;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class FluxAndMonoFactorytest {
	List<String> names = Arrays.asList("adam", "anna", "jack", "jenny");

	@Test
	public void fluxUsingIterable() {
		Flux<String> namesFlux = Flux.fromIterable(names);
		StepVerifier.create(namesFlux.log()).expectNext("adam", "anna", "jack", "jenny").verifyComplete();
	}

	@Test
	public void fluxUsingArray() {
		String[] names = new String[] { "adam", "anna", "jack", "jenny" };
		Flux<String> namesFlux = Flux.fromArray(names);
		StepVerifier.create(namesFlux.log()).expectNext("adam", "anna", "jack", "jenny").verifyComplete();
	}

	@Test
	public void fluxUsingStream() {
		Flux<String> namesFlux = Flux.fromStream(names.stream());
		StepVerifier.create(namesFlux.log()).expectNext("adam", "anna", "jack", "jenny").verifyComplete();

	}

	@Test
	public void monoUsingJustOrEmpty() {
		Mono<String> mono = Mono.justOrEmpty(null);
		StepVerifier.create(mono.log()).verifyComplete();
	}

	@Test
	public void monoUsingSupplier() {
		Supplier<String> stringSuppier = () -> "adam";
		Mono<String> monoString = Mono.fromSupplier(stringSuppier);
		System.out.println(stringSuppier.get());
		StepVerifier.create(monoString.log()).expectNext("adam").verifyComplete();

	}

	@Test
	public void fluxUsingRange() {
		Flux<Integer> integerFlux = Flux.range(1, 5).log();
		StepVerifier.create(integerFlux).expectNext(1, 2, 3, 4, 5).verifyComplete();
	}

}
