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
    private int limitChosenList;
    private int limitWaitinglList;
    private User creator;
    private Picture poster;
    private String hashCodeQR;
    private ArrayList<User> waitingList;
    private ArrayList<User> cancelledList;
    private ArrayList<User> chosenList;
    private ArrayList<User> finalList;

    // Constructor
    public Event(int eventID, String name, String description, int limitChosenList, int limitWaitinglList, User creator) {
        this.eventID = eventID;
        this.name = name;
        this.description = description;
        this.limitChosenList = limitChosenList;
        this.limitWaitinglList = limitWaitinglList;
        this.creator = creator;
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

    public int getLimitChosenList() {
        return limitChosenList;
    }

    public int getLimitWaitinglList() {
        return limitWaitinglList;
    }

    public User getCreator() {
        return creator;
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
