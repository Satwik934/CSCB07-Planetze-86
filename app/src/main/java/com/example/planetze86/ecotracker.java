package com.example.planetze86;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ecotracker extends AppCompatActivity {

    Button menuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ecotracker);

        Button transportationButton = findViewById(R.id.transportation_button);
        Button foodConsumptionButton = findViewById(R.id.food_consumption_button);
        Button shoppingButton = findViewById(R.id.shopping_button);
        Button energyBillsButton = findViewById(R.id.energy_bills_button);
        // Redirects the button to open eco gauge.
        menuButton = findViewById(R.id.planetze_menu_button);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ecotracker.this, PlanetzeMenu.class);
                startActivity(intent);
            }
        });
        transportationButton.setOnClickListener(v -> {
            Intent intent = new Intent(ecotracker.this, TransportationTracking.class);
            startActivity(intent);
        });
        foodConsumptionButton.setOnClickListener(v -> {
            Intent intent = new Intent(ecotracker.this, FoodConsumptionTracking.class);
            startActivity(intent);
        });
        shoppingButton.setOnClickListener(v -> {
            Intent intent = new Intent(ecotracker.this, ShoppingTracking.class);
        });
    }
}
