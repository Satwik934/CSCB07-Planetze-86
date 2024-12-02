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
    private HashMap<String, ArrayList<EmissionActivityElement>> EmissionActivityMegaLog;
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

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public boolean getFirstLogin() {
        return firstLogin;
    }

    public AnnualAnswers getAnnualAnswers() {
        return AnnualAnswers;
    }

    public ArrayList<EmissionActivityElement> getActivities(String date) {
        if (EmissionActivityMegaLog == null) return null;
        return EmissionActivityMegaLog.get(date);
    }


    public double getDailyEmissions(String date){
        double total = 0.0;
        for(EmissionActivityElement i : EmissionActivityMegaLog.get(date)){
            total += i.getEmissions();
        }
        return total;
    }

    public void setEmissionActivityMegaLog(HashMap<String, ArrayList<EmissionActivityElement>> emissionActivityMegaLog) {
        EmissionActivityMegaLog = emissionActivityMegaLog;
    }
    public HashMap<String, ArrayList<EmissionActivityElement>> getEmissionActivityMegaLog(){
        return EmissionActivityMegaLog;
    }

    public HashMap<String, Integer> getHabitLog() {
        return habitLog;
    }

    public void setHabitLog(HashMap<String, Integer> habitLog) {
        this.habitLog = habitLog;
    }

    // Method to add a habit and increment the logged days

}
