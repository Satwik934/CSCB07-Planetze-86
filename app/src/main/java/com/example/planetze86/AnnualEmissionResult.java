package com.example.planetze86;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AnnualEmissionResult extends AppCompatActivity {

    // Firebase references
    private DatabaseReference databaseReference;

    // UI components
    private TextView totalFootprintValue, transportationValue, foodValue, housingValue, consumptionValue, comparisonText, globalComparisonText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annual_emission_result);

        // Initialize Firebase
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            // Handle user not logged in
            finish(); // Close the activity
            return;
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        // Bind UI elements
        Button dashboard = findViewById(R.id.dashboard_button);
        totalFootprintValue = findViewById(R.id.total_footprint_value);
        transportationValue = findViewById(R.id.transportation_value);
        foodValue = findViewById(R.id.food_value);
        housingValue = findViewById(R.id.housing_value);
        consumptionValue = findViewById(R.id.consumption_value);
        comparisonText = findViewById(R.id.comparison_text);
        globalComparisonText = findViewById(R.id.global_comparison_text);

        // Fetch and display the data
        fetchAnnualAnswers(currentUser.getUid());

        // Set dashboard button click listener
        dashboard.setOnClickListener(v -> {
            Intent intent = new Intent(AnnualEmissionResult.this, EcoTracker.class);
            startActivity(intent);
        });
    }

    private void fetchAnnualAnswers(String userId) {
        // Fetch data from Firebase
        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null && user.getAnnualAnswers() != null) {
                    AnnualAnswers annualAnswers = user.getAnnualAnswers();

                    // Update the UI with the fetched data
                    totalFootprintValue.setText(String.format("%s Tons CO2e", annualAnswers.getAnnualEmission()));
                    transportationValue.setText(String.format("%s Tons CO2e", annualAnswers.getAnnualTransportation()));
                    foodValue.setText(String.format("%s Tons CO2e", annualAnswers.getAnnualFood()));
                    housingValue.setText(String.format("%s Tons CO2e", annualAnswers.getAnnualHousing()));
                    consumptionValue.setText(String.format("%s Tons CO2e", annualAnswers.getAnnualConsumption()));

                    if (annualAnswers.getAnnualCountryPercentage() > 0) {
                        comparisonText.setText(String.format("Your footprint is %s%% above the national average for %s.",
                                annualAnswers.getAnnualCountryPercentage(), annualAnswers.getCountry()));
                    } else if (annualAnswers.getAnnualCountryPercentage() < 0) {
                        int num = Math.abs((int) annualAnswers.getAnnualCountryPercentage());
                        comparisonText.setText(String.format("Your footprint is %s%% below the national average for %s.",
                                num, annualAnswers.getCountry()));
                    }

                    if (annualAnswers.getAnnualGlobalPercentage() > 0) {
                        globalComparisonText.setText(String.format("Your footprint is %s%% above the global emission target.",
                                annualAnswers.getAnnualGlobalPercentage()));
                    } else if (annualAnswers.getAnnualGlobalPercentage() < 0) {
                        int num = Math.abs((int) annualAnswers.getAnnualGlobalPercentage());
                        globalComparisonText.setText(String.format("Your footprint is %s%% below the global emission target.",
                                num));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Log the error
                System.err.println("Error fetching data: " + databaseError.getMessage());
            }
        });
    }
}
