package com.example.planetze86;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;

public class LoginActivityView extends AppCompatActivity {


    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private ImageButton backButton;

    LoginActivityPresenter presenter;
    private Button forgotPasswordButton;
    private Button signupButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        presenter = new LoginActivityPresenter(this,new LoginActivityModel());

        // Initialize Firebase Auth


        // Link UI elements
        emailEditText = findViewById(R.id.loginEmail);
        passwordEditText = findViewById(R.id.loginPassword);
        loginButton = findViewById(R.id.loginButton);
        backButton = findViewById(R.id.backButton);


        forgotPasswordButton = findViewById(R.id.forgotPasswordButton);
        signupButton = findViewById(R.id.signupButton);

        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onForgotPasswordClicked();
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onSignupButtonClicked();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onBackButtonClicked();
            }
        });



        // Set up login button click listener
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                presenter.loginUser(email, password);

            }
        });
    }

    public void setOutput(String message){
        Toast.makeText(LoginActivityView.this, message, Toast.LENGTH_SHORT).show();
    }




}