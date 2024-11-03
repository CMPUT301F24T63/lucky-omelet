/**
 * The {@code User} class represents a user in the event lottery system. All the user operations
 * from the user interface (except checkDevice) are implemented here.
 *
 * After checking device, the app should operate as the user Control.getCurrentUser().
 *
 * Determine the role of the user:
 *  1. facility == null -> Entrant
 *  2. facility != null -> Organizer
 *  3. isAdmin == true -> Admin
 */

package com.example.eventlotterysystem;

import java.util.ArrayList;
import java.util.List;

public class User {

    // Attributes

    private int userID; // Unique identifier for the user
    private String name; // Name of the user
    private String email; // Email address of the user
    private String contact; // Contact information of the user
    private Picture picture; // User's profile picture, stored as a Picture object
    private Facility facility; // Facility managed by the user
    private boolean isAdmin; // Specifies if the user has administrative privileges
    private ArrayList<Notification> notificationList; // List of notifications for the user
    private ArrayList<Event> enrolledList; // List of events the user is enrolled in
    private ArrayList<Event> organizedList; // List of events organized by the user
    private Boolean notificationSetting; // Notification settings for the user

    // Constructor

    /**
     * Constructs a new {@code User} with the specified details. This constructor should be used
     * when reading data from the database, or creating mock data.
     *
     * @param userID   the unique identifier of the user
     * @param name     the name of the user
     * @param email    the email of the user
     * @param contact  the contact information of the user
     * @param isAdmin  specifies if the user is an admin
     */

    public User(int userID, String name, String email, String contact, Boolean isAdmin) {
        this.userID = userID;
        this.name = name;
        this.email = email;
        this.contact = contact;
        this.picture = null; // A user has no picture at creation
        this.facility = null; // A user has no facility at creation
        this.isAdmin = isAdmin;
        this.notificationList = new ArrayList<>();
        this.enrolledList = new ArrayList<>();
        this.organizedList = new ArrayList<>();
        this.notificationSetting = true;
    }

    /**
     * Constructs a new {@code User} with the specified details. This constructor should be used
     * when a new user register the app.
     *
     * @param userID   the unique identifier of the user
     */

    public User(int userID) {
        // Default values except userID
        this.userID = userID;
        this.name = "Default Name";
        this.email = "user@example.com";
        this.contact = "000-000-0000";
        this.picture = null;
        this.facility = null;
        this.isAdmin = false;
        this.notificationList = new ArrayList<>();
        this.enrolledList = new ArrayList<>();
        this.organizedList = new ArrayList<>();
        this.notificationSetting = true;
    }

    // Getters

    /** @return the unique identifier of the user. */
    public int getUserID() { return userID; }

    /** @return the name of the user. */
    public String getName() { return name; }

    /** @return the email of the user. */
    public String getEmail() { return email; }

    /** @return the contact information of the user. */
    public String getContact() { return contact; }

    /** @return the user's profile picture. */
    public Picture getPicture() { return picture; }

    /** @return the facility managed by the user. */
    public Facility getFacility() { return facility; }

    /** @return {@code true} if the user is an admin; {@code false} otherwise. */
    public boolean isAdmin() { return isAdmin; }

    /** @return the list of notifications for the user. */
    public ArrayList<Notification> getNotificationList() { return notificationList; }

    /** @return the list of events the user is enrolled in. */
    public ArrayList<Event> getEnrolledList() { return enrolledList; }

    /** @return the list of events organized by the user. */
    public ArrayList<Event> getOrganizedList() { return organizedList; }

    /** @return the user's notification setting. */
    public Boolean getNotificationSetting() { return notificationSetting; }


    // Functions

    /**
     * Uploads a new profile picture for the user.
     *
     * @param control   the control object
     * @param uploader  the user uploading the picture
     */
    public void uploadPicture(Control control, User uploader) {
        // encode and save to picture string
        String encodedContent = "Some Encoded Content";
        Picture newPic = new Picture(uploader, encodedContent);
        this.picture = newPic;
        control.getPictureList().add(newPic);
    }

    /**
     * Deletes the user's profile picture.
     *
     * @param control  the control object
     */
    public void deletePicture(Control control) {
        control.getPictureList().remove(this.picture);
        this.picture = null;
    }

    /**
     * Creates a new facility managed by the user. A user can only have one facility.
     *
     * @param control     the control object
     * @param name        the name of the facility
     * @param location    the location of the facility
     * @param description the description of the facility
     * @param openTime    the opening time of the facility
     */
    public void createFacility(Control control, String name, String location, String description, String openTime) {
        if (this.facility == null) {
            Facility newFacility = new Facility(name, location, description, openTime, this);
            this.facility = newFacility;
            control.getFacilityList().add(newFacility);
        }
    }

    /**
     * The user edits the managed facility.
     *
     * @param control     the control object
     * @param name        the updated name of the facility
     * @param location    the updated location of the facility
     * @param description the updated description of the facility
     * @param openTime    the updated opening time of the facility
     */
    public void editFacility(Control control, String name, String location, String description, String openTime) {
        if (this.facility != null) {
            this.facility.setName(name);
            this.facility.setLocation(location);
            this.facility.setDescription(description);
            this.facility.setOpenTime(openTime);
        }
    }

