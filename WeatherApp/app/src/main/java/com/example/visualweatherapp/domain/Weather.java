package com.example.visualweatherapp.domain;

import java.util.ArrayList;

public class Weather {
    private String timezone;
    private Current current;
    ArrayList<Daily> dailyArrayList;
    ArrayList<Hourly> hourlyArrayList;
    private String unit;

    public Weather() {
    }

    public Weather(String timezone, Current current, ArrayList<Daily> dailyArrayList, ArrayList<Hourly> hourlyArrayList, String unit) {
        this.timezone = timezone;
        this.current = current;
        this.dailyArrayList = dailyArrayList;
        this.hourlyArrayList = hourlyArrayList;
        this.unit = unit;
    }

    public String getUnit() {
        return unit;
    }

    public Current getCurrent() {
        return current;
    }

    public ArrayList<Daily> getDailyArrayList() {
        return dailyArrayList;
    }

    public ArrayList<Hourly> getHourlyArrayList() {
        return hourlyArrayList;
    }

    public String getTimezone() {
        return timezone;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "timezone='" + timezone + '\'' +
                ", current=" + current +
                ", dailyArrayList=" + dailyArrayList +
                ", hourlyArrayList=" + hourlyArrayList +
                '}';
    }
}
