package com.example.planetze86;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class TransportationTracking extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_transportation_tracking);

        Button personalVehicleButton = findViewById(R.id.button_personal_vehicle);
        Button publicTransportationButton = findViewById(R.id.button_public_transportation);
        Button cyclingButton = findViewById(R.id.button_cycling);
        Button walkingButton = findViewById(R.id.button_walking);
        Button flightButton = findViewById(R.id.button_flight);

        // Personal Vehicle Dialog
        personalVehicleButton.setOnClickListener(v -> {
            Dialog dialog = new Dialog(TransportationTracking.this);
            dialog.setContentView(R.layout.activity_personal_vehicle_logging);

            dialog.getWindow().setLayout((int) (getResources().getDisplayMetrics().widthPixels * 0.8), ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.white);

            EditText etDistanceDriven = dialog.findViewById(R.id.et_distance_driven);
            AutoCompleteTextView actvVehicleType = dialog.findViewById(R.id.actv_vehicle_type);
            Button btnSaveVehicleData = dialog.findViewById(R.id.btn_save_vehicle_data);

            String[] vehicleTypes = {"Gasoline", "Electric", "Hybrid"};
            ArrayAdapter<String> vehicleAdapter = new ArrayAdapter<>(TransportationTracking.this, android.R.layout.simple_dropdown_item_1line, vehicleTypes);
            actvVehicleType.setAdapter(vehicleAdapter);
            actvVehicleType.setOnClickListener(w -> actvVehicleType.showDropDown());

            btnSaveVehicleData.setOnClickListener(view -> {
                String distance = etDistanceDriven.getText().toString();
                String vehicleType = actvVehicleType.getText().toString();

                if (distance.isEmpty() || vehicleType.isEmpty()) {
                    Toast.makeText(TransportationTracking.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(TransportationTracking.this, "Data Saved: " + distance + " km, " + vehicleType, Toast.LENGTH_SHORT).show();
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

            String[] transportTypes = {"Bus", "Train", "Metro"};
            ArrayAdapter<String> transportAdapter = new ArrayAdapter<>(TransportationTracking.this, android.R.layout.simple_dropdown_item_1line, transportTypes);
            actvTransportType.setAdapter(transportAdapter);
            actvTransportType.setOnClickListener(w -> actvTransportType.showDropDown());

            btnSaveTransportData.setOnClickListener(view -> {
                String travelTime = etTravelTime.getText().toString();
                String transportType = actvTransportType.getText().toString();

                if (travelTime.isEmpty() || transportType.isEmpty()) {
                    Toast.makeText(TransportationTracking.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(TransportationTracking.this, "Data Saved: " + travelTime + " min, " + transportType, Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });

            dialog.show();
        });

        // Cycling or Walking Dialog
        cyclingButton.setOnClickListener(v -> {
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
                    Toast.makeText(TransportationTracking.this, "Data Saved: " + distance + " km (" + activityType + ")", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(TransportationTracking.this, "Data Saved: " + flightNumber + " flights (" + flightType + ")", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });

            dialog.show();
        });
    }
}
