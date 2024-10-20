package com.example.eventlotterysystem;

public class Notification {
    // Attributes
    private Event event;
    private Boolean needAccept;
    private Boolean isAccepted;
    private Boolean isDeclined;
    private String customMessage;

    // Constructor
    public Notification(Event event, Boolean needAccept, String customMessage) {
        this.event = event;
        this.needAccept = needAccept;
        this.isAccepted = false; // On the notification creation stage, no one has accepted yet
        this.isDeclined = false; // On the notification creation stage, no one has declined yet
        this.customMessage = customMessage;
    }

    // Getters

    public Event getEvent() {
        return event;
    }

    public Boolean needAccept() {
        return needAccept;
    }

    public Boolean getIsAccepted() {
        return isAccepted;
    }

    public Boolean getIsDeclined() {
        return isDeclined;
    }

    public String getCustomMessage() {
        return customMessage;
    }

    // Functions
    public void accept(){
        if (this.needAccept) {isAccepted = true;}
    }

    public void decline() {isDeclined = false;}

    public boolean isResponded() {
        return isAccepted || isDeclined;
    }


}
