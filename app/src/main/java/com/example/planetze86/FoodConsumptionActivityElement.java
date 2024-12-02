package com.example.planetze86;

public class FoodConsumptionActivityElement extends EmissionActivityElement {
    private String mealType; // E.g., "Beef", "Vegetarian"
    private int servings;    // Number of servings

    public FoodConsumptionActivityElement(String date, String mealType, int servings) {
        super(date,"FoodConsumption");
        this.mealType = mealType;
        this.servings = servings;
    }
    public FoodConsumptionActivityElement(){}
    // Getters and Setters
    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    @Override
    public String getDetails() {
        return "Meal: " + mealType + ", Servings: " + servings;
    }

    @Override
    public double getEmissions() {
        return 0;
    }
}
