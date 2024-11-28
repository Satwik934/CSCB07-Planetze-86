package com.example.planetze86;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivityModel {
    private final FirebaseAuth mAuth;
    private final DatabaseReference databaseReference;

    public interface LoginCallback {
        void onSuccess(FirebaseUser user, boolean firstLogin);
        void onFailure(String errorMessage);
        void onEmailNotVerified();
    }

    public LoginActivityModel() {
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
    }

    // Method to log in the user
    public void loginUser(String email, String password, LoginCallback callback) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null && user.isEmailVerified()) {
                    checkFirstLogin(user, callback);
                } else {
                    callback.onEmailNotVerified();
                }
            } else {
                callback.onFailure("Invalid email or password");
            }
        });
    }

    // Check if it's the user's first login
    private void checkFirstLogin(FirebaseUser user, LoginCallback callback) {
        String uid = user.getUid();
        databaseReference.child(uid).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                DataSnapshot snapshot = task.getResult();
                Boolean firstLogin = snapshot.child("firstLogin").getValue(Boolean.class);
                callback.onSuccess(user, firstLogin != null && firstLogin);
            } else {
                callback.onFailure("Failed to retrieve user data");
            }
        });
    }

    // Update firstLogin field
    public void updateFirstLogin(String uid) {
        databaseReference.child(uid).child("firstLogin").setValue(false);
    }
    public void signOut(){
        mAuth.signOut();
    }
}

