package com.example.eventlotterysystem;

import java.io.Serializable;
import java.util.ArrayList;

public class Control {
    private static Control instance;

    public static Control getInstance() {
        if (instance == null) {
            instance = new Control();
        }
        return instance;
    }

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
    public void set_currentUserID(int id){
        this.currentUserID = id;
    }

    public int getCurrentEventID() {
        int id = currentEventID;
        currentEventID++;
        return id;
    }

    public int getCurrentUserID() {
        return currentUserID;
    }

    public ArrayList<User> getUserList() {
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
