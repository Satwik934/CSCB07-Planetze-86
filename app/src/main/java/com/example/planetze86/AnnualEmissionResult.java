package com.example.planetze86;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
    private TextView labelTransportation, labelFood, labelHousing, labelConsumption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annual_emission_result);

        // Initialize Firebase
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            finish();
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

        // Branch labels for the image
        labelTransportation = findViewById(R.id.label_transportation);
        labelFood = findViewById(R.id.label_food);
        labelHousing = findViewById(R.id.label_housing);
        labelConsumption = findViewById(R.id.label_consumption);

        // Fetch and display the data
        fetchAnnualAnswers(currentUser.getUid());

        // Set dashboard button click listener
        dashboard.setOnClickListener(v -> {
            Intent intent = new Intent(AnnualEmissionResult.this, EcoTracker.class);
            startActivity(intent);
        });
    }

    private void fetchAnnualAnswers(String userId) {
        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null && user.getAnnualAnswers() != null) {
                    AnnualAnswers annualAnswers = user.getAnnualAnswers();

                    // Ensure non-null and valid data
                    float transportation = (float) annualAnswers.getAnnualTransportation();
                    float food = (float) annualAnswers.getAnnualFood();
                    float housing = (float) annualAnswers.getAnnualHousing();
                    float consumption = (float) annualAnswers.getAnnualConsumption();
                    float totalEmission = (float) annualAnswers.getAnnualEmission();

                    // Update TextViews
                    transportationValue.setText(String.format("Transportation: %.2f Tons CO2e", transportation));
                    foodValue.setText(String.format("Food: %.2f Tons CO2e", food));
                    housingValue.setText(String.format("Housing: %.2f Tons CO2e", housing));
                    consumptionValue.setText(String.format("Consumption: %.2f Tons CO2e", consumption));
                    totalFootprintValue.setText(String.format("%.2f Tons CO2e", totalEmission));

                    // Update branch labels on the static pie chart
                    updateBranchLabels(transportation, food, housing, consumption);

                    // Update Comparison and Benchmark TextViews
                    comparisonText.setText(getComparisonText(annualAnswers));
                    globalComparisonText.setText(getGlobalComparisonText(annualAnswers));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("AnnualEmissionResult", "Database error: " + error.getMessage());
            }
        });
    }

    private void updateBranchLabels(float transportation, float food, float housing, float consumption) {
        // Update the branch labels with the corresponding values
        labelTransportation.setText(String.format("Transportation (%.2f tons)", transportation));
        labelFood.setText(String.format("Food (%.2f tons)", food));
        labelHousing.setText(String.format("Housing (%.2f tons)", housing));
        labelConsumption.setText(String.format("Consumption (%.2f tons)", consumption));
    }

    private String getComparisonText(AnnualAnswers annualAnswers) {
        String country = annualAnswers.getCountry();
        if (annualAnswers.getAnnualCountryPercentage() > 0) {
            return String.format("Your footprint is %.2f%% above the national average for %s.", annualAnswers.getAnnualCountryPercentage(), country);
        } else {
            return String.format("Your footprint is %.2f%% below the national average for %s.", Math.abs(annualAnswers.getAnnualCountryPercentage()), country);
        }
    }

    private String getGlobalComparisonText(AnnualAnswers annualAnswers) {
        if (annualAnswers.getAnnualGlobalPercentage() > 0) {
            return String.format("Your footprint is %.2f%% above the global emission target.", annualAnswers.getAnnualGlobalPercentage());
        } else {
            return String.format("Your footprint is %.2f%% below the global emission target.", Math.abs(annualAnswers.getAnnualGlobalPercentage()));
        }
    }
}
