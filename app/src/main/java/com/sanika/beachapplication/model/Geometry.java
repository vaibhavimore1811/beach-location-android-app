package com.sanika.beachapplication.model;

import java.util.List;

public class Geometry {
    private String type; // Geometry type (e.g., "Point")
    private List<Double> coordinates; // List of coordinates [longitude, latitude]

    // Getters and Setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Double> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Double> coordinates) {
        this.coordinates = coordinates;
    }
}


