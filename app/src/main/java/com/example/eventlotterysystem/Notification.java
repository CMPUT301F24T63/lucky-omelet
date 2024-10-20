package com.example.eventlotterysystem;

public class Notification {
    // Attributes
    private Event event;
    private Boolean needAccept;
    private Boolean isAccepted;
    private String customMessage;

    // Constructor
    public Notification(Event event, Boolean needAccept, String customMessage) {
        this.event = event;
        this.needAccept = needAccept;
        this.isAccepted = false; // At the notification creation stage, no one has accepted yet
        this.customMessage = customMessage;
    }

    // Getters

    public Event getEvent() {
        return event;
    }

    public Boolean needAccept() {
        return needAccept;
    }

    public Boolean isAccepted() {
        return isAccepted;
    }

    public String getCustomMessage() {
        return customMessage;
    }

    // Functions



}
