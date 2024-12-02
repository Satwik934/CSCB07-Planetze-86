package com.example.planetze86;

public class ShoppingActivityElement extends EmissionActivityElement {
    private String itemType;     // Main category (e.g., "Clothing", "Electronics", etc.)
    private String subCategory; // Subcategory for finer segregation (e.g., "Smartphone", "Laptop")
    private int quantity;        // Number of items purchased
    private double totalCost;    // Total cost in dollars
    // Constructor for energy bills

    public ShoppingActivityElement(String date, String billType, double totalCost) {

        super(date, "Shopping");

        this.itemType = "Energy Bill";

        this.subCategory = billType;

        this.quantity = 0; // Not applicable for bills

        this.totalCost = totalCost;

    }
    public ShoppingActivityElement(String date, String itemType, String subCategory, int quantity, double totalCost) {
        super(date, "Shopping");
        this.itemType = itemType;
        this.subCategory = subCategory;
        this.quantity = quantity;
        this.totalCost = totalCost;
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

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    @Override
    public String getDetails() {
        return "Item: " + itemType + " (" + subCategory + "), Quantity: " + quantity + ", Total Cost: $" + totalCost;
    }

    @Override
    public double getEmissions() {
        double emissionFactor = 0.0;

        if ("Electronics".equalsIgnoreCase(itemType)) {
            switch (subCategory) {
                case "Smartphone":
                    emissionFactor = 70.0; // kg CO2e per unit
                    break;
                case "Laptop":
                    emissionFactor = 200.0; // kg CO2e per unit
                    break;
                case "Tablet":
                    emissionFactor = 120.0; // kg CO2e per unit
                    break;
                case "Television":
                    emissionFactor = 300.0; // kg CO2e per unit
                    break;
                default:
                    emissionFactor = 100.0; // Default for other electronics
                    break;
            }
        } else {
            // General emission factors for other item types
            switch (itemType) {
                case "Clothing":
                    emissionFactor = 30.0; // kg CO2e per unit
                    break;
                case "Furniture":
                    emissionFactor = 100.0; // kg CO2e per unit
                    break;
                default:
                    emissionFactor = 10.0; // Default for unknown item types
                    break;
            }
        }

        return quantity * emissionFactor; // Compute emissions based on quantity
    }
}
