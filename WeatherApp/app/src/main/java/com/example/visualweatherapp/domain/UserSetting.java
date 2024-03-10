package com.example.visualweatherapp.domain;

import android.util.JsonWriter;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.StringWriter;

public class UserSetting {
    private double lat;
    private double lon;
    private String name;
    private String unit;

    public UserSetting(double lat, double lon, String name, String unit) {
        this.lat = lat;
        this.lon = lon;
        this.name = name;
        this.unit = unit;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String getName() {
        return name;
    }

    public String getUnit() {
        return unit;
    }

    @NonNull
    public String toString() {
        try {
            StringWriter sw = new StringWriter();
            JsonWriter jsonWriter = new JsonWriter(sw);
            jsonWriter.setIndent("  ");
            jsonWriter.beginObject();
            jsonWriter.name("lat").value(getLat());
            jsonWriter.name("lon").value(getLon());
            jsonWriter.name("name").value(getName());
            jsonWriter.name("unit").value(getUnit());
            jsonWriter.endObject();
            jsonWriter.close();
            return sw.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }
}
