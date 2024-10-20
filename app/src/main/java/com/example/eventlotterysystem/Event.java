/*** TO DO:
 1. poster encoding and decoding
 2. Hash Code for QR code
 */

package com.example.eventlotterysystem;

import java.util.ArrayList;

public class Event {
    // Attributes
    private int eventID;
    private String name;
    private String description;
    private int limit;
    private User creator;
    private Facility facility;
    private Picture poster;
    private String hashCodeQR;
    private ArrayList<User> waitingList;
    private ArrayList<User> cancelledList;
    private ArrayList<User> chosenList;
    private ArrayList<User> finalList;

    // Constructor
    public Event(int eventID, String name, String description, int limit, User creator, Facility facility) {
        this.eventID = eventID;
        this.name = name;
        this.description = description;
        this.limit = limit;
        this.creator = creator;
        this.facility = facility;
        this.poster = null; // no poster
        this.hashCodeQR = "";
        this.waitingList = new ArrayList<>();
        this.cancelledList = new ArrayList<>();
        this.chosenList = new ArrayList<>();
        this.finalList = new ArrayList<>();
    }

    // Getters

    public int getEventID() {
        return eventID;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getLimit() {
        return limit;
    }

    public User getCreator() {
        return creator;
    }

    public Facility getFacility() {
        return facility;
    }

    public Picture getPoster() {
        return poster;
    }

    public String getHashCodeQR() {
        return hashCodeQR;
    }

    public ArrayList<User> getWaitingList() {
        return waitingList;
    }

    public ArrayList<User> getCancelledList() {
        return cancelledList;
    }

    public ArrayList<User> getChosenList() {
        return chosenList;
    }

    public ArrayList<User> getFinalList() {
        return finalList;
    }

    // Functions


}
