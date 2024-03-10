package com.example.visualweatherapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.visualweatherapp.domain.Daily;

import java.util.ArrayList;

public class DailyForeCastActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DailyForecastAdapter dailyForecastAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_fore_cast);

        ArrayList<Daily> dailyArrayList = (ArrayList<Daily>) getIntent().getSerializableExtra("dailyList");
        recyclerView = findViewById(R.id.dailylistview);
        dailyForecastAdapter = new DailyForecastAdapter(dailyArrayList, this);
        recyclerView.setAdapter(dailyForecastAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }
}