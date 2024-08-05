package com.therainbowville.minegasm.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EventProcessor {
    private static final Map<String, EventData> activeEvents = new ConcurrentHashMap<>();

    public void startEvent(String playerName, String eventType, int duration, int intensity) {
        activeEvents.put(playerName, new EventData(eventType, duration, intensity));
    }

    public void processEvents() {
        activeEvents.forEach((playerName, eventData) -> {
            if (eventData.duration > 0) {
                // TODO: Implement event processing logic

                eventData.duration--;
            } else {
                activeEvents.remove(playerName);
            }
        });
    }

    private static class EventData {
        private final String eventType;
        private int duration;
        private int intensity;

        public EventData(String eventType, int duration, int intensity) {
            this.eventType = eventType;
            this.duration = duration;
            this.intensity = intensity;
        }
    }
}