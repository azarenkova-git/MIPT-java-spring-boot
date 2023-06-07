package com.example.mipt.services;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class SseService {
    List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public SseEmitter init() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

        emitter.onCompletion(() -> {
            emitters.remove(emitter);
        });

        emitter.onTimeout(() -> {
            emitter.complete();
            emitters.remove(emitter);
        });

        emitters.add(emitter);

        return emitter;
    }

    public void broadcast(SseEmitter.SseEventBuilder event) {
        List<SseEmitter> failures = new ArrayList<>();

        System.out.println("Broadcasting event: " + event);

        emitters.forEach(emitter -> {
            try {
                emitter.send(event);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                emitter.completeWithError(ex);
                failures.add(emitter);
            }
        });

        if (!failures.isEmpty()) {
            emitters.removeAll(failures);
        }
    }
}
