package com.example.planetze86;

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

    public LoginActivityModel() {
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
    }

    // Method to log in the user
    public Task<AuthResult> loginUser(String email, String password) {
        return mAuth.signInWithEmailAndPassword(email, password);
    }

    // Method to get current user
    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    public void signOut(){
        mAuth.signOut();
    }

    // Method to check if it's the user's first login
    public Task<DataSnapshot> checkFirstLogin(String uid) {
        return databaseReference.child(uid).get();
    }

    // Method to update the firstLogin field
    public void updateFirstLogin(String uid) {
        databaseReference.child(uid).child("firstLogin").setValue(false);
    }
}
