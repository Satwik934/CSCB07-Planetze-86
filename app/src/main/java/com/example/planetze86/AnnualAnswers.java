package com.example.planetze86;

public class AnnualAnswers {
    private String country;
    private double annualEmission;
    private double annualTransportation;
    private double annualFood;
    private double annualHousing;
    private double annualConsumption;
    private double annualCountryPercentage;

    private double annualGlobalPercentage;

    // Default constructor required for Firebase
    public AnnualAnswers() {}

    // Parameterized constructor
    public AnnualAnswers(String country, double annualEmission, double annualTransportation, double annualFood,
                         double annualHousing, double annualConsumption, double annualCountryPercentage,double annualGlobalPercentage) {
        this.country = country;
        this.annualEmission = annualEmission;
        this.annualTransportation = annualTransportation;
        this.annualFood = annualFood;
        this.annualHousing = annualHousing;
        this.annualConsumption = annualConsumption;
        this.annualCountryPercentage = annualCountryPercentage;
        this.annualGlobalPercentage = annualGlobalPercentage;
    }

    // Getters for all fields (required for Firebase)
    public String getCountry() {
        return country;
    }

    public double getAnnualEmission() {
        return annualEmission;
    }

    public double getAnnualTransportation() {
        return annualTransportation;
    }

    public double getAnnualFood() {
        return annualFood;
    }

    public double getAnnualHousing() {
        return annualHousing;
    }

    public double getAnnualConsumption() {
        return annualConsumption;
    }

    public double getAnnualCountryPercentage() {
        return annualCountryPercentage;
    }
    public double getAnnualGlobalPercentage() {
        return annualGlobalPercentage;
    }

}
