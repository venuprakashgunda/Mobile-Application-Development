package com.example.visualweatherapp;

public class Utils {

    //To convert temperatures in degrees Fahrenheit to Celsius, subtract 32 and multiply by .5556 (or 5/9).


    public int tempInCelcius(int temp) {
        return (int) Math.ceil((temp - 32) * 0.5556);
    }

    public int tempInFahren(int temp) {
        return (int) Math.ceil((temp * 1.8) + 32);
    }

}
