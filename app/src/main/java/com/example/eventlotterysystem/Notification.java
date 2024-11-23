/**
 * The {@code Notification} class represents a notification related to an event in the event
 * lottery system. Each notification informs a {@code User} about an {@code Event}, and may
 * require acceptance or response from the user.
 */
package com.example.eventlotterysystem;

public class Notification {
    // Attributes
    private Event event; // The event associated with this notification
    private User user; // The user to whom the notification is directed
    private Boolean needAccept; // Indicates if user response is required
    private Boolean isAccepted; // Indicates if the user has accepted the notification
    private Boolean isDeclined; // Indicates if the user has declined the notification
    private String customMessage; // Custom message included in the notification
    private int eventRef;
    private int userRef;

    // Constructor

    /**
     * Constructs a new {@code Notification} instance with the specified event, user,
     * response requirement, and custom message.
     *
     * @param event the event associated with this notification
     * @param user the user who receives this notification
     * @param needAccept {@code true} if the notification requires user response; otherwise {@code false}
     * @param customMessage a custom message for the notification
     */
    public Notification(Event event, User user, Boolean needAccept, String customMessage) {
        this.event = event;
        this.user = user;
        this.needAccept = needAccept;
        this.isAccepted = false; // On the notification creation stage, no one has accepted yet
        this.isDeclined = false; // On the notification creation stage, no one has declined yet
        this.customMessage = customMessage;
    }

    public Notification(int eventRef, int userRef, Boolean needAccept, String customMessage, boolean isAccepted, boolean isDeclined) {
        this.event = null;
        this.user = null;
        this.needAccept = needAccept;
        this.isAccepted = isAccepted; // On the notification creation stage, no one has accepted yet
        this.isDeclined = isDeclined; // On the notification creation stage, no one has declined yet
        this.customMessage = customMessage;
        this.userRef = userRef;
        this.eventRef = eventRef;
    }

    // Getters

    /**
     * Retrieves the event associated with this notification.
     *
     * @return the event associated with this notification
     */
    public Event getEvent() {return event;}

    /**
     * Checks if the notification requires acceptance from the user.
     *
     * @return {@code true} if acceptance is required; {@code false} otherwise
     */
    public Boolean needAccept() {return needAccept;}

    /**
     * Checks if the notification has been accepted by the user.
     *
     * @return {@code true} if accepted; {@code false} otherwise
     */
    public Boolean getIsAccepted() {return isAccepted;}

    /**
     * Checks if the notification has been declined by the user.
     *
     * @return {@code true} if declined; {@code false} otherwise
     */
    public Boolean getIsDeclined() {return isDeclined;}

    /**
     * Retrieves the custom message associated with this notification.
     *
     * @return the custom message for this notification
     */
    public String getCustomMessage() {return customMessage;}

    /**
     * Retrieves the user associated with this notification.
     *
     * @return the user associated with this notification
     */
    public User getUser() {
        return user;
    }

    //Setters

    /**
     * Sets the acceptance status of the notification.
     *
     * @param accepted the new custom message
     */
    public void setAccepted(Boolean accepted) {
        isAccepted = accepted;
    }

    /**
     * Sets the rejection status of the notification.
     *
     * @param declined the new custom message
     */
    public void setDeclined(Boolean declined) {
        isDeclined = declined;
    }

    // Functions

    /**
     * Accepts the notification if acceptance is required. Adds the user to the event's final list.
     */
    public void accept(){
        if (this.needAccept) {
            isAccepted = true;
            needAccept = false;
        }
        event.getChosenList().remove(user);
        event.getFinalList().add(user);
        // Do not remove from waiting list (otherwise more users will be invited if click "Reroll")
    }

    /**
     * Declines the notification if acceptance is required. Removes the user from the event's chosen list and
     * adds them to the event's cancelled list.
     */
    public void decline() {
        if (this.needAccept) {
            isDeclined = true;
            needAccept = false;
        }
        event.getChosenList().remove(user);
        event.getCancelledList().add(user);
    }

    /**
     * Checks if the notification has been responded to, either accepted or declined.
     *
     * @return {@code true} if the notification has been responded to; otherwise {@code false}
     */
    public boolean isResponded() {return isAccepted || isDeclined;}


}
