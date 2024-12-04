package com.example.planetze86;

public class FoodConsumptionActivityElement extends EmissionActivityElement {
    private String mealType; // E.g., "Beef", "Plant-Based"
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
        double emissions = servings;
        switch (mealType){
            case "Beef": {
                emissions *= 2500/(365*2.5);//Assuming average 2.5 meals per day
            }
            case "Pork": {
                emissions *= 2500/(365*2.5);
            }
            case "Chicken": {
                emissions *= 2500/(365*2.5);
            }
            case "Fish": {
                emissions *= 1500/(365*2.5);
            }
            case "Plant-Based":{
                emissions *= 1000/(365*2.5);
            }
        }
        return emissions;
    }
}
