/**
 * The {@code Event} class represents an event in the event lottery system.
 * Each event has a unique ID, name, description, creator, and various user lists
 * representing participants, including chosen, waiting, cancelled, and finalized lists.
 * It also includes attributes for the event's poster and a QR code hash.
 *
 * Problem: post and QR code not implemented yet
 */
package com.example.eventlotterysystem;

import android.graphics.Bitmap;
import android.graphics.Picture;
import android.util.Base64;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Random;

public class Event {

    // Attributes

    private int eventID; // Unique identifier for the event
    private String name; // Name of the event
    private String description; // Description of the event
    private int limitChosenList; // Maximum number of users in the chosen list
    private int limitWaitinglList; // Maximum number of users in the waiting list
    private User creator; // Creator of the event
    private String poster; // Poster image for the event, initially null
    private String hashCodeQR; // Hash code for the event's QR code, initially empty
    private ArrayList<User> waitingList; // List of users in the waiting queue for the event
    private ArrayList<User> cancelledList; // List of users who cancelled their participation
    private ArrayList<User> chosenList; // List of users selected to participate
    private ArrayList<User> finalList; // List of users confirmed to participate
    private ArrayList<Integer> waitingListRef; // List of users in the waiting queue for the event
    private ArrayList<Integer> cancelledListRef; // List of users who cancelled their participation
    private ArrayList<Integer> chosenListRef; // List of users selected to participate
    private ArrayList<Integer> finalListRef; // List of users confirmed to participate
    private Boolean GeoSetting; // Whether the event require location to register
    private ArrayList<Double> latitudeList; // List of latitudes for users' location
    private ArrayList<Double> longitudeList; // List of longitudes for users' location
    private int creatorRef;

    // Constructor

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
        this.latitudeList = new ArrayList<>();
        this.longitudeList = new ArrayList<>();
    }

    public ArrayList<Integer> getWaitingListRef() {
        return waitingListRef;
    }

    public ArrayList<Integer> getCancelledListRef() {
        return cancelledListRef;
    }

    public ArrayList<Integer> getChosenListRef() {
        return chosenListRef;
    }

    public ArrayList<Integer> getFinalListRef() {
        return finalListRef;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public int getCreatorRef() {
        return creatorRef;
    }

    public Event(int eventID, String name, String description, int limitChosenList, int limitWaitingList, int creatorRef, Boolean geo, String qr, String poster) {
        this.eventID = eventID;
        this.name = name;
        this.description = description;
        this.limitChosenList = limitChosenList;
        this.limitWaitinglList = limitWaitingList;
        this.creatorRef = creatorRef;
        this.creator = null;
        this.poster = poster;
        this.hashCodeQR = "";
        this.waitingList = new ArrayList<>();
        this.cancelledList = new ArrayList<>();
        this.chosenList = new ArrayList<>();
        this.finalList = new ArrayList<>();
        this.waitingListRef = new ArrayList<>();
        this.cancelledListRef = new ArrayList<>();
        this.chosenListRef = new ArrayList<>();
        this.finalListRef = new ArrayList<>();
        this.GeoSetting = geo;
        this.latitudeList = new ArrayList<>();
        this.longitudeList = new ArrayList<>();
        this.hashCodeQR = qr;
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
    public String getPoster() {return poster;}

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

    public Boolean getGeoSetting() {
        return GeoSetting;
    }

    public ArrayList<Double> getLongitudeList() {
        return longitudeList;
    }

    public ArrayList<Double> getLatitudeList() {
        return latitudeList;
    }

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
    public void setPoster(String poster) {
        this.poster = poster;
    }

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
        QRCodeWriter writer = new QRCodeWriter();
        try {
            // Define QR code dimensions
            int width = 200;
            int height = 200;
            // Generate QR code bit matrix
            com.google.zxing.common.BitMatrix bitMatrix = writer.encode(String.valueOf(eventID), BarcodeFormat.QR_CODE, width, height);
            // Create a bitmap from the bit matrix
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                }
            }
            this.hashCodeQR = encodeBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private String encodeBitmap(Bitmap bitmap) {
        // Convert bitmap to a Base64 encoded string (as an example)
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] byteArray = baos.toByteArray();
        return android.util.Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public void setGeoSetting(Boolean geoSetting) {
        GeoSetting = geoSetting;
    }
}
