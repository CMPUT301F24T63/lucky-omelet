
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

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.Typeface;
import android.util.Base64;

public class User {

    // Attributes

    private int userID; // Unique identifier for the user
    private String name; // Name of the user
    private String email; // Email address of the user
    private String contact; // Contact information of the user
    private String picture; // User's profile picture, stored as a Picture object
    private Facility facility; // Facility managed by the user
    private boolean isAdmin; // Specifies if the user has administrative privileges
    private ArrayList<Notification> notificationList; // List of notifications for the user
    private ArrayList<Event> enrolledList; // List of events the user is enrolled in
    private ArrayList<Event> organizedList; // List of events organized by the user
    private Boolean notificationSetting; // Notification settings for the user
    private String FID; // Firebase ID of the user

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

    public User(int userID, String name, String email, String contact, Boolean isAdmin, String Pic) {
        this.userID = userID;
        this.name = name;
        this.email = email;
        this.contact = contact;
        this.picture = Pic;
        this.facility = null; // A user has no facility at creation
        this.isAdmin = isAdmin;
        this.notificationList = new ArrayList<>();
        this.enrolledList = new ArrayList<>();
        this.organizedList = new ArrayList<>();
        this.notificationSetting = true;
        this.FID = "Fake User";

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
        this.FID = "Waiting For Update";
    }
    public void generate_picture() {
        if (name != null && !name.isEmpty()) {
            // Extract initials from the user's name
            String initials = getInitials(name);

            // Create the picture (Bitmap) for the user
            Bitmap bitmap = createImageWithInitials(initials);

            // Create a Picture object with the generated Bitmap (assumes the current user is the uploader)
            this.picture = encodeBitmap(bitmap); // You may need a way to encode the bitmap as a String
        }
    }

    // Helper method to extract initials from the name
    private String getInitials(String name) {
        String[] nameParts = name.split(" ");
        StringBuilder initials = new StringBuilder();
        for (String part : nameParts) {
            if (!part.isEmpty()) {
                initials.append(part.charAt(0));  // Take the first letter of each name part
            }
        }
        return initials.toString().toUpperCase();  // Convert initials to uppercase
    }

    // Helper method to create a Bitmap with initials
    private Bitmap createImageWithInitials(String initials) {
        int width = 200;  // Image width
        int height = 200; // Image height
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        // Set background color
        canvas.drawColor(Color.parseColor("#4CAF50")); // Green background (you can change the color)

        // Set up paint object for text
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);  // Text color
        paint.setTextSize(100);  // Text size
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setTextAlign(Paint.Align.CENTER);

        // Draw initials in the center of the canvas
        float xPos = width / 2;
        float yPos = (height / 2) - ((paint.descent() + paint.ascent()) / 2);
        canvas.drawText(initials, xPos, yPos, paint);

        return bitmap;  // Return the generated bitmap
    }

    // Helper method to encode Bitmap to a String (Base64 encoding or any method you prefer)
    private String encodeBitmap(Bitmap bitmap) {
        // Convert bitmap to a Base64 encoded string (as an example)
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] byteArray = baos.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
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
    public String getPicture() { return picture; }

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

    /** @return the user's Firebase ID. */
    public String getFID() { return FID; }

    // Setters

    /**
     * Sets the user's facility. Used to set data read from the database.
     *
     * @param facility the facility to set
     */
    public void setFacility(Facility facility) {
        this.facility = facility;
    }

    /**
     * Sets the user's profile picture. Used to set data read from the database.
     *
     * @param picture the picture to set
     */
    public void setPicture(String picture) {
        this.picture = picture;
    }

    /**
     * Sets the user's name.
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the user's contact.
     *
     * @param contact the email to set
     */
    public void setContact(String contact) {
        this.contact = contact;
    }

    /**
     * Sets the user's email.
     *
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Sets the user's notification setting.
     *
     * @param notificationSetting the notification setting to set
     */
    public void setNotificationSetting(Boolean notificationSetting) {
        this.notificationSetting = notificationSetting;
    }

    /**
     * Sets the user's Firebase ID.
     *
     * @param FID the Firebase ID to set
     */
    public void setFID(String FID) {
        this.FID = FID;
    }

    // Functions

    /**
     * Deletes the user's profile picture.
     *
     * @param control  the control object
     */
    public void deletePicture(Control control) {
        this.picture = null;
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
    public void createEvent(Control control, String name, String description, int limitChosenList, int limitWaitingList, boolean geoSetting) {
        if (this.facility != null) {
            int eventID = control.getEventIDForEventCreation();
            Event newEvent = new Event(eventID, name, description, limitChosenList, limitWaitingList, this, geoSetting);
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
        this.enrolledList.remove(event);
        event.getWaitingList().remove(this);
        if (event.getChosenList().contains(this)){
            event.getChosenList().remove(this);
            event.getCancelledList().add(this);
        }
        if (event.getFinalList().contains(this)) {
            event.getFinalList().remove(this);
            event.getCancelledList().add(this);
        }
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
        int remainingSpot = event.getLimitChosenList() - event.getChosenList().size() - event.getFinalList().size();
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
                // add notifications to the new winner
                event.getWaitingList().get(result.get(i)).getNotificationList().add(new Notification(event, event.getWaitingList().get(result.get(i)), true, automaticMessage));
                event.getWaitingList().remove((int)result.get(i)); // remove the winner from waiting list
                // this operation is done from high index to low index, so this should not cause the index of the winners to change
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
