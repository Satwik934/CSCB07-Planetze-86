package com.example.planetze86;

public class ShoppingActivityElement extends EmissionActivityElement {
    private String itemType;     // Main category (e.g., "Clothing", "Electronics", etc.)
    private String subCategory; // Subcategory for finer segregation (e.g., "Smartphone", "Laptop")
    private int quantity;        // Number of items purchased
    private double cost;    // Total cost in dollars
    // Constructor for energy bills

    public ShoppingActivityElement(String date, String billType, double cost) {

        super(date, "Shopping");

        this.itemType = "Energy Bill";

        this.subCategory = billType;

        this.quantity = 0; // Not applicable for bills

        this.cost = cost;

    }
    public ShoppingActivityElement(String date, String itemType, String subCategory, int quantity, double cost) {
        super(date, "Shopping");
        this.itemType = itemType;
        this.subCategory = subCategory;
        this.quantity = quantity;
        this.cost = cost;
    }

    public ShoppingActivityElement() {
        super(); // Required for Firebase
    }

    // Getters and Setters
    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    @Override
    public String getDetails() {
        return "Item: " + itemType + " (" + subCategory + "), Quantity: " + quantity + ", Total Cost: $" + cost;
    }

    @Override
    public double getEmissions() {
        double emissionFactor = 0.0;

        switch (itemType.toLowerCase()) {
            case "electronics":
                emissionFactor = getElectronicsEmissionFactor(subCategory);
                break;

            case "clothing":
                emissionFactor = 30.0; // kg CO2e per unit for clothing
                break;

            case "miscellaneous":
                emissionFactor = 100.0; // kg CO2e per unit for furniture
                break;

            case "energy bill":
                emissionFactor = calculateEnergyBillEmissions();
                break;

            default:
                emissionFactor = 10.0; // Default for unknown item types
                break;
        }

        return quantity * emissionFactor; // Emissions for item quantity
    }

    // Helper method to get emission factors for electronics subcategories
    private double getElectronicsEmissionFactor(String subCategory) {
        switch (subCategory.toLowerCase()) {
            case "smartphone":
                return 70.0; // kg CO2e per unit
            case "laptop":
                return 200.0; // kg CO2e per unit
            case "tablet":
                return 120.0; // kg CO2e per unit
            case "television":
                return 300.0; // kg CO2e per unit
            default:
                return 100.0; // Default for other electronics
        }

    }

    // Helper method to calculate emissions for energy bills
    private double calculateEnergyBillEmissions() {
        //Estimates
        double result = cost;
        switch(subCategory){
            case "Gas":{
                result *= 5.3;
                break;
            }
            case "Electricity":{
                result *= 4.5;
                break;
            }
            case "Water":{
                result *= 1.5;
            }
        }
        return result;
    }

}
