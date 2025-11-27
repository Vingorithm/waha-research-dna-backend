package com.waha.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class WahaClient {

    private final WebClient webClient;

    public WahaClient(
            @Value("${waha.base-url}") String baseUrl,
            @Value("${waha.api-key}") String apiKey
    ) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("X-Api-Key", apiKey) // penting!
                .build();
    }

    /**
     * Start session WAHA (POST /api/sessions)
     */
    public Mono<String> startSession(String sessionName) {
        return webClient.post()
                .uri("/api/sessions")
                .bodyValue(Map.of("name", sessionName))
                .retrieve()
                .bodyToMono(String.class);
    }

    /**
     * Kirim pesan text (POST /api/sendText)
     */
    public Mono<String> sendText(String sessionName, String chatId, String text) {
        return webClient.post()
                .uri("/api/sendText")
                .bodyValue(Map.of(
                        "session", sessionName,
                        "chatId", chatId,
                        "text", text
                ))
                .retrieve()
                .bodyToMono(String.class);
    }

    /**
     * Ambil screenshot (QR / tampilan WA) (GET /api/screenshot?session=...)
     * Kita balikan sebagai Base64 string ke FE.
     */
    public Mono<String> getScreenshotBase64(String sessionName) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/screenshot")
                        .queryParam("session", sessionName)
                        .build())
                .retrieve()
                .bodyToMono(byte[].class)
                .map(bytes -> java.util.Base64.getEncoder().encodeToString(bytes));
    }
}
