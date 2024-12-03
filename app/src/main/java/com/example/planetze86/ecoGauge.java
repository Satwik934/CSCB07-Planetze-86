package com.example.planetze86;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.time.Year;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

import android.app.DatePickerDialog;

import java.util.Calendar;

public class ecoGauge extends AppCompatActivity {

    private PieChart pieChart;
    private LineChart lineChart;
    private DatabaseReference databaseReference;
    private String selectedTab = "Annual";
    private AnnualAnswers annualData;
    private Button annualButton, monthlyButton, weeklyButton, dailyButton, dateButton;
    private TextView displayMessage, dateMessage;
    private String chosenDate;
    private TextView compareText;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eco_gauge);

        
        pieChart = findViewById(R.id.pie_chart);
//        lineChart = findViewById(R.id.line_chart);
        compareText = findViewById(R.id.compare_text);
        displayMessage = findViewById(R.id.emissionMessage);
        dateMessage = findViewById(R.id.dateMessage);
        annualButton = findViewById(R.id.annual_button);
        monthlyButton = findViewById(R.id.monthly_button);
        weeklyButton = findViewById(R.id.weekly_button);
        dailyButton = findViewById(R.id.daily_button);
        dateButton = findViewById(R.id.date_button);
        ImageButton backButton = findViewById(R.id.backButton);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        // Format the default date
        chosenDate = String.format("%02d-%02d-%04d", day, month + 1, year);
        dateMessage.setText(chosenDate);


        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String UID = currentUser.getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference("users").child(UID);

            defineAnnualData(databaseReference);

            displayAnnualData(databaseReference); // default

            annualButton.setOnClickListener(view -> {
                selectedTab = "Annual";
                displayAnnualData(databaseReference);
            });
            monthlyButton.setOnClickListener(view -> {
                selectedTab = "Monthly"; // Store selected tab
                displayMonthlyData(databaseReference);
            });
            weeklyButton.setOnClickListener(view -> {
                selectedTab = "Weekly"; // Store selected tab
                displayWeeklyData(databaseReference);
            });
            dailyButton.setOnClickListener(view -> {
                selectedTab = "Daily"; // Store selected tab
                displayDailyData(databaseReference);
            });

        }
        else {
            Toast.makeText(this, "No user is logged in", Toast.LENGTH_LONG).show();
        }

        dateButton.setOnClickListener(view -> showDatePickerDialog());

        // Back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ecoGauge.this, PlanetzeMenu.class);
                startActivity(intent);
            }
        });

    }

    private void defineAnnualData(DatabaseReference databaseReference) {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);
                    if (user != null) {
                        annualData = user.getAnnualAnswers();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Set annual data", "Database error: " + error.getMessage());
            }
        });
    }

    private float calculateEmissions(DataSnapshot snapshot, String category) {
        float totalEmissions = 0;
        if (snapshot.child(category).exists()) {
            for (DataSnapshot activity : snapshot.child(category).getChildren()) {
                if (activity.child("emissions").exists()) {
                    Float value = activity.child("emissions").getValue(Float.class);
                    if (value != null){
                        totalEmissions += value;
                    }
                }
            }
        }
        return totalEmissions;
    }

    private void displayAnnualData(DatabaseReference databaseReference) {

        highlightSelectedButton(annualButton);
        int selectedYear = Integer.parseInt(chosenDate.substring(6, 10));
        int numOfDays = Year.isLeap(selectedYear) ? 366 : 365;

        // Using arrays to store since java won't let us modify local var.
        // i.e. can't set transportation = 0 and than increment later.
        final float[] transportation = {0};
        final float[] food = {0};
        final float[] housing = {0};
        final float[] consumption = {0};
        final int[] processedDays = {0};

        for (int day = 1; day <= numOfDays; day++) {

            // collects and writes date in format dd-mm-yyyy
            LocalDate date = LocalDate.ofYearDay(selectedYear, day);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String currentDate = date.format(formatter);

            databaseReference.child("activities").child(currentDate)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                transportation[0] += calculateEmissions(snapshot,
                                        "Transportation");
                                food[0] += calculateEmissions(snapshot, "FoodConsumption");
                                consumption[0] += calculateEmissions(snapshot, "Shopping");
                                housing[0] = (annualData != null ? (float)(annualData
                                        .getAnnualHousing()) : 0);
                            }
                            processedDays[0]++;

                            // Display only updates when all days of year are calculated.
                            if (processedDays[0] == numOfDays) {
                                updateDisplay(transportation[0], food[0], consumption[0],
                                        housing[0], "year", (float) 1.0);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e("ecoGauge", "Database Error: " + error.getMessage());
                        }
                    });
        }
    }

    private void displayMonthlyData(DatabaseReference databaseReference) {

        highlightSelectedButton(monthlyButton);
        int selectedMonth = Integer.parseInt(chosenDate.substring(3, 5)); // Date format 00-00-0000
        int selectedYear = Integer.parseInt(chosenDate.substring(6, 10));
        int numOfDays = getDaysInMonth(chosenDate);

        // Using arrays to store since java won't let us modify local var.
        // i.e. can't set transportation = 0 and than increment later.
        final float[] transportation = {0};
        final float[] food = {0};
        final float[] housing = {0};
        final float[] consumption = {0};
        final int[] processedDays = {0};

        for (int day = 1; day <= numOfDays; day++) {
            @SuppressLint("DefaultLocale") String currentDate = String.format("%02d-%02d-%04d",
                    day, selectedMonth, selectedYear);
            databaseReference.child("activities")
                    .child(currentDate).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                transportation[0] += calculateEmissions(snapshot,
                                        "Transportation");
                                food[0] += calculateEmissions(snapshot, "FoodConsumption");
                                consumption[0] += calculateEmissions(snapshot, "Shopping");
                                housing[0] = (annualData != null ? (float)(annualData
                                        .getAnnualHousing() / 12.0) : 0);
                            }
                            processedDays[0]++;

                            // Display only updates when all days of month are calculated.
                            if (processedDays[0] == numOfDays) {
                                updateDisplay(transportation[0], food[0],
                                        consumption[0], housing[0], "month", (float) 12.0);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e("ecoGauge", "Database Error: " + error.getMessage());
                        }
                    });
        }
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    private void updateDisplay(float transportation, float food, float shopping,
                               float housing, String zone, float divisor) {
        if (transportation == 0 && food == 0 && shopping == 0 && housing == 0) {
            createPieChart(0, 0, 0, 0);
            displayMessage.setText(String.format("No data found for the selected %s.", zone));
            compareText.setText("Data unavailable for comparison.");
            Toast.makeText(ecoGauge.this,
                    String.format("No data found for the selected %s.", zone), Toast.LENGTH_SHORT).show();
        } else {
            displayMessage.setText(String.format("You've emitted %.4f kg CO2e this %s on %s.",
                    transportation + food + shopping + housing, zone, chosenDate));
            createPieChart(transportation, housing, food, shopping);
            if (annualData != null) {
                compareText.setText(String.format("The average %s's footprint in %s is %.4f kg CO2e.",
                        zone, annualData.getCountry(), (annualData.getCountryEmission() / divisor)));
            }
        }
    }

    private void displayWeeklyData(DatabaseReference databaseReference) {

        highlightSelectedButton(weeklyButton);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate selectedDate = LocalDate.parse(chosenDate, formatter);

        // Week starts sunday, ends saturday
        LocalDate startOfWeek = selectedDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        LocalDate endOfWeek = selectedDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));

        // Using arrays to store since java won't let us modify local var.
        // i.e. can't set transportation = 0 and than increment later.
        final float[] transportation = {0};
        final float[] food = {0};
        final float[] housing = {0};
        final float[] consumption = {0};
        final int[] elapsedDays = {0};

        // Using the date directly as a loop variable and adding one day at a time.
        // If the date surpasses the last day of week (saturday) it will break the loop.
        for (LocalDate date = startOfWeek; !date.isAfter(endOfWeek); date = date.plusDays(1)) {
            final String currentDate = date.format(formatter); // Format like dd-mm-yyyy

            databaseReference.child("activities").child(currentDate).addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                transportation[0] += calculateEmissions(snapshot, "Transportation");
                                food[0] += calculateEmissions(snapshot, "FoodConsumption");
                                consumption[0] += calculateEmissions(snapshot, "Shopping");
                                housing[0] = (annualData != null ? (float)(annualData.getAnnualHousing() / 52.0) : 0);
                            }
                            elapsedDays[0]++;

                            // Display only updates when all 7 days of week are calculated.
                            if (elapsedDays[0] == 7) {
                                updateDisplay(transportation[0], food[0], consumption[0], housing[0], "week", (float)52.0);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e("ecoGauge", "Database Error: " + error.getMessage());
                        }
                    });
        }
    }

    private void displayDailyData(DatabaseReference databaseReference){
        highlightSelectedButton(dailyButton);
        databaseReference.child("activities").child(chosenDate)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    float transportation = calculateEmissions(snapshot, "Transportation");
                    float food = calculateEmissions(snapshot, "FoodConsumption");
                    float shopping = calculateEmissions(snapshot, "Shopping");
                    float housing = (annualData != null ? (float) (annualData
                            .getAnnualHousing() / 365.0) : 0);
                    updateDisplay(transportation, food, shopping,
                            housing, "day", (float)365.0);
                } else {
                    updateDisplay(0, 0, 0, 0,
                            "day", (float)365.0);
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
        if (transportation > 0) chartTitles.add(new PieEntry(transportation, "Transportation"));
        if (housing > 0) chartTitles.add(new PieEntry(housing, "Energy Use"));
        if (food > 0) chartTitles.add(new PieEntry(food, "Food"));
        if (consumption > 0) chartTitles.add(new PieEntry(consumption, "Shopping"));
        if (chartTitles.isEmpty()) {
            pieChart.clear();
            pieChart.setNoDataText("No data to display.");
            return;
        }
        PieDataSet dataStorage = new PieDataSet(chartTitles, "");
        dataStorage.setColors(Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW);
        dataStorage.setSliceSpace(3f);
        PieData allData = new PieData(dataStorage);
        allData.setValueTextSize(8f);
        allData.setValueTextColor(Color.BLACK);
        pieChart.setData(allData);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.TRANSPARENT);
        pieChart.setCenterTextSize(18f);
        pieChart.animateY(1000);
        pieChart.invalidate();
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


    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int chosenYear = calendar.get(Calendar.YEAR);
        int chosenMonth = calendar.get(Calendar.MONTH);
        int chosenDay = calendar.get(Calendar.DAY_OF_MONTH);
        @SuppressLint("DefaultLocale") DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    chosenDate = String.format("%02d-%02d-%04d", selectedDay, selectedMonth + 1, selectedYear);
                    storeSelectedDate();
                }, chosenYear, chosenMonth, chosenDay);
        datePickerDialog.show();
    }

    private void storeSelectedDate() {
        dateMessage.setText(chosenDate);
        Toast.makeText(this, "Date Selected: " + chosenDate, Toast.LENGTH_SHORT).show();
        switch (selectedTab) {
            case "Annual":
                displayAnnualData(databaseReference);
                break;
            case "Monthly":
                displayMonthlyData(databaseReference);
                break;
            case "Weekly":
                displayWeeklyData(databaseReference);
                break;
            case "Daily":
                displayDailyData(databaseReference);
                break;
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

    private int getDaysInMonth(String chosenDate) {
        int day = Integer.parseInt(chosenDate.substring(0, 2));
        int month = Integer.parseInt(chosenDate.substring(3, 5));
        int year = Integer.parseInt(chosenDate.substring(6, 10));
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH); // This returns # of days in month.
    }

}

