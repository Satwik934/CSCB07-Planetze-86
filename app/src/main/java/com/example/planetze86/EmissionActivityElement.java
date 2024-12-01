package com.example.planetze86;
public abstract class EmissionActivityElement {

    private String type; // Discriminator field
    private String date;

    public EmissionActivityElement(String date, String type) {
        this.date = date;
        this.type = type;
    }

    public EmissionActivityElement() {} // Default constructor for Firebase

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
