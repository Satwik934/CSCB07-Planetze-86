package com.example.planetze86;

import android.annotation.SuppressLint;
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

/**
 * Activity class for AnnualEmissionResult feature.
 * Provides a breakdown by transportation, food, housing and consumption annual C02e emissions.
 * Shows the user's Total annual C02e emissions and compare that with global and national benchmark.
 */
public class AnnualEmissionResult extends AppCompatActivity {

    // Firebase references
    private DatabaseReference databaseReference;

    // UI components
    private TextView totalFootprintValue, transportationValue, foodValue, housingValue, consumptionValue, comparisonText, globalComparisonText;
    private TextView labelTransportation, labelFood, labelHousing, labelConsumption;

    /**
     * Initializes the activity, binds UI components like all of them and displays everthing on the screen
     * Also, dashboard button navigate to eco tracker main page.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annual_emission_result);

        // Initialize Firebase Authentication and get the current user
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            // End activity if the user is not authenticated
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
        LoginActivityModel model = new LoginActivityModel();

        // Fetch and display the user's annual emission results
        fetchAnnualAnswers(currentUser.getUid());
        model.updateFirstLogin();

        // Set the dashboard button click listener to navigate to the dashboard
        dashboard.setOnClickListener(v -> {
            Intent intent = new Intent(AnnualEmissionResult.this, EcoTracker.class);
            startActivity(intent);
        });
    }

    /**
     * Fetches the annual emission data for the given user and displays based on the categories.
     *
     * @param userId identifies the user.
     */
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

                    // Update TextViews with the user's data
                    transportationValue.setText(String.format("%s Tons CO2e", transportation));
                    foodValue.setText(String.format("%s Tons CO2e", food));
                    housingValue.setText(String.format("%s Tons CO2e", housing));
                    consumptionValue.setText(String.format("%s Tons CO2e", consumption));
                    totalFootprintValue.setText(String.format("%s Tons CO2e", totalEmission));

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

    /**
     * Generates a comparison about how the user's footprint compares
     * to the national average for his country.
     */
    @SuppressLint("DefaultLocale")
    private String getComparisonText(AnnualAnswers annualAnswers) {
        String country = annualAnswers.getCountry();
        if (annualAnswers.getAnnualCountryPercentage() > 0) {
            return String.format("Your footprint is %.2f%% above the national average for %s.", annualAnswers.getAnnualCountryPercentage(), country);
        } else {
            return String.format("Your footprint is %.2f%% below the national average for %s.", Math.abs(annualAnswers.getAnnualCountryPercentage()), country);
        }
    }

    /**
     * Generates a comparison about how the user's footprint compares
     * to the global emission reduction target.
     */
    @SuppressLint("DefaultLocale")
    private String getGlobalComparisonText(AnnualAnswers annualAnswers) {
        if (annualAnswers.getAnnualGlobalPercentage() > 0) {
            return String.format("Your footprint is %.2f%% above the global emission target.", annualAnswers.getAnnualGlobalPercentage());
        } else {
            return String.format("Your footprint is %.2f%% below the global emission target.", Math.abs(annualAnswers.getAnnualGlobalPercentage()));
        }
    }
}
