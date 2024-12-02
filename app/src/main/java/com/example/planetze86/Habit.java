package com.example.planetze86;

import java.util.HashMap;
import java.util.Map;

public class Habit {
    private String name;
    private String category;
    private String impact;

    // Constructor
    public Habit(String name, String category, String impact) {
        this.name = name;
        this.category = category;
        this.impact = impact;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImpact() {
        return impact;
    }

    public void setImpact(String impact) {
        this.impact = impact;
    }


}

