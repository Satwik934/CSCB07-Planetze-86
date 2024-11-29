package com.example.planetze86;

import android.app.Dialog;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class FoodConsumptionTracking extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_foodconsumption_tracking);

        Button mealButton = findViewById(R.id.button_meal);
        mealButton.setOnClickListener(v -> {
            // Create a custom dialog
            Dialog dialog = new Dialog(FoodConsumptionTracking.this);
            dialog.setContentView(R.layout.activity_meal_logging);

            // Set specific width and height for the dialog
            dialog.getWindow().setLayout((int) (getResources().getDisplayMetrics().widthPixels * 0.8), ViewGroup.LayoutParams.WRAP_CONTENT);

            // Access elements in the dialog
            AutoCompleteTextView actvMealType = dialog.findViewById(R.id.actv_meal_type);
            EditText etServings = dialog.findViewById(R.id.et_servings);
            Button btnSaveFoodData = dialog.findViewById(R.id.btn_save_food_data);

            // Populate dropdown for meal type
            String[] mealTypes = {"Beef", "Pork", "Chicken", "Fish", "Plant-based"};
            ArrayAdapter<String> adapter = new ArrayAdapter<>(FoodConsumptionTracking.this, android.R.layout.simple_dropdown_item_1line, mealTypes);
            actvMealType.setAdapter(adapter);
            actvMealType.setOnClickListener(w -> actvMealType.showDropDown());

            // Set up the Save button logic
            btnSaveFoodData.setOnClickListener(view -> {
                String mealType = actvMealType.getText().toString();
                String servingsStr = etServings.getText().toString();

                if (mealType.isEmpty() || servingsStr.isEmpty()) {
                    Toast.makeText(FoodConsumptionTracking.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    int servings = Integer.parseInt(servingsStr);

                    // Save or log the data (can replace with actual storage logic)
                    Toast.makeText(FoodConsumptionTracking.this, "Saved: " + mealType + ", Servings: " + servings, Toast.LENGTH_SHORT).show();

                    dialog.dismiss();
                }
            });

            dialog.show();
        });
    }
}
