package com.example.planetze86;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class User {
    private String email;
    private AnnualAnswers AnnualAnswers;
    private String firstName;
    private String lastName;
    private boolean firstLogin;
    private HashMap<String, Integer> habitLog;

    // Default constructor required for calls to DataSnapshot.getValue(User.class)
    public User() {}

    public User(String email, String firstName, String lastName, boolean firstLogin) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.firstLogin = firstLogin;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean getFirstLogin() {
        return firstLogin;
    }

    public void setFirstLogin(boolean firstLogin) {
        this.firstLogin = firstLogin;
    }

    public AnnualAnswers getAnnualAnswers() {
        return AnnualAnswers;
    }

    public void setAnnualAnswers(AnnualAnswers annualAnswers) {
        this.AnnualAnswers = annualAnswers;
    }

    /**
     * Calculates daily emissions dynamically by summing up emissions across all types for a given date.
     * @param activitiesByDate The activities mapped by date and type from Firebase.
     * @param date The date for which emissions are calculated.
     * @return Total emissions for the specified date.
     */
    public double getDailyEmissions(HashMap<String, HashMap<String, ArrayList<Object>>> activitiesByDate, String date) {
        double totalEmissions = 0.0;

        if (activitiesByDate == null || !activitiesByDate.containsKey(date)) {
            return totalEmissions; // Return 0.0 if no data exists for the date
        }

        HashMap<String, ArrayList<Object>> activitiesForDate = activitiesByDate.get(date);
        for (ArrayList<Object> activities : activitiesForDate.values()) {
            for (Object obj : activities) {
                if (obj instanceof EmissionActivityElement) {
                    totalEmissions += ((EmissionActivityElement) obj).getEmissions();
                }
            }
        }

        return totalEmissions;
    }

    public HashMap<String, Integer> getHabitLog() {
        return habitLog;
    }

    public void setHabitLog(HashMap<String, Integer> habitLog) {
        this.habitLog = habitLog;
    }

    // Method to add a habit and increment the logged days

}
