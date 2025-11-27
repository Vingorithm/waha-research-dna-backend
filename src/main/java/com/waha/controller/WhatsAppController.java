package com.waha.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.waha.client.WahaClient;
import com.waha.dto.SendMessageRequest;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/wa")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class WhatsAppController {

    private final WahaClient wahaClient = null;

    @PostMapping("/session/start/{sessionId}")
    public Mono<String> startSession(@PathVariable String sessionId) {
        return wahaClient.startSession(sessionId);
    }

    @GetMapping("/session/qr/{sessionId}")
    public Mono<String> getSessionQr(@PathVariable String sessionId) {
        // kita balikan base64 image
        return wahaClient.getScreenshotBase64(sessionId);
    }

    @PostMapping("/message/send")
    public Mono<String> sendMessage(@RequestBody SendMessageRequest request) {
        // Konversi phone → chatId
        // Misal input: 6281234567890 → 6281234567890@c.us
        String chatId = request.getPhone() + "@c.us";
        return wahaClient.sendText(
                request.getSessionId(),
                chatId,
                request.getMessage()
        );
    }
}
