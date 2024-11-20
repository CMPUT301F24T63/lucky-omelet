/**
 * This class represents a picture in the event lottery system. Picture upload is not supported in this release.
 */

package com.example.eventlotterysystem;

import java.io.Serializable;

public class Picture implements Serializable {
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
