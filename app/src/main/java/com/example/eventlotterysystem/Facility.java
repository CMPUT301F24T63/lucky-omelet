package com.example.eventlotterysystem;

public class Facility {
    // Attributes
    private String name;
    private String location;
    private String description;
    private String openTime;
    private User creator;
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

    // Functions



}
