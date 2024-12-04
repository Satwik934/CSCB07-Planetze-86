package com.example.planetze86;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.view.Gravity;


import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class userHabit extends AppCompatActivity {
    List<Habit> habits;
    String[] suggested = {""};
    HashMap<String, Integer> habitLog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_habit);
        ImageButton backButton = findViewById(R.id.backButton);
        habits = new ArrayList<>();
        habits = readHabitsFromFile(this);

        habitLog = new HashMap<>();
        getSuggested();
        fetchHabits();
        backButton.bringToFront();
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(userHabit.this, EcoTracker.class);
                startActivity(intent);
            }
        });


    }




    private void populateYourHabitsSection(LinearLayout container) {
        // Clear the container and set the background color
        container.removeAllViews();

        if (habitLog == null || habitLog.isEmpty()) {
            // Add an empty message with padding
            TextView emptyMessage = new TextView(this);
            emptyMessage.setText("No habits found. Start logging your habits!");
            emptyMessage.setPadding(16, 16, 16, 16);
            emptyMessage.setGravity(Gravity.CENTER);
            emptyMessage.setBackgroundColor(Color.parseColor("#A9BCD0"));
            container.addView(emptyMessage);
        } else {
            for (String habitName : habitLog.keySet()) {
                // Create a layout for each habit item
                LinearLayout habitLayout = new LinearLayout(this);
                habitLayout.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                layoutParams.setMargins(8, 8, 8, 8); // Adjusted margins for spacing
                habitLayout.setLayoutParams(layoutParams);
                habitLayout.setPadding(12, 32, 12, 32); // Adjusted padding for compact design

                // TextView for habit details
                TextView habitTextView = new TextView(this);
                habitTextView.setText(habitName + " - Days logged: " + habitLog.get(habitName));
                habitTextView.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.check_circle, 0, 0, 0);
                habitTextView.setCompoundDrawablePadding(16);
                habitTextView.setLayoutParams(new LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.WRAP_CONTENT, 8)); // Adjust weight to give habitTextView more space
                habitTextView.setMaxLines(Integer.MAX_VALUE); // No limit on the number of lines
                habitTextView.setEllipsize(null); // Ensure no ellipsis behavior
                habitTextView.setTextSize(14);
                habitTextView.setPadding(16, 16, 16, 16);

                // Log button
                Button logButton = new Button(this);
                logButton.setText("Log");
                logButton.setTextSize(12); // Smaller text size
                logButton.setPadding(8, 4, 8, 4); // Smaller padding
                logButton.setBackgroundResource(R.drawable.rounded_button_sky_blue);
                LinearLayout.LayoutParams logButtonParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                logButtonParams.setMargins(0, 0, 0, 0); // Spacing between buttons
                logButton.setLayoutParams(logButtonParams);
                logButton.setOnClickListener(v -> {
                    logHabit(habitName);
                    updateHabitLogToFirebase();
                    populateYourHabitsSection(container);
                });

                // Remove button
                Button removeButton = new Button(this);
                removeButton.setText("Remove");
                removeButton.setTextSize(12); // Smaller text size
                removeButton.setPadding(0, 0, 0, 0); // Smaller padding
                removeButton.setBackgroundResource(R.drawable.rounded_button_dark_blue);
                removeButton.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.trash_can, 0, 0, 0);
                removeButton.setCompoundDrawablePadding(8); // Add some spacing for icon
                LinearLayout.LayoutParams removeButtonParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                removeButtonParams.setMargins(8, 0, 8, 0); // Spacing between buttons
                removeButton.setLayoutParams(removeButtonParams);
                removeButton.setOnClickListener(v -> {
                    habitLog.remove(habitName);
                    updateHabitLogToFirebase();
                    populateYourHabitsSection(container);
                });

                // Add components to the habit layout
                habitLayout.addView(habitTextView);
                habitLayout.addView(logButton);
                habitLayout.addView(removeButton);
                container.addView(habitLayout);
            }
        }

        // Add button at the bottom
        Button addHabitsButton = new Button(this);
        addHabitsButton.setText("Add Habits");
        addHabitsButton.setTextSize(14); // Smaller text size
        addHabitsButton.setBackgroundResource(R.drawable.rounded_button_teal);
        addHabitsButton.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.plus_box, 0, 0, 0);
        addHabitsButton.setCompoundDrawablePadding(12); // Spacing for icon
        addHabitsButton.setPadding(16, 12, 16, 12); // Adjusted padding
        addHabitsButton.setOnClickListener(v -> populateAllActivitiesSection(container));
        container.addView(addHabitsButton);
    }


    private void populateAllActivitiesSection(LinearLayout container) {
        container.removeAllViews();
        habits = readHabitsFromFile(this);

        // Add filter dropdown (Spinner)
        Spinner filterSpinner = new Spinner(this);
        String[] filterOptions = {"All","Suggested", "High Impact", "Medium Impact", "Low Impact","Transportation", "Energy","Food","Consumption"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, filterOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterSpinner.setAdapter(adapter);

        // Add a listener to handle filter selection
        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedFilter = filterOptions[position];
                List<Habit> filteredHabits = applyFilter(selectedFilter);
                displayHabits(container, filteredHabits);  // Refresh the displayed habits
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No filter applied, show all habits
                displayHabits(container, habits);
            }
        });

        container.addView(filterSpinner);

        // Initially display all habits
        displayHabits(container, habits);
    }

    // Helper method to apply the selected filter
    private List<Habit> applyFilter(String filter) {
        if (filter.equals("All")) {
            return new ArrayList<>(habits);  // No filter, return all habits
        }

        List<Habit> filteredHabits = new ArrayList<>();
        for (Habit habit : habits) {
            switch (filter) {
                case "Suggested":
                    if (habit.getCategory().equalsIgnoreCase(suggested[0])) {
                        filteredHabits.add(habit);
                    }
                    break;
                case "High Impact":
                    if (habit.getImpact().equals("High")) {
                        filteredHabits.add(habit);
                    }
                    break;
                case "Low Impact":
                    if (habit.getImpact().equals("Low")) {
                        filteredHabits.add(habit);
                    }
                    break;
                case "Medium Impact":
                    if (habit.getImpact().equals("Medium")) {
                        filteredHabits.add(habit);
                    }
                    break;
                case "Transportation":
                    if (habit.getCategory().equalsIgnoreCase("Transportation")) {
                        filteredHabits.add(habit);
                    }
                    break;
                case "Energy":
                    if (habit.getCategory().equalsIgnoreCase("Energy")) {
                        filteredHabits.add(habit);
                    }
                    break;
                case "Food":
                    if (habit.getCategory().equalsIgnoreCase("Food")) {
                        filteredHabits.add(habit);
                    }
                    break;
                case "Consumption":
                    if (habit.getCategory().equalsIgnoreCase("Consumption")) {
                        filteredHabits.add(habit);
                    }
                    break;
            }
        }
        return filteredHabits;
    }

    // Helper method to display habits in the container
    private void displayHabits(LinearLayout container, List<Habit> displayedHabits) {
        container.removeViews(1, container.getChildCount() - 1);  // Keep the Spinner at the top


        if (displayedHabits == null || displayedHabits.isEmpty()) {
            TextView emptyMessage = new TextView(this);
            emptyMessage.setText("No activities found for the selected filter.");
            container.addView(emptyMessage);

        }

        for (Habit habit : displayedHabits) {
            if (habitLog.containsKey(habit.getName())) {
                continue; // Skip this habit if it's already logged
            }
            // Create a container for each habit
            LinearLayout habitLayout = new LinearLayout(this);
            habitLayout.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams.setMargins(16, 24, 16, 24);
            habitLayout.setLayoutParams(layoutParams);
            habitLayout.setPadding(16, 16, 16, 16);
            TextView habitTextView = new TextView(this);
            habitTextView.setText(habit.getName() + " (" + habit.getCategory() + ") - Impact: " + habit.getImpact());
            int iconRes;
            //add icons
            switch (habit.getCategory().toLowerCase()) {
                case "transportation":
                    iconRes = R.drawable.car;
                    break;
                case "energy":
                    iconRes = R.drawable.light_bulb;
                    break;
                case "food":
                    iconRes = R.drawable.apple;
                    break;
                case "consumption":
                    iconRes = R.drawable.shopping;
                    break;
                default:
                    iconRes = R.drawable.check_circle;
            }
            habitTextView.setCompoundDrawablesWithIntrinsicBounds(iconRes, 0, 0, 0);
            habitTextView.setCompoundDrawablePadding(16);
            habitTextView.setLayoutParams(new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 2));
            habitTextView.setMaxLines(2);
            habitTextView.setEllipsize(null);
            habitTextView.setTextSize(13);

            Button addButton = new Button(this);
            addButton.setText("Add");
            addButton.setBackgroundResource(R.drawable.rounded_button_sky_blue);
            //button for logging habits
            addButton.setOnClickListener(v -> {
                logHabit(habit.getName());
                habitLog.put(habit.getName(), 1);
                displayedHabits.remove(habit);
                updateHabitLogToFirebase();
                displayHabits(container, displayedHabits);
            });

            habitLayout.addView(habitTextView);
            habitLayout.addView(addButton);
            container.addView(habitLayout);
        }

        Button returnButton = new Button(this);
        returnButton.setText("Return");
        returnButton.setBackgroundResource(R.drawable.rounded_button_teal);
        returnButton.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.backarrow, 0, 0, 0);
        returnButton.setPadding(16, 16, 16, 16);
        returnButton.setOnClickListener(v -> fetchHabits());
        container.addView(returnButton);
    }



    //updates user habits in firebase
    private void updateHabitLogToFirebase() {
        // Get the current user's UID from FirebaseAuth
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Reference to the user's habitLog in Firebase Realtime Database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference("users").child(userId).child("habitLog");

        // Update the habitLog in Firebase
        databaseReference.setValue(habitLog).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("userHabit", "habitLog updated successfully in Firebase");
            } else {
                Log.e("userHabit", "Failed to update habitLog in Firebase", task.getException());
            }
        });
    }
    //to populate habits list from habit text file in raw resource
    private List<Habit> readHabitsFromFile(Context context) {
        List<Habit> habits = new ArrayList<>();

        try {
            // Read the habits.txt file from the assets folder
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(context.getResources().openRawResource(R.raw.habits_list))
            );

            String line;
            String name = "";
            String category = "";
            String impact = "";
            int lineCount = 0;

            // Read the file line by line
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                // Skip blank lines
                if (line.isEmpty()) {
                    continue;
                }

                else{
                    if (lineCount % 3 == 0) {
                        name = line;
                    } else if (lineCount % 3 == 1) {
                        category = line;
                    } else {
                        impact = line;

                        // Create Habit object and add to the list
                        habits.add(new Habit(name, category, impact));
                    }

                    lineCount++;
                }


            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return habits;
    }

    //to add habits to user habits
    public void logHabit(String habitName) {
        if (habitLog.containsKey(habitName)) {
            habitLog.put(habitName, habitLog.get(habitName) + 1); // Increment the logged days
        } else {
            habitLog.put(habitName, 1); // Add the habit with 1 day logged if not already present
        }

    }
    //to fetch user habits from firebase
    public void fetchHabits() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            Log.e("userHabit", "No user is signed in");
            finish();
            return;
        }

        String userId = auth.getCurrentUser().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference("users").child(userId);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);

                    if (user != null) {
                        habitLog = user.getHabitLog();
                        if (habitLog == null) {
                            habitLog = new HashMap<>();
                        }
                    }
                }

                // Populate UI after data is fetched
                LinearLayout yourHabitsSection = findViewById(R.id.your_habits_section);
                populateYourHabitsSection(yourHabitsSection);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("userHabit", "Error fetching habit log", databaseError.toException());
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Update the habitLog to Firebase when the activity is paused (user leaves)
        updateHabitLogToFirebase();
    }
    @Override
    protected void onResume() {
        super.onResume();

        // Fetch the latest habit log from Firebase when the user returns to the activity
        fetchHabits();
    }


   //to set suggested category to the one with most emissions
    private void getSuggested(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            Log.e("getSuggested", "No user is signed in");
        }

        String userId = auth.getCurrentUser().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference("users");

        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null && user.getAnnualAnswers() != null) {
                    AnnualAnswers annualAnswers = user.getAnnualAnswers();

                    // Ensure non-null and valid data
                    float transportation = (float) annualAnswers.getAnnualTransportation();
                    float food = (float) annualAnswers.getAnnualFood();
                    float housing = (float) annualAnswers.getAnnualHousing();
                    float consumption = (float) annualAnswers.getAnnualConsumption();


                    // Update TextViews
                    float maxEmission = 0;
                    if (transportation > maxEmission) {
                        maxEmission = transportation;
                        suggested[0] = "Transportation";
                    }
                    if (food > maxEmission) {
                        maxEmission = food;
                        suggested[0] = "Food";
                    }
                    if (housing > maxEmission) {
                        maxEmission = housing;
                        suggested[0] = "Energy";
                    }
                    if (consumption > maxEmission) {
                        maxEmission = consumption;
                        suggested[0] = "Consumption";
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("AnnualEmissionResult", "Database error: " + error.getMessage());
            }
        });



    }


}