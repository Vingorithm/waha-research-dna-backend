package com.waha.event;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class WebhookBroadcaster {

    private static final Set<SseEmitter> clients = new CopyOnWriteArraySet<>();

    public static SseEmitter registerClient() {
        SseEmitter emitter = new SseEmitter(0L); // no timeout
        clients.add(emitter);

        emitter.onCompletion(() -> clients.remove(emitter));
        emitter.onTimeout(() -> clients.remove(emitter));
        emitter.onError(e -> clients.remove(emitter));

        return emitter;
    }

    public static void broadcast(String event, Object data) {
        clients.forEach(emitter -> {
            try {
                emitter.send(
                    SseEmitter.event()
                        .name(event)
                        .data(data)
                );
            } catch (IOException | IllegalStateException e) {
                // Connection already closed atau error kirim,
                // pastikan emitter di-close dan di-remove dari set.
                emitter.complete();
                clients.remove(emitter);
            }
        });
    }
}
