package com.example.eventlotterysystem;

public class Facility {
    // Attributes
    private String name;
    private String location;
    private String description;
    private String openTime;
    private final User creator;
    private Picture poster;

    // Constructor
    public Facility(String name, String location, String description, String openTime, User creator) {
        this.name = name;
        this.location = location;
        this.description = description;
        this.openTime = openTime;
        this.creator = creator;
        this.poster = null;
    }

    // Getters

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public String getOpenTime() {
        return openTime;
    }

    public User getCreator() {
        return creator;
    }

    public Picture getPoster() {
        return poster;
    }

    // Setters (May add constraint later)

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public void setPoster(Picture poster) {
        this.poster = poster;
    }

}
