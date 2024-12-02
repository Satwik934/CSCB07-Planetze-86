package com.example.planetze86;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class EcoTracker extends AppCompatActivity {
    FirebaseManager firebaseManager = new FirebaseManager();
    private void updateEmissions(TextView tvEmissions, String date) {
        firebaseManager.calculateDailyEmissions(date, totalEmissions ->
                tvEmissions.setText(String.format(Locale.getDefault(), "%.2f kg CO2e", totalEmissions))
        );
    }

    Button menuButton;
    Button transportationButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ecotracker);
        TextView tvEmissions = findViewById(R.id.daily_emissions_value);
        transportationButton = findViewById(R.id.transportation_button);
        Button foodConsumptionButton = findViewById(R.id.food_consumption_button);
        Button shoppingButton = findViewById(R.id.shopping_button);
        Button dateSelectButton = findViewById(R.id.date_select_button);
        Button viewActivitiesButton = findViewById(R.id.view_activities_button);
        Button habitButton = findViewById(R.id.habitButton);
        TextView tvSelectedDate = findViewById(R.id.tv_selected_date);
        final String selectedDateDefault = (new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())).format(new Date());
        tvSelectedDate.setText(("Selected Date: " + selectedDateDefault));
        updateEmissions(tvEmissions,selectedDateDefault);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        // Initialize emissions for the default selected date
        //updateEmissions(tvEmissions, tvSelectedDate.getText().toString().replace("Selected Date: ", ""), currentUser);
        // Redirects the button to open eco gauge.
        menuButton = findViewById(R.id.planetze_menu_button);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EcoTracker.this, PlanetzeMenu.class);
                startActivity(intent);
            }
        });
        transportationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(EcoTracker.this, TransportationTracking.class);
                intent.putExtra("SELECTED_DATE",tvSelectedDate.getText().toString().replace("Selected Date: ",""));
                startActivity(intent);
            }
        });
        foodConsumptionButton.setOnClickListener(v -> {
            Intent intent = new Intent(EcoTracker.this, FoodConsumptionTracking.class);
            intent.putExtra("SELECTED_DATE",tvSelectedDate.getText().toString().replace("Selected Date: ",""));
            startActivity(intent);
        });
        shoppingButton.setOnClickListener(v -> {
            Intent intent = new Intent(EcoTracker.this, ShoppingTracking.class);
            intent.putExtra("SELECTED_DATE",tvSelectedDate.getText().toString().replace("Selected Date: ",""));
            startActivity(intent);
        });

        habitButton.setOnClickListener(v -> {

            Intent intent = new Intent(EcoTracker.this, userHabit.class);

            startActivity(intent);
        });


        dateSelectButton.setOnClickListener(v -> {
            // Get the current date
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            // Show DatePickerDialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    EcoTracker.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        // Update the TextView with the selected date
                        String chosenDate = selectedDay + "-" + (selectedMonth + 1) + "-" + selectedYear;
                        tvSelectedDate.setText("Selected Date: " + chosenDate);
                        // Update emissions for the newly selected date
                        updateEmissions(tvEmissions, chosenDate);


                        // Update the real-time listener for the new date
                        addRealTimeListener(tvEmissions, chosenDate);
                    },
                    year, month, day);

            datePickerDialog.show();
        });
        viewActivitiesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EcoTracker.this, ViewEmissionActivitiesActivity.class);
                intent.putExtra("SELECTED_DATE", tvSelectedDate.getText().toString().replace("Selected Date: ", ""));
                startActivity(intent);
            }
        });

    }
    /**
     * Adds a real-time listener to the selected date's activities in Firebase.
     */
    private ValueEventListener currentListener;

    private void addRealTimeListener(TextView tvEmissions, String date) {
        DatabaseReference activitiesRef = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("activities")
                .child(date);

        // Remove the current listener if it exists
        if (currentListener != null) {
            activitiesRef.removeEventListener(currentListener);
        }

        currentListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Update emissions whenever the activities for the date change
                updateEmissions(tvEmissions, date);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.err.println("Failed to listen for activity updates: " + error.getMessage());
            }
        };

        activitiesRef.addValueEventListener(currentListener);
    }

}
