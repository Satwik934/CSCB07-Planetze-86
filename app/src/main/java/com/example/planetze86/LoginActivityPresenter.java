package com.example.planetze86;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;

public class LoginActivityPresenter {
    LoginActivityView view;
    LoginActivityModel model;
    public LoginActivityPresenter(LoginActivityView view,LoginActivityModel model){
        this.view = view;
        this.model = model;
    }

    // Handle Forgot Password button click
    public void onForgotPasswordClicked() {
        Intent intent = new Intent(view, forgetPass.class);
        view.startActivity(intent);
    }

    // Handle Back button click
    public void onBackButtonClicked() {
        Intent intent = new Intent(view, MainActivity.class);
        view.startActivity(intent);
    }

    public void loginUser(String email, String password) {


        if (email.isEmpty() || password.isEmpty()) {
            view.setOutput("Please enter your email and password");
            return;
        }

        // Sign in with email and password
        model.loginUser(email, password)
                .addOnCompleteListener(view, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign-in successful
                            FirebaseUser user = model.getCurrentUser();
                            if (user != null && user.isEmailVerified()) {
                                String uid = user.getUid();

                                // Check `firstLogin` field in Firebase Realtime Database
                                checkFirstLogin(uid);
                            } else {
                                // Email not verified
                                view.setOutput("Please verify your email before logging in.");
                                model.signOut();

                            }
                        } else {
                            // Sign-in failed, show an error message
                            view.setOutput("Invalid email or password");
                            Log.e("LoginActivity", "Login failed", task.getException());
                        }
                    }
                });
    }

    // Method to check `firstLogin` from Firebase Realtime Database
    private void checkFirstLogin(String uid) {


        model.checkFirstLogin(uid).addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful() && task.getResult().exists()) {
                    DataSnapshot snapshot = task.getResult();
                    boolean firstLogin = snapshot.child("firstLogin").getValue(Boolean.class);

                    if (firstLogin) {
                        // Redirect to onboarding activity
                        Intent intent = new Intent(view, Onboarding.class); // Replace with your onboarding activity
                        view.startActivity(intent);

                        // Update `firstLogin` to false
                        model.updateFirstLogin(uid);
                        view.finish();

                    } else {
                        // Redirect to main dashboard

                        Intent intent = new Intent(view, EcoTracker.class); // Replace with your main dashboard activity
                        view.startActivity(intent);
                        view.finish();
                    }


                } else {
                    view.setOutput("Failed to retrieve user data. Please try again.");
                    Log.e("LoginActivity", "Database error", task.getException());
                }
            }
        });
    }


}
