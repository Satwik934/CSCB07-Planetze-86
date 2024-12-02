package com.example.planetze86;

import java.util.UUID;

public abstract class EmissionActivityElement {
    private String type; // Discriminator field
    private String date;
    private String id; // Change int to String for unique IDs

    public EmissionActivityElement(String date, String type) {
        this.date = date;
        this.type = type;
        this.id = UUID.randomUUID().toString(); // Automatically generate unique ID
    }

    public EmissionActivityElement() {
        this.id = UUID.randomUUID().toString(); // Default constructor for Firebase
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public abstract String getDetails();
    public abstract double getEmissions();
}
