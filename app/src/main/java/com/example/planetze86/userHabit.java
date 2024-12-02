package com.example.planetze86;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.Gravity;


import androidx.activity.EdgeToEdge;
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
    HashMap<String, Integer> habitLog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_habit);

        habits = new ArrayList<>();
        habits = readHabitsFromFile(this);

        habitLog = new HashMap<>();
        fetchHabits();


    }




    private void populateYourHabitsSection(LinearLayout container) {
        // Set the background color for this section
        container.removeAllViews();

        if (habitLog == null || habitLog.isEmpty()) {
            // Add an empty message with padding
            TextView emptyMessage = new TextView(this);
            emptyMessage.setText("No habits found. Start logging your habits!");
            emptyMessage.setPadding(16, 16, 16, 16);
            emptyMessage.setGravity(Gravity.CENTER); // Center the text
            emptyMessage.setBackgroundColor(Color.parseColor("#A9BCD0")); // Ensure color consistency
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
                layoutParams.setMargins(16, 24, 16, 24);
                habitLayout.setLayoutParams(layoutParams);
                habitLayout.setPadding(16, 16, 16, 16);

                TextView habitTextView = new TextView(this);
                habitTextView.setText(habitName + " - Days logged: " + habitLog.get(habitName));
                habitTextView.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.check_circle, 0, 0, 0);
                habitTextView.setCompoundDrawablePadding(16);
                habitTextView.setLayoutParams(new LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.WRAP_CONTENT, 2));
                habitTextView.setMaxLines(2);
                habitTextView.setEllipsize(null);
                habitTextView.setTextSize(13);

                Button logButton = new Button(this);
                logButton.setText("Log");
                logButton.setBackgroundResource(R.drawable.rounded_button_sky_blue);
                logButton.setOnClickListener(v -> {
                    logHabit(habitName);
                    updateHabitLogToFirebase();
                    populateYourHabitsSection(container);
                });

                Button removeButton = new Button(this);
                removeButton.setText("Remove");
                removeButton.setBackgroundResource(R.drawable.rounded_button_dark_blue);
                removeButton.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.trash_can, 0, 0, 0);
                removeButton.setOnClickListener(v -> {
                    habitLog.remove(habitName);
                    updateHabitLogToFirebase();
                    populateYourHabitsSection(container);
                });

                habitLayout.addView(habitTextView);
                habitLayout.addView(logButton);
                habitLayout.addView(removeButton);
                container.addView(habitLayout);
            }
        }

        // Add a button at the bottom
        Button addHabitsButton = new Button(this);
        addHabitsButton.setText("Add Habits");
        addHabitsButton.setBackgroundResource(R.drawable.rounded_button_teal);
        addHabitsButton.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.plus_box, 0, 0, 0);
        addHabitsButton.setPadding(16, 16, 16, 16);
        addHabitsButton.setOnClickListener(v -> populateAllActivitiesSection(container));
        container.addView(addHabitsButton);
    }



    private void populateAllActivitiesSection(LinearLayout container) {
        // Set the background color for this section
        container.removeAllViews();

        if (habits == null || habits.isEmpty()) {
            // Add an empty message with padding
            TextView emptyMessage = new TextView(this);
            emptyMessage.setText("No activities found.");
            emptyMessage.setPadding(16, 16, 16, 16);
            emptyMessage.setGravity(Gravity.CENTER); // Center the text
            emptyMessage.setBackgroundColor(Color.parseColor("#D8DBE2")); // Ensure color consistency
            container.addView(emptyMessage);
        } else {
            for (int i = 0; i < habits.size(); i++) {
                Habit habit = habits.get(i);

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
                final int index = i;
                addButton.setOnClickListener(v -> {
                    logHabit(habit.getName());
                    habitLog.put(habit.getName(), 1);
                    habits.remove(index);
                    updateHabitLogToFirebase();
                    populateAllActivitiesSection(container);
                });

                habitLayout.addView(habitTextView);
                habitLayout.addView(addButton);
                container.addView(habitLayout);
            }
        }

        // Add a button at the bottom
        Button returnButton = new Button(this);
        returnButton.setText("Return");
        returnButton.setBackgroundResource(R.drawable.rounded_button_teal);
        returnButton.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.backarrow, 0, 0, 0);
        returnButton.setPadding(16, 16, 16, 16);
        returnButton.setOnClickListener(v -> populateYourHabitsSection(container));
        container.addView(returnButton);
    }







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

    public void logHabit(String habitName) {
        if (habitLog.containsKey(habitName)) {
            habitLog.put(habitName, habitLog.get(habitName) + 1); // Increment the logged days
        } else {
            habitLog.put(habitName, 1); // Add the habit with 1 day logged if not already present
        }

    }

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


}