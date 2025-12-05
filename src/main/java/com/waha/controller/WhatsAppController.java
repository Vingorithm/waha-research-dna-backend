package com.waha.controller;

import java.time.Duration;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.waha.client.WahaClient;
import com.waha.dto.SendMessageRequest;
import com.waha.dto.StatusTextRequest;

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

    @GetMapping(value = "/session/{sessionId}/qr", produces = MediaType.IMAGE_PNG_VALUE)
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

        return wahaClient.sendSeen(chatId)
                .then(wahaClient.startTyping(chatId))
                // delay non-blocking, kira-kira 3 detik mengetik
                .then(Mono.delay(Duration.ofSeconds(3)))
                .then(wahaClient.stopTyping(chatId))
                // terakhir baru benar-benar kirim pesan
                .then(wahaClient.sendText(chatId, req.getMessage()))
                .onErrorResume(e -> {
                    System.out.println("Gagal sendText: " + e.getMessage());
                    return Mono.error(e);
                });
    }

    // @PostMapping("/message/sendSeen")
    // public Mono<String> sendSeen(@RequestBody SendMessageRequest req) {
    // if (req.getPhone() == null) {
    // return Mono.error(new IllegalArgumentException("phone wajib diisi"));
    // }

    // String chatId = req.getPhone() + "@c.us";

    // return wahaClient.sendSeen(chatId);
    // }

    // @PostMapping("/message/startTyping")
    // public Mono<String> startTyping(@RequestBody SendMessageRequest req) {
    // if (req.getPhone() == null) {
    // return Mono.error(new IllegalArgumentException("phone wajib diisi"));
    // }

    // String chatId = req.getPhone() + "@c.us";

    // return wahaClient.startTyping(chatId);
    // }

    // @PostMapping("/message/stopTyping")
    // public Mono<String> stopTyping(@RequestBody SendMessageRequest req) {
    // if (req.getPhone() == null) {
    // return Mono.error(new IllegalArgumentException("phone wajib diisi"));
    // }

    // String chatId = req.getPhone() + "@c.us";

    // return wahaClient.stopTyping(chatId);
    // }

    // ==========================
    // SEND STATUS TEXT
    // ==========================

    @PostMapping("/{session}/status/text")
    public Mono<String> sendStatusText(
            @PathVariable String session,
            @RequestBody StatusTextRequest request) {
        return wahaClient.sendStatusText(session, request);
    }

    @GetMapping("/test")
    public Mono<String> test() {
        return Mono.just("Test endpoint is working!");
    }
}