    /**
     * The user deletes the managed facility. This method should not be used by admin.
     *
     * @param control the control object
     */
    public void deleteFacility(Control control) {
        control.getFacilityList().remove(this.facility);
        this.facility = null;
    }

    /**
     * The admin deletes a facility for a user. This method should be used by admin.
     *
     * @param control the control object
     * @param facility the facility object the admin wish to delete
     */
    public void adminDeleteFacility(Control control, Facility facility) {
        facility.getCreator().deleteFacility(control);
    }

    /**
     * Creates a new event organized by the user.
     *
     * @param control            the control object
     * @param name               the name of the event
     * @param description        the description of the event
     * @param limitChosenList    the limit for chosen attendees
     * @param limitWaitingList  the limit for the waiting list
     */
    public void createEvent(Control control, String name, String description, int limitChosenList, int limitWaitingList) {
        if (this.facility != null) {
            int eventID = control.getCurrentEventID();
            Event newEvent = new Event(eventID, name, description, limitChosenList, limitWaitingList, this);
            this.organizedList.add(newEvent);
            control.getEventList().add(newEvent);
        }
    }

    /**
     * Deletes an event organized by the user.
     *
     * @param control the control object managing events
     * @param event   the event to be deleted
     */
    public void deleteEvent(Control control, Event event) {
        this.organizedList.remove(event);
        control.getEventList().remove(event);
    }

    /**
     * Adds the user to the waiting list for an event.
     *
     * @param event the event the user wants to join
     */
    public void joinEvent(Event event) {
        // if this user is not on the waiting list and there is still space in the waiting list
        if (!event.getWaitingList().contains(this) && event.getWaitingList().size() < event.getLimitWaitinglList()) {
            event.getWaitingList().add(this);
            enrolledList.add(event);
        }
    }

    /**
     * The user cancels an event in the event page. This will remove the user from waiting list if
     * the user has not been chosen. If the user is already in the chosen list or final list, this
     * will remove them from there as well.
     *
     * @param event the event to cancel participation in
     */
    public void cancelEvent(Event event) {
        event.getWaitingList().remove(this);
        if (event.getChosenList().contains(this)){
            event.getChosenList().remove(this);
            event.getCancelledList().add(this);
        }
        event.getFinalList().remove(this);
        enrolledList.remove(event);
    }

    /**
     * The user accept the invitation in Notification section. This operation will add the user in
     * the final list
     *
     * @param notification the event to cancel participation in
     */
    public void acceptInvitation(Notification notification) {
        notification.accept();
    }

    /**
     * The user decline the invitation in Notification section. This operation will remove the user
     * from the chosen list and add the user in the cancelled list
     *
     * @param notification the event to cancel participation in
     */
    public void declineInvitation(Notification notification) {
        notification.decline();
    }

    /**
     * Removes a notification from the user's notification list.
     *
     * @param notification the notification to be removed
     */
    public void removeNotification(Notification notification) {
        this.notificationList.remove(notification);
    }

    /**
     * Draws participants for an event, selecting a limited number randomly
     * from the waiting list and sending invitations to the winners.
     *
     * @param event the event to perform a reroll for
     */
    public void reRoll(Event event){
        // Construct message
        String automaticMessage = "[Auto] Congratulations! You have been chosen to attend " + event.getName() + "! Click 'Accept' below to accept the invitation!";
        // Calculate remaining spots
        int remainingSpot = event.getLimitChosenList() - event.getChosenList().size();
        // if not enough people registered
        if (event.getWaitingList().size() <= remainingSpot) { // enough spots for everyone
            for (User winner: event.getWaitingList()) { // waiting list shrinks as we delete them
                winner.getNotificationList().add(new Notification(event, winner, true,automaticMessage)); // give everyone an invitation
                event.getChosenList().add(winner); // add the winner to chosen list
            }
            event.getWaitingList().clear(); // clear the waiting list (avoid concurrent modification exception)
        } else { // not enough spots for everyone
            //draw randomly from waiting list (result is in descending order)
            List<Integer> result = Utils.drawRandomNumbers(remainingSpot, event.getWaitingList().size()-1);
            for (int i = 0; i < result.size(); i++) {
                event.getChosenList().add(event.getWaitingList().get(result.get(i))); // add the winner to chosen list
                event.getWaitingList().remove((int)result.get(i)); // remove the winner from waiting list
                // this operation is done from high index to low index, so this should not cause the index of the winners to change
            }
            // add notifications to winners
            for (int i = 0; i < event.getChosenList().size(); i++) {
                event.getChosenList().get(i).getNotificationList().add(new Notification(event, event.getChosenList().get(i), true,automaticMessage));
            }
        }

    }

    /**
     * Updates the user's notification setting.
     *
     * @param setting the new notification setting
     */
    public void changeNotificationSetting(Boolean setting) {
        this.notificationSetting = setting;
    }
}
