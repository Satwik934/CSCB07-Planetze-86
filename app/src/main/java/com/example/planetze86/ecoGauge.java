package com.example.planetze86;

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
import com.example.planetze86.utils.FirebaseConstants;
import android.util.Log;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eco_gauge);

        pieChart = findViewById(R.id.pie_chart);
        lineChart = findViewById(R.id.line_chart);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String UID = currentUser.getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference("userEmissions").child(UID);
            getData();
        }
        else {
            Toast.makeText(this, "No user is logged in", Toast.LENGTH_LONG).show();
        }

    }


    private void getData(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    float transportation = (float) 0.0;
                    float food = (float) 0.0;
                    float housing = (float) 0.0;
                    float consumption = (float) 0.0;

                    if (snapshot.hasChild(FirebaseConstants.TRANSPORTATION)) {
                        transportation = getFloat(snapshot.child(FirebaseConstants.TRANSPORTATION));
                    }
                    if (snapshot.hasChild(FirebaseConstants.FOOD)) {
                        food = getFloat(snapshot.child(FirebaseConstants.FOOD));
                    }
                    if (snapshot.hasChild(FirebaseConstants.HOUSING)) {
                        housing = getFloat(snapshot.child(FirebaseConstants.HOUSING));
                    }
                    if (snapshot.hasChild(FirebaseConstants.CONSUMPTION)) {
                        consumption = getFloat(snapshot.child(FirebaseConstants.CONSUMPTION));
                    }
                    createPieChart(transportation, food, housing, consumption);
                    createLineChart(transportation, food, housing, consumption);
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

    private float getFloat(DataSnapshot snapshot) {
        Double value = snapshot.getValue(Double.class);
        if (value != null) {
            return value.floatValue();
        } else {
            return (float) 0.0;
        }
    }

    private void createPieChart(float transportation, float food, float housing, float consumption){
        ArrayList<PieEntry> chartTitles = new ArrayList<>();
        chartTitles.add(new PieEntry(transportation, "Transportation"));
        chartTitles.add(new PieEntry(food, "Food"));
        chartTitles.add(new PieEntry(housing, "Housing"));
        chartTitles.add(new PieEntry(consumption, "Consumption"));
        PieDataSet dataStorage = new PieDataSet(chartTitles, "Emissions Details");
        dataStorage.setColors(Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW);
        dataStorage.setSliceSpace(3f);
        PieData allData = new PieData(dataStorage);
        allData.setValueTextSize(12f);
        allData.setValueTextColor(Color.WHITE);
        pieChart.setData(allData);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.TRANSPARENT);
        pieChart.setCenterText("Emissions");
        pieChart.setCenterTextSize(18f);
        pieChart.animateY(1400);
    }

    private void createLineChart(float transportation, float food, float housing, float consumption){
        ArrayList<Entry> lineTitles = new ArrayList<>();
        lineTitles.add(new Entry(0, transportation));
        lineTitles.add(new Entry(1, food));
        lineTitles.add(new Entry(2, housing));
        lineTitles.add(new Entry(3, consumption));
        LineDataSet dataStorage = new LineDataSet(lineTitles, "Emissions Summary");
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

