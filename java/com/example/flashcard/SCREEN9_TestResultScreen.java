package com.example.flashcard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

public class SCREEN9_TestResultScreen extends AppCompatActivity {

    //Element declaration
    Intent intent2; //Retries quiz
    String deckName;
    int deckSize;
    int rightCounter;
    PieChart pieChart;
    TextView displayResults;
    int rightPercent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_result_screen);

        //Connecting with UI
        intent2 = new Intent (this, SCREEN8_TestMode.class);
        pieChart = findViewById(R.id.piechart);
        displayResults = findViewById(R.id.displayResults);

        //Get extra data
        deckName=getIntent().getStringExtra("DECK_NAME");
        deckSize=getIntent().getIntExtra("DECK_SIZE", -1);
        rightCounter=getIntent().getIntExtra("RIGHT_COUNTER", -1);
        rightPercent= (rightCounter*100)/deckSize;

        displayResults.setText(rightCounter + " out of " + deckSize);
        addPieChartData();
    }

    //Inserting data into the pie chart
    private void addPieChartData() {
        pieChart.addPieSlice(
                new PieModel(
                        "Wrong Answers",
                        100-rightPercent,
                        Color.parseColor("#FF0000")));
        pieChart.addPieSlice(
                new PieModel(
                        "Right Answers",
                        rightPercent*1,
                        Color.parseColor("#00FFFF")));
        pieChart.startAnimation();
    }

    //Return to deck
    public void Return(View view) {
        finish();
    }

    //Retry quiz
    public void Retry(View view) {
        intent2.putExtra("DECK_NAME", deckName);
        startActivity(intent2);

        finish();
    }
}
