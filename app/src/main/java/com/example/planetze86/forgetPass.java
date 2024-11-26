package com.example.planetze86;
import android.content.Intent;
import android.widget.ImageButton;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class forgetPass extends AppCompatActivity {

    private EditText emailEditText;
    private Button submitButton;
    private FirebaseAuth mAuth;

    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass);

        mAuth = FirebaseAuth.getInstance();
        emailEditText = findViewById(R.id.emailEditText);
        submitButton = findViewById(R.id.resetPasswordButton);
        backButton = findViewById(R.id.backButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(forgetPass.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                } else {
                    resetPassword(email);
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(forgetPass.this, LoginActivityView.class);
                startActivity(intent);
            }
        });
    }

    private void resetPassword(String email) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(forgetPass.this, "Password reset email sent", Toast.LENGTH_SHORT).show();
                        finish(); // Go back to the login activity
                    } else {
                        Toast.makeText(forgetPass.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}