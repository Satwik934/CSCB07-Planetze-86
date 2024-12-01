package com.example.planetze86;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

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

public class ViewEmissionActivitiesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_emission_activities);

        ListView listView = findViewById(R.id.activities_list_view);
        String selectedDate = getIntent().getStringExtra("SELECTED_DATE");

        if (selectedDate == null || selectedDate.isEmpty()) {
            Toast.makeText(this, "No date selected", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String userId = currentUser.getUid();

        retrieveActivities(userId, selectedDate, activities -> {
            ArrayList<String> activityDetails = new ArrayList<>();

            for (EmissionActivityElement activity : activities) {
                activityDetails.add(activity.getDetails());
            }

            if (activityDetails.isEmpty()) {
                activityDetails.add("No activities logged for this date.");
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, activityDetails);
            listView.setAdapter(adapter);
        });
    }

    private void retrieveActivities(String userId, String date, OnActivitiesLoadedListener listener) {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(userId)
                .child("EmissionActivityMegaLog")
                .child(date);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<EmissionActivityElement> activities = new ArrayList<>();

                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    String type = childSnapshot.child("type").getValue(String.class);

                    if ("Transportation".equals(type)) {
                        TransportationActivityElement activity = childSnapshot.getValue(TransportationActivityElement.class);
                        if (activity != null) {
                            activities.add(activity);
                        }
                    }
                    else if("Shopping".equals(type)) {
                        ShoppingActivityElement activity = childSnapshot.getValue(ShoppingActivityElement.class);
                        if (activity != null) {
                            activities.add(activity);
                        }
                    }
                    else if("Food Consumption".equals(type)) {
                        FoodConsumptionActivityElement activity = childSnapshot.getValue(FoodConsumptionActivityElement.class);
                        if (activity != null) {
                            activities.add(activity);
                        }
                    }
                    // Add more types if needed
                }

                listener.onActivitiesLoaded(activities);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Error: " + error.getMessage());
                listener.onActivitiesLoaded(new ArrayList<>()); // Pass an empty list on failure
            }
        });
    }

    // Listener interface for callback
    interface OnActivitiesLoadedListener {
        void onActivitiesLoaded(ArrayList<EmissionActivityElement> activities);
    }

    // Callback interface
    interface OnUserLoadedListener {
        void onUserLoaded(User user);
    }

}
