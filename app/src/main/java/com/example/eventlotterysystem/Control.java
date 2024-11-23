/**
 * The {@code Control} class is a singleton responsible for managing users, facilities, events, and
 * pictures in the event lottery system. It provides centralized access to the lists of these entities
 * and generates unique IDs for new users and events. This class also keeps track of the currently
 * logged-in user.
 */
package com.example.eventlotterysystem;

import android.graphics.Picture;
import android.util.Log;

import java.util.ArrayList;

public class Control {

    // Attributes

    private int currentUserID; // Tracks the ID for the next user to be created
    private int currentEventID; // Tracks the ID for the next event to be created
    private ArrayList<User> userList; // List of users in the system
    private ArrayList<Facility> facilityList; // List of facilities managed in the system
    private ArrayList<Event> eventList; // List of events in the system
    private ArrayList<Notification> notificationList; // List of notifications for users
    private static Control instance; // Singleton instance of Control
    private static User currentUser = null; // Currently logged-in user
    private static String localFID = ""; // Local Firebase installation ID

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
    }

    // Singleton instance retrieval

    public ArrayList<Notification> getNotificationList() {
        return notificationList;
    }

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


    /**
     * Sets the singleton instance of {@code Control}.
     *
     * @param instance the {@code Control} instance to set as the singleton
     */
    public static void setInstance(Control instance) {
        Control.instance = instance;
    }

    /**
    * Get Event object by providing eventID. 
    *
    * @para eventId EventID
    */
    public Event getEventById(int eventId) {
        for (Event event : eventList) {
            if (event.getEventID() == eventId) {
                return event;
            }
        }
        return null;
    }

    public void match(){
        for (User user : userList) {
            Facility fac = findFacById(user.getUserID());
            if (fac!=null){
                user.setFacility(fac);
                fac.setCreator(user);
            }
        }
        for (Event event : eventList){
            User c = findUserById(event.getCreatorRef());
            if (c != null){
                event.setCreator(c);
                c.getOrganizedList().add(event);
            }
            for (int id : event.getWaitingListRef()){
                User u = findUserById(id);
                if (u!=null){
                    event.getWaitingList().add(u);
                    u.getEnrolledList().add(event);
                }
            }
            for (int id : event.getCancelledListRef()){
                User u = findUserById(id);
                if (u!=null){
                    event.getCancelledList().add(u);
//                    u.getEnrolledList().add(event);
//                    ?????????????????????????????????????????????????????????????
                }
            }
            for (int id : event.getChosenListRef()){
                User u = findUserById(id);
                if (u!=null){
                    event.getChosenList().add(u);
                    u.getEnrolledList().add(event);
                }
            }
            for (int id : event.getFinalListRef()){
                User u = findUserById(id);
                if (u!=null){
                    event.getFinalList().add(u);
                    u.getEnrolledList().add(event);
                }
            }
        }
    }

    private User findUserById(int userId) {
        for (User user : userList) {
            if (user.getUserID() == userId){
//                Log.i("++++++++++++++++++++++++++++", "++++++++++++++++++++++++++++");
                return user;
            }
        }
//        Log.i("___________________________", "user"+String.valueOf(userId));
        return null;
    }

    private Facility findFacById(int userId) {
        for (Facility fac : facilityList) {
            if (fac.getCreatorRef() == userId) {
//                Log.i("++++++++++++++++++++++++++++", "++++++++++++++++++++++++++++");
                return fac;
            }
        }
//        Log.i("___________________________", "fac"+String.valueOf(userId));
        return null;
    }

    private Event findEventById(int eventId) {
        for (Event event : eventList) {
            if (event.getEventID() == eventId) return event;
        }
        return null;
    }

}
