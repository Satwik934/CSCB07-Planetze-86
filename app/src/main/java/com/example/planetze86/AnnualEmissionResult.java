package com.example.planetze86;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AnnualEmissionResult extends AppCompatActivity {

    // Firebase references
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private Button dashboard;

    // UI components
    private TextView totalFootprintValue, transportationValue, foodValue, housingValue, consumptionValue, comparisonText;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_annual_emission_result);


        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        dashboard = findViewById(R.id.dashboard_button);
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        // Bind UI elements
        totalFootprintValue = findViewById(R.id.total_footprint_value);
        transportationValue = findViewById(R.id.transportation_value);
        foodValue = findViewById(R.id.food_value);
        housingValue = findViewById(R.id.housing_value);
        consumptionValue = findViewById(R.id.consumption_value);
        comparisonText = findViewById(R.id.comparison_text);

        // Fetch and display the data
        fetchAnnualAnswers();

        dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to ForgotPassActivity
                Intent intent = new Intent(AnnualEmissionResult.this, ecotracker.class);
                startActivity(intent);
            }
        });
    }

    private void fetchAnnualAnswers() {
        String userId = auth.getCurrentUser().getUid(); // Get the current user's ID

        // Fetch data from Firebase
        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Deserialize the User object
                User user = dataSnapshot.getValue(User.class);
                if (user != null && user.getAnnualAnswers() != null) {
                    AnnualAnswers annualAnswers = user.getAnnualAnswers();

                    // Update the UI with the fetched data
                    totalFootprintValue.setText(String.format("%s Tons CO2e", annualAnswers.getAnnualEmission()));
                    transportationValue.setText(String.format("%s Tons CO2e", annualAnswers.getAnnualTransportation()));
                    foodValue.setText(String.format("%s Tons CO2e", annualAnswers.getAnnualFood()));
                    housingValue.setText(String.format("%s Tons CO2e", annualAnswers.getAnnualHousing()));
                    consumptionValue.setText(String.format("%s Tons CO2e", annualAnswers.getAnnualConsumption()));
                    if (annualAnswers.getAnnualCountryPercentage() > 0){
                        comparisonText.setText(String.format("Your footprint is %s%% above the national average for %s.", annualAnswers.getAnnualCountryPercentage(), annualAnswers.getCountry()));
                    }
                    if (annualAnswers.getAnnualCountryPercentage() < 0){
                        int num = (int) (annualAnswers.getAnnualCountryPercentage() * (-1));
                        comparisonText.setText(String.format("Your footprint is %s%% below the national average for %s.", num, annualAnswers.getCountry()));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
                System.err.println("Error fetching data: " + databaseError.getMessage());
            }
        });
    }
}
