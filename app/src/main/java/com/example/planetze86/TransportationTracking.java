package com.example.planetze86;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;

public class TransportationTracking extends AppCompatActivity {
    private EmissionActivityElement deserializeActivity(DataSnapshot snapshot) {
        String type = snapshot.child("type").getValue(String.class);

        if (type == null) {
            return null;
        }

        switch (type) {
            case "Transportation":
                return snapshot.getValue(TransportationActivityElement.class);
            case "Shopping":
                return snapshot.getValue(ShoppingActivityElement.class);
            case "Food Consumption":
                return snapshot.getValue(FoodConsumptionActivityElement.class);
            default:
                return null; // Unknown type
        }
    }

    private void saveToFirebase(EmissionActivityElement activity, String date) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference megaLogRef = reference.child(userId).child("EmissionActivityMegaLog");

            megaLogRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    HashMap<String, ArrayList<EmissionActivityElement>> activityMap = new HashMap<>();

                    // Retrieve existing data
                    if (snapshot.exists()) {
                        for (DataSnapshot dateSnapshot : snapshot.getChildren()) {
                            ArrayList<EmissionActivityElement> activities = new ArrayList<>();
                            for (DataSnapshot activitySnapshot : dateSnapshot.getChildren()) {
                                EmissionActivityElement existingActivity = deserializeActivity(activitySnapshot);
                                if (existingActivity != null) {
                                    activities.add(existingActivity);
                                }
                            }
                            activityMap.put(dateSnapshot.getKey(), activities);
                        }
                    }

                    // Add the new activity
                    ArrayList<EmissionActivityElement> dateActivities = activityMap.getOrDefault(date, new ArrayList<>());
                    dateActivities.add(activity);
                    activityMap.put(date, dateActivities);

