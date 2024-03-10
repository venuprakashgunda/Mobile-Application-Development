package com.example.visualweatherapp.domain;

public class Hourly {
    private String dayName;
    private String time;
    private String icon;
    private String temp;
    private String desc;
    //Gets the Hourly weather data
    public Hourly(String dayName, String time, String icon, String temp, String desc) {
        this.dayName = dayName;
        this.time = time;
        this.icon = icon;
        this.temp = temp;
        this.desc = desc;
    }
    //Gets the data like day name, time, icons, temperature and order
    public String getDayName() {
        return dayName;
    }

    public String getTime() {
        return time;
    }

    public String getIcon() {
        return icon;
    }

    public String getTemp() {
        return temp;
    }

    public String getDesc() {
        return desc;
    }

    @Override
    public String toString() {
        return "Hourly{" +
                "dayName='" + dayName + '\'' +
                ", time='" + time + '\'' +
                ", icon='" + icon + '\'' +
                ", temp='" + temp + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
