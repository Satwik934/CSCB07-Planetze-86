package com.example.planetze86;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ViewEmissionActivitiesActivity extends AppCompatActivity {

    private FirebaseManager firebaseManager;
    private ListView listView;
    private String selectedDate;
    private ArrayList<String> activityDetails;
    private ArrayList<EmissionActivityElement> activityObjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_emission_activities);

        firebaseManager = new FirebaseManager();
        listView = findViewById(R.id.activities_list_view);
        selectedDate = getIntent().getStringExtra("SELECTED_DATE");

        if (selectedDate == null || selectedDate.isEmpty()) {
            Toast.makeText(this, "No date selected", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        fetchActivities();
    }

    private void fetchActivities() {
        firebaseManager.retrieveAllActivitiesForDate(selectedDate, activities -> {
            activityDetails = new ArrayList<>();
            activityObjects = new ArrayList<>();

            for (EmissionActivityElement activity : activities) {
                activityDetails.add(activity.getDetails());
                activityObjects.add(activity); // Keep track of the actual objects
            }

            if (activityDetails.isEmpty()) {
                activityDetails.add("No activities logged for this date.");
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, activityDetails);
            listView.setAdapter(adapter);

            // Set up long-press listener for edit/delete options
            listView.setOnItemLongClickListener((parent, view, position, id) -> {
                if (position >= activityObjects.size()) {
                    return false; // Ignore if it's the "No activities" message
                }
                showEditDeleteDialog(position);
                return true;
            });
        });
    }

    private void showEditDeleteDialog(int position) {
        String[] options = {"Edit", "Delete"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose an action")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        // Edit selected
                        //editActivity(position);
                    } else if (which == 1) {
                        // Delete selected
                        deleteActivity(position);
                    }
                })
                .show();
    }


    private void deleteActivity(int position) {
        EmissionActivityElement selectedActivity = activityObjects.get(position);

        new AlertDialog.Builder(this)
                .setTitle("Confirm Delete")
                .setMessage("Are you sure you want to delete this activity?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    firebaseManager.deleteActivity(
                            selectedDate,
                            selectedActivity.getType(),
                            selectedActivity.getId(),
                            success -> {
                                if (success) {
                                    Toast.makeText(this, "Activity deleted successfully!", Toast.LENGTH_SHORT).show();
                                    fetchActivities(); // Refresh the list
                                } else {
                                    Toast.makeText(this, "Failed to delete activity.", Toast.LENGTH_SHORT).show();
                                }
                            });
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    /**
     * Retrieves transportation activities for the selected date.
     */
    /*public void retrieveTransportationActivities() {
        firebaseManager.retrieveActivities("transportationActivities", selectedDate, TransportationActivityElement.class, activities -> {
            if (!activities.isEmpty()) {
                for (TransportationActivityElement activity : activities) {
                    activityDetails.add(activity.getDetails());
                }
            } else {
                activityDetails.add("No transportation activities logged for this date.");
            }
            updateListView();
        });
    }

    /**
     * Retrieves shopping activities for the selected date.
     */
    /*public void retrieveShoppingActivities() {
        firebaseManager.retrieveActivities("shoppingActivities", selectedDate, ShoppingActivityElement.class, activities -> {
            if (!activities.isEmpty()) {
                for (ShoppingActivityElement activity : activities) {
                    activityDetails.add("Shopping: " + activity.getType() +
                            ", Quantity: " + activity.getQuantity());
                }
            } else {
                activityDetails.add("No shopping activities logged for this date.");
            }
            updateListView();
        });
    }

    /**
     * Retrieves food consumption activities for the selected date.
     */
    /*public void retrieveFoodConsumptionActivities() {
        firebaseManager.retrieveActivities("foodConsumptionActivities", selectedDate, FoodConsumptionActivityElement.class, activities -> {
            if (!activities.isEmpty()) {
                for (FoodConsumptionActivityElement activity : activities) {
                    activityDetails.add("Food Consumption: " + activity.getMealType() +
                            ", Servings: " + activity.getServings());
                }
            } else {
                activityDetails.add("No food consumption activities logged for this date.");
            }
            updateListView();
        });
    }*/

    /**
     * Updates the ListView with the current activity details.
     */
    private void updateListView() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, activityDetails);
        listView.setAdapter(adapter);
    }
}
