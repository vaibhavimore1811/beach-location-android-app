package com.sanika.beachapplication.model;

import com.google.gson.annotations.SerializedName;

public class Beach {
    private int id;
    private String name;
    private String location;
    private String imageUrl;

    // Constructor
    public Beach(int id, String name, String location, String imageUrl) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.imageUrl = imageUrl;
    }

    public Beach() {

    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "Beach{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
