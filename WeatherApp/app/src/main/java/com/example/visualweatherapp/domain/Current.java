package com.example.visualweatherapp.domain;

public class Current {
    private String datetime;
    private String temp;
    private String feelslike;
    private String winds;
    private String humidity;
    private String uvi;
    private String visibility;
    private String sunrise;
    private String sunset;
    private String windspeed;
    private String weatherDesc;
    private String weatherIcon;

    public Current(String dateTime, String temp, String feelslike, String winds, String humidity, String uvi, String visibility, String sunrise, String sunset, String windspeed, String weatherDesc, String weatherIcon) {
        this.datetime = dateTime;
        this.temp = temp;
        this.feelslike = feelslike;
        this.winds = winds;
        this.humidity = humidity;
        this.uvi = uvi;
        this.visibility = visibility;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.windspeed = windspeed;
        this.weatherDesc = weatherDesc;
        this.weatherIcon = weatherIcon;
    }
    //Gets the Current weather data
    public String getWinds() {
        return winds;
    }

    public String getDatetime() {
        return datetime;
    }

    public String getTemp() {
        return temp;
    }

    public String getFeelslike() {
        return feelslike;
    }

    public String getHumidity() {
        return humidity;
    }

    public String getUvi() {
        return uvi;
    }

    public String getVisibility() {
        return visibility;
    }

    public String getSunrise() {
        return sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public String getWindspeed() {
        return windspeed;
    }

    public String getWeatherDesc() {
        return weatherDesc;
    }

    public String getWeatherIcon() {
        return weatherIcon;
    }

    @Override
    public String toString() {
        return "Current{" +
                "datetime='" + datetime + '\'' +
                ", temp='" + temp + '\'' +
                ", feelslike='" + feelslike + '\'' +
                ", humidity='" + humidity + '\'' +
                ", uvi='" + uvi + '\'' +
                ", visibility='" + visibility + '\'' +
                ", sunrise='" + sunrise + '\'' +
                ", sunset='" + sunset + '\'' +
                ", windspeed='" + windspeed + '\'' +
                ", weatherDesc='" + weatherDesc + '\'' +
                ", weatherIcon='" + weatherIcon + '\'' +
                '}';
    }
}
