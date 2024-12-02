package com.example.planetze86;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.function.Consumer;

public class FirebaseManager {
    private final DatabaseReference userRef;

    public FirebaseManager() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            userRef = FirebaseDatabase.getInstance().getReference("users").child(currentUser.getUid());
        } else {
            throw new IllegalStateException("User not logged in");
        }
    }

    /**
     * Save an activity under the "date -> type" hierarchy.
     */
    public <T extends EmissionActivityElement> void saveActivity(String date, String type, T activity) {
        DatabaseReference activityRef = userRef.child("activities").child(date).child(type).push();
        activity.setId(activityRef.getKey()); // Use Firebase-generated key as ID
        activityRef.setValue(activity)
                .addOnSuccessListener(aVoid -> Log.d("FirebaseManager", "Activity saved successfully"))
                .addOnFailureListener(e -> Log.e("FirebaseManager", "Failed to save activity: " + e.getMessage()));
    }


    /**
     * Retrieve activities of a specific type for a given date.
     */
    public <T> void retrieveActivitiesByType(String date, String type, Class<T> activityType, Consumer<ArrayList<T>> callback) {
        userRef.child("activities")
                .child(date)
                .child(type)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList<T> activities = new ArrayList<>();
                        for (DataSnapshot child : snapshot.getChildren()) {
                            T activity = child.getValue(activityType);
                            if (activity != null) {
                                activities.add(activity);
                            }
                        }
                        callback.accept(activities);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        System.err.println("Failed to retrieve activities: " + error.getMessage());
                        callback.accept(new ArrayList<>()); // Return empty list on failure
                    }
                });
    }

    /**
     * Retrieve all activities for a specific date.
     */
    public void retrieveAllActivitiesForDate(String date, Consumer<ArrayList<EmissionActivityElement>> callback) {
        userRef.child("activities").child(date).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<EmissionActivityElement> allActivities = new ArrayList<>();

                // Iterate through activity types (e.g., transportation, shopping)
                for (DataSnapshot typeSnapshot : snapshot.getChildren()) {
                    String activityType = typeSnapshot.getKey();

                    for (DataSnapshot activitySnapshot : typeSnapshot.getChildren()) {
                        EmissionActivityElement activity = null;

                        // Deserialize based on type
                        switch (activityType) {
                            case "Transportation":
                                activity = activitySnapshot.getValue(TransportationActivityElement.class);
                                break;
                            case "Shopping":
                                activity = activitySnapshot.getValue(ShoppingActivityElement.class);
                                break;
                            case "FoodConsumption":
                                activity = activitySnapshot.getValue(FoodConsumptionActivityElement.class);
                                break;
                            default:
                                Log.w("FirebaseManager", "Unknown activity type: " + activityType);
                                break;
                        }

                        if (activity != null) {
                            allActivities.add(activity);
                        }
                    }
                }

                callback.accept(allActivities);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseManager", "Failed to retrieve activities: " + error.getMessage());
                callback.accept(new ArrayList<>()); // Return an empty list on failure
            }
        });
    }


    public void calculateDailyEmissions(String date, Consumer<Double> callback) {
        DatabaseReference activitiesRef = FirebaseDatabase.getInstance()
                .getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("activities").child(date);

        activitiesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double totalEmissions = 0.0;

                for (DataSnapshot typeSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot activitySnapshot : typeSnapshot.getChildren()) {
                        // Attempt to parse the emission value dynamically
                        Double emissions = activitySnapshot.child("emissions").getValue(Double.class);
                        if (emissions != null) {
                            totalEmissions += emissions;
                        }
                    }
                }

                callback.accept(totalEmissions);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.err.println("Failed to retrieve emissions: " + error.getMessage());
                callback.accept(0.0); // Return 0 in case of failure
            }
        });
    }
    public void deleteActivity(String date, String type, String activityId, Consumer<Boolean> callback) {
        DatabaseReference typeRef = userRef.child("activities").child(date).child(type);

        typeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot activitySnapshot : snapshot.getChildren()) {
                    EmissionActivityElement storedActivity = deserializeActivity(activitySnapshot, type);
                    if (storedActivity != null && storedActivity.getId().equals(activityId)) {
                        // Remove the activity
                        activitySnapshot.getRef().removeValue()
                                .addOnSuccessListener(aVoid -> callback.accept(true))
                                .addOnFailureListener(e -> callback.accept(false));
                        return;
                    }
                }
                callback.accept(false); // If no matching activity is found
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseManager", "Failed to delete activity: " + error.getMessage());
                callback.accept(false);
            }
        });
    }


    /**
     * Deserializes a DataSnapshot into the correct activity type.
     *
     * @param snapshot The snapshot of the activity to deserialize.
     * @param activityType The type of the activity (e.g., "transportation", "shopping").
     * @return The deserialized EmissionActivityElement object.
     */
    private EmissionActivityElement deserializeActivity(DataSnapshot snapshot, String activityType) {
        switch (activityType) {
            case "Transportation":
                return snapshot.getValue(TransportationActivityElement.class);
            case "Shopping":
                return snapshot.getValue(ShoppingActivityElement.class);
            case "FoodConsumption":
                return snapshot.getValue(FoodConsumptionActivityElement.class);
            default:
                Log.w("FirebaseManager", "Unknown activity type: " + activityType);
                return null;
        }
    }


}
