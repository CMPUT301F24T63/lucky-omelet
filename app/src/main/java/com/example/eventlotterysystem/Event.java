/**
 * The {@code Event} class represents an event in the event lottery system.
 * Each event has a unique ID, name, description, creator, and various user lists
 * representing participants, including chosen, waiting, cancelled, and finalized lists.
 * It also includes attributes for the event's poster and a QR code hash.
 *
 * Problem: post and QR code not implemented yet
 */
package com.example.eventlotterysystem;

import android.os.Build;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Random;

public class Event {

    // Attributes

    private int eventID; // Unique identifier for the event
    private String name; // Name of the event
    private String description; // Description of the event
    private int limitChosenList; // Maximum number of users in the chosen list
    private int limitWaitinglList; // Maximum number of users in the waiting list
    private User creator; // Creator of the event
    private Picture poster; // Poster image for the event, initially null
    private String hashCodeQR; // Hash code for the event's QR code, initially empty
    private ArrayList<User> waitingList; // List of users in the waiting queue for the event
    private ArrayList<User> cancelledList; // List of users who cancelled their participation
    private ArrayList<User> chosenList; // List of users selected to participate
    private ArrayList<User> finalList; // List of users confirmed to participate
    private Boolean GeoSetting;

    // Constructor

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLimitChosenList(int limitChosenList) {
        this.limitChosenList = limitChosenList;
    }

    public void setLimitWaitinglList(int limitWaitinglList) {
        this.limitWaitinglList = limitWaitinglList;
    }

    public void generateQR() {
        int key = 19467382;
        int enc = eventID^key;
        this.hashCodeQR = generateRandomHex(12)+Integer.toHexString(enc).toUpperCase()+generateRandomHex(12);
    }

    private String generateRandomHex(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            // Generate a random hex digit (0-9, A-F)
            int hexDigit = random.nextInt(16);
            sb.append(Integer.toHexString(hexDigit).toUpperCase());
        }

        return sb.toString();
    }

    public Boolean getGeoSetting() {
        return GeoSetting;
    }

    public void setGeoSetting(Boolean geoSetting) {
        GeoSetting = geoSetting;
    }

    /**
     * Constructs a new {@code Event} instance with the specified ID, name, description,
     * participant limits, and creator.
     *
     * @param eventID the unique identifier for this event
     * @param name the name of the event
     * @param description the description of the event
     * @param limitChosenList the maximum number of users that can be chosen to participate
     * @param limitWaitingList the maximum number of users that can be on the waiting list
     * @param creator the {@code User} who created the event
     */
    public Event(int eventID, String name, String description, int limitChosenList, int limitWaitingList, User creator) {
        this.eventID = eventID;
        this.name = name;
        this.description = description;
        this.limitChosenList = limitChosenList;
        this.limitWaitinglList = limitWaitingList;
        this.creator = creator;
        this.poster = null;
        this.hashCodeQR = "";
        this.waitingList = new ArrayList<>();
        this.cancelledList = new ArrayList<>();
        this.chosenList = new ArrayList<>();
        this.finalList = new ArrayList<>();
        this.GeoSetting = false;
    }

    public Event(int eventID, String name, String description, int limitChosenList, int limitWaitingList, User creator, Boolean geo) {
        this.eventID = eventID;
        this.name = name;
        this.description = description;
        this.limitChosenList = limitChosenList;
        this.limitWaitinglList = limitWaitingList;
        this.creator = creator;
        this.poster = null;
        this.hashCodeQR = "";
        this.waitingList = new ArrayList<>();
        this.cancelledList = new ArrayList<>();
        this.chosenList = new ArrayList<>();
        this.finalList = new ArrayList<>();
        this.GeoSetting = geo;
    }

    // Getters

    /**
     * Retrieves the unique identifier for this event.
     *
     * @return the event's unique ID
     */
    public int getEventID() {return eventID;}

    /**
     * Retrieves the name of the event.
     *
     * @return the event name
     */
    public String getName() {return name;}

    /**
     * Retrieves the description of the event.
     *
     * @return the event description
     */
    public String getDescription() {return description;}

    /**
     * Retrieves the maximum number of users allowed in the chosen list.
     *
     * @return the chosen list participant limit
     */
    public int getLimitChosenList() {return limitChosenList;}

    /**
     * Retrieves the maximum number of users allowed in the waiting list.
     *
     * @return the waiting list participant limit
     */
    public int getLimitWaitinglList() {return limitWaitinglList;}

    /**
     * Retrieves the creator of the event.
     *
     * @return the {@code User} who created this event
     */
    public User getCreator() {return creator;}

    /**
     * Retrieves the poster image for the event.
     *
     * @return the {@code Picture} representing the event's poster
     */
    public Picture getPoster() {return poster;}

    /**
     * Retrieves the hash code for the event's QR code.
     *
     * @return the QR code hash as a {@code String}
     */
    public String getHashCodeQR() {return hashCodeQR;}

    /**
     * Retrieves the list of users currently waiting for a spot in the event.
     *
     * @return an {@code ArrayList} of {@code User} objects on the waiting list
     */
    public ArrayList<User> getWaitingList() {return waitingList;}

    /**
     * Retrieves the list of users who cancelled their participation.
     *
     * @return an {@code ArrayList} of {@code User} objects on the cancelled list
     */
    public ArrayList<User> getCancelledList() {return cancelledList;}

    /**
     * Retrieves the list of users chosen to participate in the event.
     *
     * @return an {@code ArrayList} of {@code User} objects on the chosen list
     */
    public ArrayList<User> getChosenList() {return chosenList;}

    /**
     * Retrieves the final list of users confirmed to participate in the event.
     *
     * @return an {@code ArrayList} of {@code User} objects on the final list
     */
    public ArrayList<User> getFinalList() {return finalList;}

    // Setters

    /**
     * Sets the HashCodeQR of the event. Used to set data read from database.
     *
     * @param hashCodeQR
     */
    public void setHashCodeQR(String hashCodeQR) {
        this.hashCodeQR = hashCodeQR;
    }

    /**
     * Sets the poster image for the event. Used to set data read from database.
     *
     * @param poster the {@code Picture} representing the event's poster
     */
    public void setPoster(Picture poster) {
        this.poster = poster;
    }

}
