package com.data;

public class LatencyPayload {

    int eventId;
    long timestamp;

    public LatencyPayload(int eventId) {
        this.eventId = eventId;
        this.timestamp = System.nanoTime();
    }

    public int getEventId() {
        return eventId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String toString()
    {
        return "eventId: " + eventId + ", " +
                "timestamp: " + timestamp;
    }
}