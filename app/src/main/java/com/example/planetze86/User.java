package com.example.planetze86;

public class User {
    private String email;
    private AnnualAnswers AnnualAnswers;
    private String firstName;
    private String lastName;
    private boolean firstLogin;

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
}
