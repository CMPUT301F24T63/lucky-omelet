package com.example.eventlotterysystem;

public class Notification {
    // Attributes
    private Event event;
    private User user;
    private Boolean needAccept;
    private Boolean isAccepted;
    private Boolean isDeclined;
    private String customMessage;

    // Constructor
    public Notification(Event event, User user, Boolean needAccept, String customMessage) {
        this.event = event;
        this.user = user;
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

    public User getUser() {
        return user;
    }



    // Functions
    public void accept(){
        if (this.needAccept) {isAccepted = true;}
        event.getFinalList().add(user);
        // Do not remove from waiting list (otherwise more users will be invited if click "Reroll")

    }

    public void decline() {
        if (this.needAccept) {isDeclined = false;}
        event.getChosenList().remove(user);
        event.getCancelledList().add(user);
    }

    public boolean isResponded() {
        return isAccepted || isDeclined;
    }


}
