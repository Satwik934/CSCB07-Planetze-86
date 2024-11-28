package com.example.planetze86;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class ShoppingTracking extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_shoppingconsumption_tracking);
        Button buyNewClothes = findViewById(R.id.button_buy_new_clothes);
        Button buyElectronics = findViewById(R.id.button_buy_electronics);
        Button otherPurchases = findViewById(R.id.button_other_purchases);
        Button energyBills = findViewById(R.id.button_energy_bills);

    }

}
