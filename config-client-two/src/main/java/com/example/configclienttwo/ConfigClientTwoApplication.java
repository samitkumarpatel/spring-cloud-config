package com.example.configclienttwo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.endpoint.RefreshEndpoint;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Objects;

@SpringBootApplication
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class ConfigClientTwoApplication {
	final RefreshEndpoint refreshEndpoint;

	public static void main(String[] args) {
		SpringApplication.run(ConfigClientTwoApplication.class, args);
	}

	@Scheduled(fixedRate = 5000)
	public void refresh() {
		refreshEndpoint.refresh().forEach(d -> log.info("Refreshed: {}", d));
	}

	@Value("${message}")
	private String message;

	@Bean
	RouterFunction<ServerResponse> routes() {
		return RouterFunctions
				.route()
				.GET("/", this::justGet)
				.build();
	}

	final Services service;
	private Mono<ServerResponse> justGet(ServerRequest request) {
		return service
				.getMsg()
				.flatMap(ServerResponse.ok()::bodyValue);
	}

}

@Service
@RequiredArgsConstructor
class Services {
	@Value("${message}")
	String message;
	final Environment environment;
	public Mono<Map<String, String>> getMsg() {
		return Mono.fromCallable(() -> Map.of("message", message, "messageX", Objects.requireNonNull(environment.getProperty("message"))));
	}
}