package com.example.eventlotterysystem;

public class Picture {
    // Attributes
    private String content; //encoded
    private User uploader;

    // Constructor
    public Picture(User uploader, String content){
        this.uploader = uploader;
        this.content = content;
    }

    // Getters

    public String getContent() {
        return content;
    }

    public User getUploader() {
        return uploader;
    }

    // Functions



}
