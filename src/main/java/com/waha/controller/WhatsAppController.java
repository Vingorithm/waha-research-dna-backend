package com.waha.controller;

import com.waha.client.WahaClient;
import com.waha.dto.SendMessageRequest;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/wa")
public class WhatsAppController {

    private final WahaClient wahaClient;

    public WhatsAppController(WahaClient wahaClient) {
        this.wahaClient = wahaClient;
    }
    
    // ==========================
    // SESSION MANAGEMENT
    // ==========================

    @PostMapping("/session/start")
    public Mono<String> startSession() {
        return wahaClient.startSession();
    }

    @PostMapping("/session/stop")
    public Mono<String> stopSession() {
        return wahaClient.stopSession();
    }

    @PostMapping("/session/logout")
    public Mono<String> logout() {
        return wahaClient.logoutSession();
    }

    @GetMapping("/session/status")
    public Mono<String> getStatus() {
        return wahaClient.getStatus();
    }

    // ==========================
    // QR CODE
    // ==========================

    @GetMapping(
            value = "/session/{sessionId}/qr",
            produces = MediaType.IMAGE_PNG_VALUE
    )
    public Mono<byte[]> getQr(@PathVariable String sessionId) {
        return wahaClient.getQrBytes(sessionId);
    }

    // ==========================
    // SEND MESSAGE
    // ==========================

    @PostMapping("/message/send")
    public Mono<String> sendMessage(@RequestBody SendMessageRequest req) {

        if (req.getPhone() == null || req.getMessage() == null) {
            return Mono.error(new IllegalArgumentException("phone dan message wajib diisi"));
        }

        String chatId = req.getPhone() + "@c.us";

        return wahaClient.sendText(chatId, req.getMessage());
    }

    @GetMapping("/test")
    public Mono<String> test() {
        return Mono.just("Test endpoint is working!");
    }
}
