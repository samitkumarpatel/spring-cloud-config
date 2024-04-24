package com.example.configclientone;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.endpoint.RefreshEndpoint;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@SpringBootApplication
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class ConfigClientOneApplication {
	final RefreshEndpoint refreshEndpoint;

	public static void main(String[] args) {
		SpringApplication.run(ConfigClientOneApplication.class, args);
	}

	@Scheduled(fixedRate = 10000)
	private void scheduled() {
		refreshEndpoint.refresh().forEach(d -> log.info("refreshed: {}", d));
	}

}

@RestController
class Routers {

	@Value("${message}")
	private String message;

	@GetMapping
	public ResponseEntity<Map<String,String>> message(@Value("${message}") String messageX) {
		return ResponseEntity.ok(
				Map.of("message", message, "messageX", messageX)
		);
	}
}