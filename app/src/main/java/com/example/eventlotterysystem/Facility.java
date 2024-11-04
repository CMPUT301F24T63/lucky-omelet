/**
 * The {@code Facility} class represents a physical or virtual facility associated with events
 * in the event lottery system. A facility includes details such as its name, location,
 * description, open time, creator, and an optional poster image.
 * An entrant can only create an event after creating a facility. Then the entrant becomes an organizer.
 */
package com.example.eventlotterysystem;

public class Facility {

    // Attributes

    private String name; // Name of the facility
    private String location; // Location of the facility
    private String description; // Description of the facility
    private String openTime; // Open time of the facility
    private User creator; // Creator of the facility
    private Picture poster; // Poster image for the facility, initially null

    // Constructor

    /**
     * Constructs a new {@code Facility} instance with the specified name, location, description,
     * open time, and creator.
     *
     * @param name the name of the facility
     * @param location the location of the facility
     * @param description a brief description of the facility
     * @param openTime the opening hours of the facility
     * @param creator the {@code User} who created the facility
     */
    public Facility(String name, String location, String description, String openTime, User creator) {
        this.name = name;
        this.location = location;
        this.description = description;
        this.openTime = openTime;
        this.creator = creator;
        this.poster = null;
    }

    public Facility(String name, String description, User creator) {
        this.name = name;
        this.location = "Default location";
        this.description = description;
        this.openTime = "Default open time";
        this.creator = creator;
        this.poster = null;
    }

    // Getters

    /**
     * Retrieves the name of the facility.
     *
     * @return the facility name
     */
    public String getName() {return name;}

    /**
     * Retrieves the location of the facility.
     *
     * @return the facility location
     */
    public String getLocation() {return location;}

    /**
     * Retrieves the description of the facility.
     *
     * @return the facility description
     */
    public String getDescription() {return description;}

    /**
     * Retrieves the opening hours of the facility.
     *
     * @return the facility's open time
     */
    public String getOpenTime() {return openTime;}

    /**
     * Retrieves the creator of the facility.
     *
     * @return the {@code User} who created this facility
     */
    public User getCreator() {return creator;}

    /**
     * Retrieves the poster image for the facility.
     *
     * @return the {@code Picture} representing the facility's poster, or {@code null} if not set
     */
    public Picture getPoster() {return poster;}

    // Setters

    /**
     * Sets the name of the facility.
     *
     * @param name the new name for the facility
     */
    public void setName(String name) {this.name = name;}

    /**
     * Sets the location of the facility.
     *
     * @param location the new location for the facility
     */
    public void setLocation(String location) {this.location = location;}

    /**
     * Sets the description of the facility.
     *
     * @param description the new description for the facility
     */
    public void setDescription(String description) {this.description = description;}

    /**
     * Sets the opening hours of the facility.
     *
     * @param openTime the new open time for the facility
     */
    public void setOpenTime(String openTime) {this.openTime = openTime;}

    /**
     * Sets the poster image for the facility.
     *
     * @param poster the {@code Picture} to set as the facility's poster
     */
    public void setPoster(Picture poster) {this.poster = poster;}

}
