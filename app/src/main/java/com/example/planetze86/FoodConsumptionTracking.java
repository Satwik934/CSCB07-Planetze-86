package com.example.planetze86;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class FoodConsumptionTracking extends AppCompatActivity {

    FirebaseManager firebaseManager = new FirebaseManager();
    private boolean isProgrammaticClick = false;
    FoodConsumptionActivityElement toBeEdited;
    @Override
    protected void onResume() {
        super.onResume();

        String activityId = getIntent().getStringExtra("ACTIVITY_ID");
        String selectedDate = getIntent().getStringExtra("SELECTED_DATE_UPDATE");

        if (activityId != null && selectedDate != null) {
            firebaseManager.retrieveActivitiesByType(
                    selectedDate,
                    "FoodConsumption",
                    FoodConsumptionActivityElement.class,
                    activities -> {
                        for (FoodConsumptionActivityElement activity : activities) {
                            if (activity.getId().equals(activityId)) {
                                Dialog dialog = new Dialog(this);
                                prefillFields(dialog, activity);
                                break;
                            }
                        }
                    }
            );
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        String selectedDate = getIntent().getStringExtra("SELECTED_DATE");
        String activityId = getIntent().getStringExtra("ACTIVITY_ID");
        String selectedDateUpdate = getIntent().getStringExtra("SELECTED_DATE_UPDATE");

        if (activityId != null && selectedDate != null) {
            firebaseManager.retrieveActivitiesByType(
                    selectedDateUpdate,
                    "FoodConsumption",
                    FoodConsumptionActivityElement.class,
                    activities -> {
                        for (FoodConsumptionActivityElement activity : activities) {
                            if (activity.getId().equals(activityId)) {
                                Dialog dialog = new Dialog(this);
                                prefillFields(dialog, activity);
                                break;
                            }
                        }
                    }
            );
        }
        if ((selectedDate != null && !selectedDate.isEmpty()) || (selectedDateUpdate != null && !selectedDateUpdate.isEmpty())) {
            Toast.makeText(this, "Selected Date: " + ((selectedDate != null) ? selectedDate : selectedDateUpdate), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No date selected", Toast.LENGTH_SHORT).show();
        }
        setContentView(R.layout.activity_foodconsumption_tracking);
        ImageButton backButton = findViewById(R.id.backButton);

        Button mealButton = findViewById(R.id.button_meal);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FoodConsumptionTracking.this, EcoTracker.class);
                startActivity(intent);
            }
        });
        mealButton.setOnClickListener(v -> {
            // Create a custom dialog
            Dialog dialog = new Dialog(FoodConsumptionTracking.this);
            dialog.setContentView(R.layout.activity_meal_logging);

            // Set specific width and height for the dialog
            dialog.getWindow().setLayout((int) (getResources().getDisplayMetrics().widthPixels * 0.8), ViewGroup.LayoutParams.WRAP_CONTENT);
            if(isProgrammaticClick){
                prefillMealDialog(dialog,toBeEdited);
            }
            else {
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

                        // Create FoodConsumptionActivityElement
                        FoodConsumptionActivityElement foodActivity = new FoodConsumptionActivityElement(
                                selectedDate,
                                mealType,
                                servings
                        );

                        // Save to Firebase
                        firebaseManager.saveActivity(selectedDate, "FoodConsumption", foodActivity);

                        Toast.makeText(FoodConsumptionTracking.this, "Saved: " + mealType + ", Servings: " + servings, Toast.LENGTH_SHORT).show();

                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
    }
    private void prefillFields(Dialog dialog, FoodConsumptionActivityElement activity) {
        toBeEdited = activity;

        isProgrammaticClick = true;
        findViewById(R.id.button_meal).performClick();
        isProgrammaticClick = false;
    }

    private void prefillMealDialog(Dialog dialog, FoodConsumptionActivityElement activity) {
        AutoCompleteTextView actvMealType = dialog.findViewById(R.id.actv_meal_type);
        EditText etServings = dialog.findViewById(R.id.et_servings);
        Button btnSaveFoodData = dialog.findViewById(R.id.btn_save_food_data);

        actvMealType.setText(activity.getMealType(), false);
        etServings.setText(String.valueOf(activity.getServings()));

        btnSaveFoodData.setOnClickListener(view -> {
            String mealType = actvMealType.getText().toString();
            String servingsStr = etServings.getText().toString();

            if (!mealType.isEmpty() && !servingsStr.isEmpty()) {
                activity.setMealType(mealType);
                activity.setServings(Integer.parseInt(servingsStr));
                firebaseManager.updateActivity(activity.getDate(), "FoodConsumption", activity);
                dialog.dismiss();
                finish();
            } else {
                Toast.makeText(this, "Please fill the fields.", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

}
