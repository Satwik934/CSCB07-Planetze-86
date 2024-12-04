package com.example.planetze86;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.concurrent.ExecutionException;

public class EmissionDataWrapper {
    private final FirebaseManager firebaseManager;

    public EmissionDataWrapper() {
        firebaseManager = new FirebaseManager(); // Initialize the FirebaseManager instance
    }

    /**
     * Retrieves the total emissions for a specific date and category.
     *
     * @param date     The date for which emissions are retrieved (e.g., "01-12-2024").
     * @param category The category of emissions (e.g., "Transportation", "Shopping", "FoodConsumption").
     * @return The total emissions for the specified date and category.
     */
    public double getCategoryEmissions(String date, String category) {
        DatabaseReference categoryRef = firebaseManager.getUserRef().child("activities").child(date).child(category);

        Task<DataSnapshot> task = categoryRef.get();

        try {
            // Wait for the task to complete synchronously
            DataSnapshot snapshot = Tasks.await(task);

            double totalEmissions = 0.0;

            for (DataSnapshot activitySnapshot : snapshot.getChildren()) {
                Double emissions = activitySnapshot.child("emissions").getValue(Double.class);
                if (emissions != null) {
                    totalEmissions += emissions;
                }
            }

            return totalEmissions;

        } catch (ExecutionException | InterruptedException e) {
            Log.e("EmissionDataWrapper", "Failed to retrieve emissions: " + e.getMessage());
            return 0.0; // Return 0 if there's an error
        }
    }
}
