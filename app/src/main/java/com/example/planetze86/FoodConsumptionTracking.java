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
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class FoodConsumptionTracking extends AppCompatActivity {
    /*private EmissionActivityElement deserializeActivity(DataSnapshot snapshot) {
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
                                    FoodConsumptionTracking.this,
                                    "Data saved successfully!",
                                    Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(
                                    FoodConsumptionTracking.this,
                                    "Failed to save data: " + e.getMessage(),
                                    Toast.LENGTH_SHORT).show());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(
                            FoodConsumptionTracking.this,
                            "Failed to retrieve data: " + error.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }*/

    FirebaseManager firebaseManager = new FirebaseManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        String selectedDate = getIntent().getStringExtra("SELECTED_DATE");
        if (selectedDate != null && !selectedDate.isEmpty()) {
            Toast.makeText(this, "Selected Date: " + selectedDate, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No date selected", Toast.LENGTH_SHORT).show();
        }
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
        });
    }
}
