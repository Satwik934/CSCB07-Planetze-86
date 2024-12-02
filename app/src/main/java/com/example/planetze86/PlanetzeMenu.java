package com.example.planetze86;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class PlanetzeMenu extends AppCompatActivity {

    // Declare CardView variables for all three buttons
    CardView ecoGaugeCard, ecoTrackerCard, recalculateCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planetze_menu);

        // Link CardViews to their XML counterparts
        ecoGaugeCard = findViewById(R.id.eco_gauge_card);
        ecoTrackerCard = findViewById(R.id.eco_tracker_card);
        recalculateCard = findViewById(R.id.recalculate_card);

        // Set OnClickListener for Eco Gauge Card
        ecoGaugeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to the Eco Gauge page
                Intent intent = new Intent(PlanetzeMenu.this, ecoGauge.class);
                startActivity(intent);
            }
        });

        // Set OnClickListener for Eco Tracker Card
        ecoTrackerCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Placeholder for Eco Tracker page
                // If the class isn't implemented, use Toast or log for now
                Intent intent = new Intent(PlanetzeMenu.this, EcoTracker.class); // Example class
                startActivity(intent);
            }
        });

        // Set OnClickListener for Recalculate Card
        recalculateCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to the Onboarding page
                Intent intent = new Intent(PlanetzeMenu.this, Onboarding.class);
                startActivity(intent);
            }
        });
    }
}
