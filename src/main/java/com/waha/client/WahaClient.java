package com.waha.client;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.waha.dto.StatusTextRequest;

import reactor.core.publisher.Mono;

@Service
public class WahaClient {

    private final WebClient webClient;

    public WahaClient(
            @Value("${waha.base-url}") String baseUrl,
            @Value("${waha.api-key}") String apiKey
    ) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("X-Api-Key", apiKey)
                .build();
    }

    /**
     * START
     */
        public Mono<String> startSession() {
        return webClient.post()
                .uri("/api/sessions/{session}/start", "default")
                .retrieve()
                .bodyToMono(String.class);
        }

    /**
     * LOGOUT nomor dari WA (disconnect)
     */
        public Mono<String> stopSession() {
        return webClient.post()
                .uri("/api/sessions/{session}/stop", "default")
                .retrieve()
                .bodyToMono(String.class);
        }

    /**
     * LOGOUT nomor dari WA (disconnect)
     */
        public Mono<String> logoutSession() {
        return webClient.post()
                .uri("/api/sessions/{session}/logout", "default")
                .retrieve()
                .bodyToMono(String.class);
        }

    /**
     * CHECK status session
     */
    public Mono<String> getStatus() {
        return webClient.get()
                .uri("/api/sessions")
                .retrieve()
                .bodyToMono(String.class);
    }

    /**
     * SEND MESSAGE
     */
    public Mono<String> sendText(String chatId, String text) {
        return webClient.post()
                .uri("/api/sendText")
                .bodyValue(Map.of(
                        "session", "default",
                        "chatId", chatId,
                        "text", text
                ))
                .retrieve()
                .bodyToMono(String.class);
    }

    /**
     * QR RAW Bytes (untuk image/png)
     */
    public Mono<byte[]> getQrBytes(String sessionName) {
        return webClient.get()
                .uri("/api/{session}/auth/qr", sessionName)
                .retrieve()
                .bodyToMono(byte[].class);
    }
    
    /*
     *  Send Status Text 
     */
    public Mono<String> sendStatusText(String session, StatusTextRequest req) {
        return webClient.post()
                .uri("/api/" + session + "/status/text")
                .bodyValue(req)
                .retrieve()
                .bodyToMono(String.class);
    }
}
