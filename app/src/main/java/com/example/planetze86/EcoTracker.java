package com.example.planetze86;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class EcoTracker extends AppCompatActivity {

    Button menuButton;
    Button transportationButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ecotracker);

        transportationButton = findViewById(R.id.transportation_button);
        Button foodConsumptionButton = findViewById(R.id.food_consumption_button);
        Button shoppingButton = findViewById(R.id.shopping_button);
        Button energyBillsButton = findViewById(R.id.energy_bills_button);
        Button dateSelectButton = findViewById(R.id.date_select_button);
        TextView tvSelectedDate = findViewById(R.id.tv_selected_date);
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
                startActivity(intent);
            }
        });
        foodConsumptionButton.setOnClickListener(v -> {
            Intent intent = new Intent(EcoTracker.this, FoodConsumptionTracking.class);
            startActivity(intent);
        });
        shoppingButton.setOnClickListener(v -> {
            Intent intent = new Intent(EcoTracker.this, ShoppingTracking.class);
            startActivity(intent);
        });
        energyBillsButton.setOnClickListener(v -> {
            Intent intent = new Intent(EcoTracker.this, EnergyBills.class);
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
                        String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        tvSelectedDate.setText("Selected Date: " + selectedDate);
                    },
                    year, month, day);

            datePickerDialog.show();
        });
    }
}
