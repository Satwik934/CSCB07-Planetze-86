package com.example.planetze86;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PlanetzeMenu extends AppCompatActivity {

    Button gaugeButton;
    Button annualButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_planetze_menu);

        // Redirects the button to open eco gauge.
        annualButton = findViewById(R.id.annual_button);
        gaugeButton = findViewById(R.id.eco_gauge_button);
        gaugeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlanetzeMenu.this, ecoGauge.class);
                startActivity(intent);
            }
        });
        annualButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlanetzeMenu.this, Onboarding.class);
                startActivity(intent);
            }
        });
    }
}
