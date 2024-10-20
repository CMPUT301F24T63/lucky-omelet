/*** TO DO:
    1. picture encoding and decoding
    2. Comparing Events: if (notificationList.get(i).getEvent().equals(event)){
    3. Testing!!!
 */



package com.example.eventlotterysystem;

import java.util.ArrayList;
import java.util.List;

public class User {
    // Attributes
    private int userID;
    private String firstName;
    private String lastName;
    private String email;
    private String contact;
    private Picture picture; // Convert pictures to String
    private Facility facility;
    private boolean isAdmin;
    private ArrayList<Notification> notificationList;
    private ArrayList<Event> enrolledList;
    private ArrayList<Event> organizedList;

    // Constructor
    public User(int userID, String firstName, String lastName, String email, String contact, Boolean isAdmin) {
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.contact = contact;
        this.picture = null; // no pic
        this.facility = null; // A user has no facility at creation
        this.isAdmin = isAdmin;
        this.notificationList = new ArrayList<>();
        this.enrolledList = new ArrayList<>();
        this.organizedList = new ArrayList<>();
    }

    // Getters

    public int getUserID() {
        return userID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getContact() {
        return contact;
    }

    public Picture getPicture() {
        return picture;
    }

    public Facility getFacility() {
        return facility;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public ArrayList<Notification> getNotificationList() {
        return notificationList;
    }

    public ArrayList<Event> getEnrolledList() {
        return enrolledList;
    }

    public ArrayList<Event> getOrganizedList() {
        return organizedList;
    }

    // Functions

    public void uploadPicture(Control control, User uploader) {
        // encode and save to picture string
        String encodedContent = "Some Encoded Content";
        Picture newPic = new Picture(uploader, encodedContent);
        this.picture = newPic;
        control.getPictureList().add(newPic);
    }

    public void deletePicture(Control control) {
        control.getPictureList().remove(this.picture);
        this.picture = null;
    }

    public void createFacility(Control control, String name, String location, String description, String openTime, User creator) {
        if (this.facility == null) {
            Facility newFacility = new Facility(name, location, description, openTime, creator);
            this.facility = newFacility;
            control.getFacilityList().add(newFacility);
        }
    }

    public void deleteFacility(Control control) {
        control.getFacilityList().remove(this.facility);
        this.facility = null;
    }

    public void createEvent(Control control, String name, String description, int limit) {
        int eventID = control.getCurrentEventID();
        Event newEvent = new Event(eventID, name, description, limit, this, facility); // "this" refers to the user himself
        this.organizedList.add(newEvent); // add to organized events
        control.getEventList().add(newEvent); // add to all events
    }

    // Function to delete an event
    public void deleteEvent(Control control, Event event) {
        this.organizedList.remove(event);
        control.getEventList().remove(event);
    }

    public void joinEvent(Event event) {
        if (!event.getWaitingList().contains(this)) { // if this user is not on the waiting list
            event.getWaitingList().add(this);
        }
    }

    public void cancelEvent(Event event) { // on the view event page (different from declining invitation)
        event.getWaitingList().remove(this);
        if (event.getChosenList().contains(this)){ // if this user has been chosen
            event.getChosenList().remove(this);
            event.getCancelledList().add(this);
        }
        event.getFinalList().remove(this);
    }

    // Function to accept an invitation
    public void acceptInvitation(Notification notification) {
        notification.accept();
        notification.getEvent().getChosenList().add(this); // accept -> chosen list
    }

    // Function to decline an invitation
    public void declineInvitation(Notification notification) {
        notification.decline();
        notification.getEvent().getCancelledList().add(this);
    }

    // Function to remove a notification
    public void removeNotification(Notification notification) {
        this.notificationList.remove(notification);
    }

    public void reRoll(Event event){ // use for the first time = draw
        // Construct message
        String automaticMessage = "[Auto] Congratulations! You have been chosen to attend " + event.getName() + "! Click 'Accept' below to accept the invitation!";
        Notification automaticInvitation = new Notification(event, true, automaticMessage);
        // Calculate remaining spots
        int remainingSpot = event.getLimit() - event.getChosenList().size();
        // if not enough people registered
        if (event.getWaitingList().size() <= remainingSpot) {
            for (int i = 0; i < event.getWaitingList().size(); i++) {
                // give everyone an invitation
                event.getWaitingList().get(i).getNotificationList().add(automaticInvitation);
            }
        } else { // not enough spots for everyone
            //draw randomly from waiting list (result is in descending order)
            List<Integer> result = Utils.drawRandomNumbers(remainingSpot, event.getWaitingList().size());
            for (int i = 0; i < event.getWaitingList().size(); i++) {
                event.getChosenList().add(event.getWaitingList().get(i)); // add the winner to chosen list
                event.getWaitingList().remove(i); // remove the winner from waiting list
                // this operation is done from high index to low index, so this should not cause the index of the winners to change
            }
        }

    }

    public void makeFinal(Event event){
        event.getFinalList().clear();
        event.getFinalList().addAll(event.getWaitingList());
    }

}