                    // Save back to Firebase
                    megaLogRef.setValue(activityMap)
                            .addOnSuccessListener(aVoid -> Toast.makeText(
                                    TransportationTracking.this,
                                    "Data saved successfully!",
                                    Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(
                                    TransportationTracking.this,
                                    "Failed to save data: " + e.getMessage(),
                                    Toast.LENGTH_SHORT).show());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(
                            TransportationTracking.this,
                            "Failed to retrieve data: " + error.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String selectedDate = getIntent().getStringExtra("SELECTED_DATE");

        if (selectedDate != null && !selectedDate.isEmpty()) {
            Toast.makeText(this, "Selected Date: " + selectedDate, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No date selected", Toast.LENGTH_SHORT).show();
        }
        setContentView(R.layout.activity_transportation_tracking);



        Button personalVehicleButton = findViewById(R.id.button_personal_vehicle);
        Button publicTransportationButton = findViewById(R.id.button_public_transportation);
        Button cyclingOrWalkingButton = findViewById(R.id.button_cycling);
        Button flightButton = findViewById(R.id.button_flight);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int screenHeight = displayMetrics.heightPixels;
        int[] location = new int[2];
        // Personal Vehicle Dialog


        personalVehicleButton.setOnClickListener(v -> {
            Dialog dialog = new Dialog(TransportationTracking.this);
            dialog.setContentView(R.layout.activity_personal_vehicle_logging);

            dialog.getWindow().setLayout((int) (getResources().getDisplayMetrics().widthPixels * 0.8), ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.white);

            EditText etDistanceDriven = dialog.findViewById(R.id.et_distance_driven);
            AutoCompleteTextView actvVehicleType = dialog.findViewById(R.id.actv_vehicle_type);


            actvVehicleType.getLocationOnScreen(location);
            int availableHeight = screenHeight - location[1] - actvVehicleType.getHeight();
            int dropdownHeight = Math.min((int) (screenHeight * 0.3), availableHeight);
            actvVehicleType.setDropDownHeight(dropdownHeight);

            Button btnSaveVehicleData = dialog.findViewById(R.id.btn_save_vehicle_data);

            String[] vehicleTypes = {"Gasoline", "Diesel", "Hybrid", "Electric"};
            ArrayAdapter<String> vehicleAdapter = new ArrayAdapter<>(TransportationTracking.this, android.R.layout.simple_dropdown_item_1line, vehicleTypes);
            actvVehicleType.setAdapter(vehicleAdapter);
            actvVehicleType.setOnClickListener(w -> actvVehicleType.showDropDown());

            btnSaveVehicleData.setOnClickListener(view -> {
                String distance = etDistanceDriven.getText().toString();
                String vehicleType = actvVehicleType.getText().toString();

                if (distance.isEmpty() || vehicleType.isEmpty()) {
                    Toast.makeText(TransportationTracking.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    EmissionActivityElement activity = new TransportationActivityElement(
                            selectedDate,
                            "Personal Vehicle",
                            Double.parseDouble(distance),
                            vehicleType);

                    saveToFirebase(activity, selectedDate);
                    dialog.dismiss();
                }
            });

            dialog.show();
        });

        // Public Transportation Dialog
        publicTransportationButton.setOnClickListener(v -> {
            Dialog dialog = new Dialog(TransportationTracking.this);
            dialog.setContentView(R.layout.activity_public_transportation_logging);

            dialog.getWindow().setLayout((int) (getResources().getDisplayMetrics().widthPixels * 0.8), ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.white);

            EditText etTravelTime = dialog.findViewById(R.id.et_transportation_time);
            AutoCompleteTextView actvTransportType = dialog.findViewById(R.id.actv_transportation_type);
            Button btnSaveTransportData = dialog.findViewById(R.id.btn_save_transport_data);

            actvTransportType.getLocationOnScreen(location);
            int availableHeight = screenHeight - location[1] - actvTransportType.getHeight();
            int dropdownHeight = Math.min((int) (screenHeight * 0.3), availableHeight);
            actvTransportType.setDropDownHeight(dropdownHeight);

            String[] transportTypes = {"Bus", "Train", "Subway"};
            ArrayAdapter<String> transportAdapter = new ArrayAdapter<>(TransportationTracking.this, android.R.layout.simple_dropdown_item_1line, transportTypes);
            actvTransportType.setAdapter(transportAdapter);
            actvTransportType.setOnClickListener(w -> actvTransportType.showDropDown());

            btnSaveTransportData.setOnClickListener(view -> {
                String travelTime = etTravelTime.getText().toString();
                String transportType = actvTransportType.getText().toString();

                if (travelTime.isEmpty() || transportType.isEmpty()) {
                    Toast.makeText(TransportationTracking.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    EmissionActivityElement activity = new TransportationActivityElement(
                            selectedDate,
                            "Public Transport",
                            Double.parseDouble(travelTime),
                            transportType);

                    saveToFirebase(activity, selectedDate);
                    dialog.dismiss();
                }
            });

            dialog.show();
        });

        // Cycling or Walking Dialog
        cyclingOrWalkingButton.setOnClickListener(v -> {
            Dialog dialog = new Dialog(TransportationTracking.this);
            dialog.setContentView(R.layout.activity_cycling_or_walking_logging);

            dialog.getWindow().setLayout((int) (getResources().getDisplayMetrics().widthPixels * 0.8), ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.white);

            RadioGroup rgActivityType = dialog.findViewById(R.id.rg_activity_type);
            EditText etDistance = dialog.findViewById(R.id.et_distance);
            Button btnSaveActivityData = dialog.findViewById(R.id.btn_save_activity_data);

            btnSaveActivityData.setOnClickListener(view -> {
                int selectedActivityId = rgActivityType.getCheckedRadioButtonId();
                String activityType = selectedActivityId == R.id.rb_cycling ? "Cycling" : "Walking";
                String distance = etDistance.getText().toString();

                if (distance.isEmpty()) {
                    Toast.makeText(TransportationTracking.this, "Please enter the distance", Toast.LENGTH_SHORT).show();
                } else {
                    EmissionActivityElement activity = new TransportationActivityElement(
                            selectedDate,
                            activityType,
                            Double.parseDouble(distance),
                            activityType);

                    saveToFirebase(activity, selectedDate);
                    dialog.dismiss();
                }
            });

            dialog.show();
        });

        // Flight Dialog
        flightButton.setOnClickListener(v -> {
            Dialog dialog = new Dialog(TransportationTracking.this);
            dialog.setContentView(R.layout.activity_flight_logging);

            dialog.getWindow().setLayout((int) (getResources().getDisplayMetrics().widthPixels * 0.8), ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.white);

            EditText etFlightNumber = dialog.findViewById(R.id.et_flight_number);
            AutoCompleteTextView actvFlightType = dialog.findViewById(R.id.actv_flight_type);
            Button btnSaveFlightData = dialog.findViewById(R.id.btn_save_flight_data);

            String[] flightTypes = {"Short-Haul", "Long-Haul"};
            ArrayAdapter<String> flightAdapter = new ArrayAdapter<>(TransportationTracking.this, android.R.layout.simple_dropdown_item_1line, flightTypes);
            actvFlightType.setAdapter(flightAdapter);
            actvFlightType.setOnClickListener(w -> actvFlightType.showDropDown());

            btnSaveFlightData.setOnClickListener(view -> {
                String flightNumber = etFlightNumber.getText().toString();
                String flightType = actvFlightType.getText().toString();

                if (flightNumber.isEmpty() || flightType.isEmpty()) {
                    Toast.makeText(TransportationTracking.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    EmissionActivityElement activity = new TransportationActivityElement(
                            selectedDate,
                            "Flight",
                            Double.parseDouble(flightNumber),
                            flightType);

                    saveToFirebase(activity, selectedDate);
                    dialog.dismiss();
                }
            });

            dialog.show();
        });
    }
}
