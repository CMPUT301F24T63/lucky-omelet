/**
 * The {@code Control} class is a singleton responsible for managing users, facilities, events, and
 * pictures in the event lottery system. It provides centralized access to the lists of these entities
 * and generates unique IDs for new users and events. This class also keeps track of the currently
 * logged-in user.
 */
package com.example.eventlotterysystem;

import java.util.ArrayList;

public class Control {

    // Attributes

    private int currentUserID; // Tracks the ID for the next user to be created
    private int currentEventID; // Tracks the ID for the next event to be created
    private ArrayList<User> userList; // List of users in the system
    private ArrayList<Facility> facilityList; // List of facilities managed in the system
    private ArrayList<Event> eventList; // List of events in the system
    private ArrayList<Picture> pictureList; // List of pictures uploaded by users
    private static Control instance; // Singleton instance of Control
    private static User currentUser = null; // Currently logged-in user
    private static String localFID; // Local Firebase installation ID

    public static void setLocalFID(String localFID) {
        Control.localFID = localFID;
    }

    public static String getLocalFID() {
        return localFID;
    }

    // Constructor

    /**
     * Constructs a new {@code Control} instance. Initializes unique user and event IDs
     * and initializes lists for users, facilities, events, and pictures.
     */
    public Control() {
        this.currentUserID = 0;
        this.currentEventID = 0;
        this.userList = new ArrayList<>();
        this.facilityList = new ArrayList<>();
        this.eventList = new ArrayList<>();
        this.pictureList = new ArrayList<>();
    }

    // Singleton instance retrieval

    /**
     * Retrieves the singleton instance of {@code Control}. If it does not exist,
     * it creates a new instance.
     *
     * @return the singleton instance of {@code Control}
     */
    public static Control getInstance() {
        if (instance == null) {
            instance = new Control();
        }
        return instance;
    }

    // Getters

    /**
     * Retrieves the current unique event ID and increments it for the next event.
     *
     * @return the unique ID for the new event
     */
    public int getEventIDForEventCreation() {
        int id = currentEventID;
        currentEventID++;
        return id;
    }

    public int getCurrentEventID() {
        return currentEventID;
    }

    /**
     * Retrieves the current unique user ID and increments it for the next user.
     *
     * @return the unique ID for the new user
     */
    public int getUserIDForUserCreation() {
        int id = currentUserID;
        currentUserID++;
        return id;
    }

    /**
     * Retrieves the current unique user ID.
     *
     * @return the current unique user ID
     */
    public int getCurrentUserID() {
        return currentUserID;
    }

    /**
     * Retrieves the list of users in the system.
     *
     * @return an {@code ArrayList} of {@code User} objects
     */
    public ArrayList<User> getUserList() {
        return userList;
    }

    /**
     * Retrieves the list of facilities managed in the system.
     *
     * @return an {@code ArrayList} of {@code Facility} objects
     */
    public ArrayList<Facility> getFacilityList() {
        return facilityList;
    }

    /**
     * Retrieves the list of events in the system.
     *
     * @return an {@code ArrayList} of {@code Event} objects
     */
    public ArrayList<Event> getEventList() {
        return eventList;
    }

    /**
     * Retrieves the list of pictures uploaded by users.
     *
     * @return an {@code ArrayList} of {@code Picture} objects
     */
    public ArrayList<Picture> getPictureList() {
        return pictureList;
    }

    /**
     * Retrieves the currently logged-in user.
     *
     * @return the currently logged-in {@code User} object
     */
    public static User getCurrentUser() {
        return currentUser;
    }

    /**
     * Sets the currently logged-in user.
     *
     * @param user the {@code User} to set as the current user
     */
    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    /**
     * Sets the current unique event ID. Used to set data read from database.
     *
     * @param currentUserID
     */
    public void setCurrentUserID(int currentUserID) {
        this.currentUserID = currentUserID;
    }

    /**
     * Sets the current unique event ID. Used to set data read from database.
     *
     * @param currentEventID
     */
    public void setCurrentEventID(int currentEventID) {
        this.currentEventID = currentEventID;
    }

    public Event getEventById(int eventId) {
        for (Event event : eventList) {
            if (event.getEventID() == eventId) {
                return event;
            }
        }
        return null;
    }

}
