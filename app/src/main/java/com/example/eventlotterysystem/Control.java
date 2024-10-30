package com.example.eventlotterysystem;

import java.util.ArrayList;

public class Control {
    // Attributes
    private int currentUserID;
    private int currentEventID;
    private ArrayList<User> userList;
    private ArrayList<Facility> facilityList;
    private ArrayList<Event> eventList;
    private ArrayList<Picture> pictureList;

    // Constructor
    public Control() {
        this.currentUserID = 0;
        this.currentEventID = 0;
        this.userList = new ArrayList<>();
        this.facilityList = new ArrayList<>();
        this.eventList = new ArrayList<>();
        this.pictureList = new ArrayList<>();
    }

    //getters

    public int getCurrentEventID() {
        int id = currentEventID;
        currentEventID++;
        return id;
    }

    public int getCurrentUserID() {
        int id = currentUserID;
        currentUserID++;
        return id;
    }

    public ArrayList<User> getUserList() {
        // if user need admin privilege to getUserList, they can not add themself to the list
        // Maybe try to control this with database permission later?
        // Write permission and no read permission?
        return userList;
    }

    public ArrayList<Facility> getFacilityList() {
        return facilityList;
    }

    public ArrayList<Event> getEventList() {
        return eventList;
    }

    public ArrayList<Picture> getPictureList() {
        return pictureList;
    }

    // functions

}
