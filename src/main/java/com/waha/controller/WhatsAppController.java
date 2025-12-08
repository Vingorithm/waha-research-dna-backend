package com.waha.controller;

import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.waha.client.WahaClient;
import com.waha.dto.SendMessageRequest;
import com.waha.dto.StatusTextRequest;
import com.waha.event.WebhookBroadcaster;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/wa")
public class WhatsAppController {

    private final WahaClient wahaClient;

    public WhatsAppController(WahaClient wahaClient) {
        this.wahaClient = wahaClient;
    }

    // ==========================
    // SESSION
    // ==========================
    @PostMapping("/session/start")
    public String startSession() {
        return wahaClient.startSession().block();
    }

    @PostMapping("/session/stop")
    public String stopSession() {
        return wahaClient.stopSession().block();
    }

    @PostMapping("/session/logout")
    public String logout() {
        return wahaClient.logoutSession().block();
    }

    @GetMapping("/session/status")
    public String getStatus() {
        return wahaClient.getStatus().block();
    }

    // ==========================
    // QR CODE
    // ==========================
    @GetMapping(value = "/session/{sessionId}/qr", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getQr(@PathVariable String sessionId) {
        return wahaClient.getQrBytes(sessionId).block();
    }

    // ==========================
    // SEND TEXT
    // ==========================
    @PostMapping("/message/send")
    public String sendMessage(@RequestBody SendMessageRequest req) {
        String chatId = req.getPhone() + "@c.us";
        return wahaClient.sendText(chatId, req.getMessage()).block();
    }

    // ==========================
    // STATUS TEXT
    // ==========================
    @PostMapping("/{session}/status/text")
    public String sendStatusText(
            @PathVariable String session,
            @RequestBody StatusTextRequest request) {

        return wahaClient.sendStatusText(session, request).block();
    }

    // ==========================
    // TEST
    // ==========================
    @GetMapping("/test")
    public String test() {
        return "Test endpoint OK";
    }

    // ==========================
    // WEBHOOK (WAHA → BACKEND → SSE)
    // ==========================
    @PostMapping("/webhook")
    public ResponseEntity<String> webhook(@RequestBody Map<String, Object> body) {

        String event = (String) body.get("event");
        Object data = body.get("payload");

        WebhookBroadcaster.broadcast(event, data);

        return ResponseEntity.ok("OK");
    }

    // ==========================
    // SSE STREAM (BACKEND → ANGULAR)
    // ==========================
    @GetMapping(value = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter events() {
        return WebhookBroadcaster.registerClient();
    }
}
