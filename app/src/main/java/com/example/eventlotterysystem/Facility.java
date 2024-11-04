package com.example.eventlotterysystem;

public class Facility {
    // Attributes
    private String name;
    private String description;
    private final User creator;
    private Picture poster;

    // Constructor
    public Facility(String name, String description, User creator) {
        this.name = name;
        this.description = description;
        this.poster = null;
        this.creator = creator;
    }

    // Getters

    public String getName() {
        return name;
    }


    public String getDescription() {
        return description;
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

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPoster(Picture poster) {
        this.poster = poster;
    }

}
