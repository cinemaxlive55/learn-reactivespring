package com.learnreactivespring.fluxandmonoplayground;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

public class FluxAndMonoTransformtest {
	List<String> names = Arrays.asList("adam", "anna", "jack", "jenny");

	@Test
	public void transformUsingMap() {
		Flux<String> fluxString = Flux.fromIterable(names).map(s -> s.toUpperCase()).log();
		StepVerifier.create(fluxString).expectNext("ADAM", "ANNA", "JACK", "JENNY").verifyComplete();
	}

	@Test
	public void transformUsingFlatMap() {
		Flux<String> stringFlux = Flux.fromIterable(Arrays.asList("A", "B", "C", "D", "E", "F")).flatMap(s -> {
			return Flux.fromIterable(convertToList(s));
		}).log();

		StepVerifier.create(stringFlux).expectNextCount(12).verifyComplete();

	}

	private List<String> convertToList(String s) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();

		}
		return Arrays.asList(s, "newValue");

	}

	@Test
	public void transformUsingFlatMap_usingparallel() {
		Flux<String> fluxString = Flux.fromIterable(Arrays.asList("A", "B", "C", "D", "E", "F")).window(2)
				.flatMap((s) -> s.map(this::convertToList).subscribeOn(Schedulers.parallel())).flatMap(s -> {
					return Flux.fromIterable(s);
				}).log();

		StepVerifier.create(fluxString).expectNextCount(12).verifyComplete();
	}

	@Test
	public void transformUsingFlatMap_usingparallel_maintain_order() {
		Flux<String> fluxString = Flux.fromIterable(Arrays.asList("A", "B", "C", "D", "E", "F")).window(2)
				.flatMapSequential((s) -> s.map(this::convertToList).subscribeOn(Schedulers.parallel())).flatMap(s -> {
					return Flux.fromIterable(s);
				}).log();

		StepVerifier.create(fluxString).expectNextCount(12).verifyComplete();
	}

	@Test
	public void combineUsingZip() {
		Flux<String> flux1 = Flux.just("A", "B", "C");
		Flux<String> flux2 = Flux.just("D", "E", "F");

		Flux<String> mergedFlux = Flux.zip(flux1, flux2, (t1, t2) -> {
			return t1.concat(t2);
		});

		StepVerifier.create(mergedFlux.log()).expectSubscription().expectNext("AD", "BE", "CF").verifyComplete();

	}

}
