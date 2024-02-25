package com.data;

public class Dog {

    int eventId;
    long timestamp;
    String name;
    String sound;

    public Dog(int eventId, String name) {
        this.eventId = eventId;
        this.timestamp = System.nanoTime();
        this.name = name;
        sound = "Wuff!";
    }

    public int getEventId() {
        return eventId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getName() { return name; }

    public String getSound() { return sound; }

    public String toString()
    {
        return "eventId: " + eventId + ", " +
                "timestamp: " + timestamp + ", " +
                "name: " + name + ", " +
                "sound: " + sound;
    }
}