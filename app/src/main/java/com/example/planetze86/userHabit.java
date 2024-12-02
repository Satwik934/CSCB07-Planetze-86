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
        container.removeAllViews();

        if (habitLog == null || habitLog.isEmpty()) {
            TextView emptyMessage = new TextView(this);
            emptyMessage.setText("No habits found. Start logging your habits!");
            container.addView(emptyMessage);
        } else {
            // Add all the habits
            for (String habitName : habitLog.keySet()) {
                LinearLayout habitLayout = new LinearLayout(this);
                habitLayout.setOrientation(LinearLayout.HORIZONTAL);
                habitLayout.setPadding(8, 8, 8, 8);

                TextView habitTextView = new TextView(this);
                habitTextView.setText(habitName + " - Days logged: " + habitLog.get(habitName));
                habitTextView.setLayoutParams(new LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

                // Create the "Log" button and set a smaller size
                Button logButton = new Button(this);
                logButton.setText("Log");
                logButton.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                logButton.setTextSize(8); // Smaller text size
                logButton.setPadding(6, 2, 6, 2); // Smaller padding
                logButton.setBackgroundColor(Color.parseColor("#a9bcd0")); // Optional: Custom button color

                logButton.setOnClickListener(v -> {
                    logHabit(habitName);
                    updateHabitLogToFirebase();
                    populateYourHabitsSection(container); // Refresh the list
                });

                // Create the "Remove" button
                Button removeButton = new Button(this);
                removeButton.setText("Remove");
                removeButton.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                removeButton.setTextSize(12); // Smaller text size
                removeButton.setPadding(10, 5, 10, 5); // Smaller padding
                removeButton.setBackgroundColor(Color.parseColor("#373f51")); // Optional: Custom button color for "Remove"

                removeButton.setOnClickListener(v -> {
                    habitLog.remove(habitName);  // Remove the habit from the log
                    updateHabitLogToFirebase();  // Update the habit log in Firebase
                    populateYourHabitsSection(container); // Refresh the list
                });

                habitLayout.addView(habitTextView);
                habitLayout.addView(logButton);
                habitLayout.addView(removeButton);

                container.addView(habitLayout);
            }
        }

        // Always add the "Add Habits" button at the bottom
        Button addHabitsButton = new Button(this);
        addHabitsButton.setText("Add Habits");
        addHabitsButton.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        addHabitsButton.setTextSize(16); // Larger text size for visibility
        addHabitsButton.setPadding(20, 10, 20, 10); // Padding for a better look
        addHabitsButton.setBackgroundColor(Color.parseColor("#009999")); // Optional: Custom color for the button

        addHabitsButton.setOnClickListener(v -> {
            habits = readHabitsFromFile(this);
            populateAllActivitiesSection(container); // Calls the function to populate all activities
        });

        container.addView(addHabitsButton); // Add the button to the container
    }


    private void populateAllActivitiesSection(LinearLayout container) {
        container.removeAllViews();

        if (habits == null || habits.isEmpty()) {
            TextView emptyMessage = new TextView(this);
            emptyMessage.setText("No activities found.");
            container.addView(emptyMessage);
            return;
        }

        for (int i = 0; i < habits.size(); i++) {
            Habit habit = habits.get(i);

            // Check if the habit's name is already in the habitLog
            if (habitLog.containsKey(habit.getName())) {
                continue;  // Skip adding this habit to the list
            }

            // Create a container for each habit
            LinearLayout habitLayout = new LinearLayout(this);
            habitLayout.setOrientation(LinearLayout.HORIZONTAL);
            habitLayout.setPadding(8, 8, 8, 8);

            // Create the habit text with bold formatting
            String habitInfo = habit.getName() + " (" + habit.getCategory() + ") - Impact: " + habit.getImpact();
            SpannableString styledText = new SpannableString(habitInfo);

            // Make the habit name and impact bold
            int habitNameEnd = habit.getName().length();
            int impactStart = habitInfo.indexOf("Impact: ");
            int impactEnd = habitInfo.length();

            styledText.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, habitNameEnd, 0); // Bold habit name
            styledText.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), impactStart, impactEnd, 0); // Bold impact text

            TextView habitTextView = new TextView(this);
            habitTextView.setText(styledText);
            habitTextView.setLayoutParams(new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            habitTextView.setTextSize(16);  // Increase the text size for better readability
            habitTextView.setPadding(10, 5, 10, 5);  // Add padding for a more spaced-out look

            // Button to log and remove the habit
            Button logButton = new Button(this);
            logButton.setText("Add");
            logButton.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            logButton.setTextSize(14);  // Slightly larger text size for better visibility
            logButton.setPadding(10, 5, 10, 5); // Padding for better button appearance
            logButton.setBackgroundColor(Color.parseColor("#373f51"));  // Custom green color for button
            logButton.setTextColor(Color.WHITE); // White text for contrast

            int finalI = i;
            logButton.setOnClickListener(v -> {
                logHabit(habit.getName());  // Log the habit
                habitLog.put(habit.getName(), 1);  // Mark the habit as logged (adding to habitLog)
                habits.remove(finalI);  // Remove the habit from the list
                updateHabitLogToFirebase();  // Update Firebase
                populateAllActivitiesSection(container);  // Refresh the list
                fetchHabits();  // Refresh user habits section
            });

            habitLayout.addView(habitTextView);
            habitLayout.addView(logButton);

            container.addView(habitLayout);
        }
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