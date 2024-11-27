package com.example.planetze86;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class TransportationTracking extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_transportation_tracking);
        Button personalVehicle = findViewById(R.id.button_personal_vehicle);
        Button publicTransportation = findViewById(R.id.button_public_transportation);
        Button cycling = findViewById(R.id.button_cycling);
        Button walking = findViewById(R.id.button_walking);
        Button flight = findViewById(R.id.button_flight);


    }

}
