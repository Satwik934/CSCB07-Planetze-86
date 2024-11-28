package com.example.planetze86;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import android.graphics.Color;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class ecoGauge extends AppCompatActivity {

    private PieChart pieChart;
    private LineChart lineChart;
    private DatabaseReference databaseReference;
    private AnnualAnswers annualData;
    private Button annualButton, monthlyButton, weeklyButton, dailyButton;
    private TextView displayMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eco_gauge);

        pieChart = findViewById(R.id.pie_chart);
//        lineChart = findViewById(R.id.line_chart);
        displayMessage = findViewById(R.id.emissionMessage);
        annualButton = findViewById(R.id.annual_button);
        monthlyButton = findViewById(R.id.monthly_button);
        weeklyButton = findViewById(R.id.weekly_button);
        dailyButton = findViewById(R.id.daily_button);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String UID = currentUser.getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference("users").child(UID);
            displayAnnualData(databaseReference); // default

            annualButton.setOnClickListener(view -> {
                displayAnnualData(databaseReference);
            });
            monthlyButton.setOnClickListener(view -> {
                displayMonthlyData(databaseReference);
            });
            weeklyButton.setOnClickListener(view -> {
                displayWeeklyData(databaseReference);
            });
            dailyButton.setOnClickListener(view -> {
                displayDailyData(databaseReference);
            });

        }
        else {
            Toast.makeText(this, "No user is logged in", Toast.LENGTH_LONG).show();
        }

    }

    private void highlightSelectedButton(Button btn){

        // Remove every highlighted button
        annualButton.setBackgroundColor(Color.parseColor("#A9BCD0"));
        monthlyButton.setBackgroundColor(Color.parseColor("#A9BCD0"));
        weeklyButton.setBackgroundColor(Color.parseColor("#A9BCD0"));
        dailyButton.setBackgroundColor(Color.parseColor("#A9BCD0"));

        // Highlight current button
        btn.setBackgroundColor(Color.parseColor("#E0E1DD"));

    }

    private void displayAnnualData(DatabaseReference databaseReference){
        highlightSelectedButton(annualButton);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    User user = snapshot.getValue(User.class);
                    if (user != null && user.getAnnualAnswers() != null) {
                        annualData = user.getAnnualAnswers();
                        displayMessage.setText(String.format("You've emitted %.2f kg CO2e this year", annualData.getAnnualEmission()));
                        float transportation = (float) annualData.getAnnualTransportation();
                        float food = (float) annualData.getAnnualFood();
                        float housing = (float) annualData.getAnnualHousing();
                        float consumption = (float) annualData.getAnnualConsumption();
                        createPieChart(transportation, food, housing, consumption);
                    }
                    else{
                        float transportation = (float) 0.0;
                        float food = (float) 0.0;
                        float housing = (float) 0.0;
                        float consumption = (float) 0.0;
                        createPieChart(transportation, food, housing, consumption);
                        Toast.makeText(ecoGauge.this, "No annual data found.", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Log.e("ecoGauge", "Used data not found.");
                    Toast.makeText(ecoGauge.this, "Failed to load data.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ecoGauge", "Database Error: " + error.getMessage());
            }
        });
    }

    private void displayMonthlyData(DatabaseReference databaseReference){
        highlightSelectedButton(monthlyButton);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    User user = snapshot.getValue(User.class);
                    if (user != null && user.getAnnualAnswers() != null) {
                        annualData = user.getAnnualAnswers();
                        displayMessage.setText(String.format("You've emitted %.2f kg CO2e this year", annualData.getAnnualEmission()));
                        float transportation = (float) annualData.getAnnualTransportation();
                        float food = (float) annualData.getAnnualFood();
                        float housing = (float) annualData.getAnnualHousing();
                        float consumption = (float) annualData.getAnnualConsumption();
                        createPieChart(transportation, food, housing, consumption);
                    }
                    else{
                        float transportation = (float) 0.0;
                        float food = (float) 0.0;
                        float housing = (float) 0.0;
                        float consumption = (float) 0.0;
                        createPieChart(transportation, food, housing, consumption);
                        Toast.makeText(ecoGauge.this, "No annual data found.", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Log.e("ecoGauge", "Used data not found.");
                    Toast.makeText(ecoGauge.this, "Failed to load data.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ecoGauge", "Database Error: " + error.getMessage());
            }
        });
    }

    private void displayWeeklyData(DatabaseReference databaseReference){
        highlightSelectedButton(weeklyButton);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    User user = snapshot.getValue(User.class);
                    if (user != null && user.getAnnualAnswers() != null) {
                        annualData = user.getAnnualAnswers();
                        displayMessage.setText(String.format("You've emitted %.2f kg CO2e this year", annualData.getAnnualEmission()));
                        float transportation = (float) annualData.getAnnualTransportation();
                        float food = (float) annualData.getAnnualFood();
                        float housing = (float) annualData.getAnnualHousing();
                        float consumption = (float) annualData.getAnnualConsumption();
                        createPieChart(transportation, food, housing, consumption);
                    }
                    else{
                        float transportation = (float) 0.0;
                        float food = (float) 0.0;
                        float housing = (float) 0.0;
                        float consumption = (float) 0.0;
                        createPieChart(transportation, food, housing, consumption);
                        Toast.makeText(ecoGauge.this, "No annual data found.", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Log.e("ecoGauge", "Used data not found.");
                    Toast.makeText(ecoGauge.this, "Failed to load data.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ecoGauge", "Database Error: " + error.getMessage());
            }
        });
    }

    private void displayDailyData(DatabaseReference databaseReference){
        highlightSelectedButton(dailyButton);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    User user = snapshot.getValue(User.class);
                    if (user != null && user.getAnnualAnswers() != null) {
                        annualData = user.getAnnualAnswers();
                        displayMessage.setText(String.format("You've emitted %.2f kg CO2e this year", annualData.getAnnualEmission()));
                        float transportation = (float) annualData.getAnnualTransportation();
                        float food = (float) annualData.getAnnualFood();
                        float housing = (float) annualData.getAnnualHousing();
                        float consumption = (float) annualData.getAnnualConsumption();
                        createPieChart(transportation, food, housing, consumption);
                    }
                    else{
                        float transportation = (float) 0.0;
                        float food = (float) 0.0;
                        float housing = (float) 0.0;
                        float consumption = (float) 0.0;
                        createPieChart(transportation, food, housing, consumption);
                        Toast.makeText(ecoGauge.this, "No annual data found.", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Log.e("ecoGauge", "Used data not found.");
                    Toast.makeText(ecoGauge.this, "Failed to load data.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ecoGauge", "Database Error: " + error.getMessage());
            }
        });
    }

    private void createPieChart(float transportation, float housing, float food, float consumption){
        ArrayList<PieEntry> chartTitles = new ArrayList<>();
        chartTitles.add(new PieEntry(transportation, "Transportation"));
        chartTitles.add(new PieEntry(housing, "Housing"));
        chartTitles.add(new PieEntry(food, "Food"));
        chartTitles.add(new PieEntry(consumption, "Consumption"));
        PieDataSet dataStorage = new PieDataSet(chartTitles, "");
        dataStorage.setColors(Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW);
        dataStorage.setSliceSpace(3f);
        PieData allData = new PieData(dataStorage);
        allData.setValueTextSize(8f);
        allData.setValueTextColor(Color.WHITE);
        pieChart.setData(allData);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.TRANSPARENT);
        pieChart.setCenterText("Annual Emissions Breakdown");
        pieChart.setCenterTextSize(18f);
        pieChart.animateY(1000);
    }

    private void createLineChart(float transportation, float housing, float food, float consumption){
        ArrayList<Entry> lineTitles = new ArrayList<>();
        lineTitles.add(new Entry(0, transportation));
        lineTitles.add(new Entry(1, housing));
        lineTitles.add(new Entry(2, food));
        lineTitles.add(new Entry(3, consumption));
        LineDataSet dataStorage = new LineDataSet(lineTitles, "Emissions Trend");
        dataStorage.setColor(Color.BLUE);
        dataStorage.setLineWidth(2f);
        dataStorage.setCircleColor(Color.RED);
        dataStorage.setCircleRadius(5f);
        dataStorage.setValueTextSize(10f);
        LineData allData = new LineData(dataStorage);
        lineChart.setData(allData);
        lineChart.getDescription().setEnabled(false);
        lineChart.animateX(1400);
    }

}

