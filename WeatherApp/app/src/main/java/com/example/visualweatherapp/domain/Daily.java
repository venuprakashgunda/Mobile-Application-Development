package com.example.visualweatherapp.domain;

import java.io.Serializable;

public class Daily implements Serializable {

    private String mon;
    private String day;
    private String eve;
    private String night;
    private String dayTime;
    private String min;
    private String max;
    private String desc;
    private String pop;
    private String uvi;
    private String icon;
    //Gets the Daily weather data
    public Daily(String mon, String day, String eve, String night, String dayTime, String min, String max, String desc, String pop, String uvi, String icon) {
        this.mon = mon;
        this.day = day;
        this.eve = eve;
        this.night = night;
        this.dayTime = dayTime;
        this.min = min;
        this.max = max;
        this.desc = desc;
        this.pop = pop;
        this.uvi = this.uvi;
        this.icon = icon;
    }

    public String getMon() {
        return mon;
    }

    public String getDay() {
        return day;
    }

    public String getEve() {
        return eve;
    }

    public String getNight() {
        return night;
    }

    public String getDayTime() {
        return dayTime;
    }

    public String getMin() {
        return min;
    }

    public String getMax() {
        return max;
    }

    public String getDesc() {
        return desc;
    }

    public String getPop() {
        return pop;
    }

    public String getuvi() {
        return uvi;
    }

    public String getIcon() {
        return icon;
    }

    @Override
    public String toString() {
        return "Daily{" +
                "mon='" + mon + '\'' +
                ", day='" + day + '\'' +
                ", eve='" + eve + '\'' +
                ", night='" + night + '\'' +
                ", dayTime='" + dayTime + '\'' +
                ", min='" + min + '\'' +
                ", max='" + max + '\'' +
                ", desc='" + desc + '\'' +
                ", pop='" + pop + '\'' +
                ", uvi='" + uvi + '\'' +
                ", icon='" + icon + '\'' +
                '}';
    }
}
