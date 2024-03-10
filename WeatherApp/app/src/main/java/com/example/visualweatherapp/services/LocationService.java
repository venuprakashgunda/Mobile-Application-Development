package com.example.visualweatherapp.services;

import android.location.Address;
import android.location.Geocoder;

import com.example.visualweatherapp.MainActivity;

import java.io.IOException;
import java.util.List;

public class LocationService {

    private MainActivity mainActivity;

    public LocationService(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }


    public String getLocation(double loc, double lon) {
        Geocoder geocoder = new Geocoder(mainActivity);
        try {
            List<Address> address = geocoder.getFromLocation(loc, lon, 1);
            if (address == null || address.isEmpty()) {
                return null;
            }
            String country = address.get(0).getCountryCode();
            String p1 = "";
            String p2 = "";
            if (country.equals("US")) {
                p1 = address.get(0).getLocality();
                p2 = address.get(0).getAdminArea();
            } else {
                p1 = address.get(0).getLocality();
                if (p1 == null)
                    p1 = address.get(0).getSubAdminArea();
                p2 = address.get(0).getCountryName();
            }

            return p1 + ", " + p2;
        } catch (IOException e) {
            return null;
        }
    }

    public String getLocationName(String userProvidedLocation) {
        Geocoder geocoder = new Geocoder(mainActivity);
        try {
            List<Address> address = geocoder.getFromLocationName(userProvidedLocation, 1);
            if (address == null || address.isEmpty()) {
                return null;
            }
            String country = address.get(0).getCountryCode();
            String p1 = "";
            String p2 = "";
            if (country.equals("US")) {
                p1 = address.get(0).getLocality();
                p2 = address.get(0).getAdminArea();
            } else {
                p1 = address.get(0).getLocality();
                if (p1 == null)
                    p1 = address.get(0).getSubAdminArea();
                p2 = address.get(0).getCountryName();
            }

            return p1 + ", " + p2;
        } catch (IOException e) {
            return null;
        }
    }


    public double[] getLatLon(String userProvidedLocation) {
        Geocoder geocoder = new Geocoder(mainActivity);
        try {
            List<Address> address = geocoder.getFromLocationName(userProvidedLocation, 1);
            if (address == null || address.isEmpty()) {
                return null;
            }
            return new double[] {address.get(0).getLatitude(), address.get(0).getLongitude()};
        } catch (IOException e) {
            return null;
        }
    }


}
