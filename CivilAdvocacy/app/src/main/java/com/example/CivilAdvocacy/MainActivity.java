package com.example.CivilAdvocacy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "MainActivity";

    private final List<Official> officialList = new ArrayList<>();
    private RecyclerView recyclerView;
    private OfficialAdapter officialAdapter;
    private static MainActivity mainActivity;
    //private RequestQueue queue;

    private FusedLocationProviderClient mFusedLocationClient;
    private String locationString = "Unspecified Location";
    private static final int LOCATION_REQUEST = 111;
    private TextView currLocation;
    private TextView warning;
    private ConnectivityManager cm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Civil Advocacy");
        //queue = Volley.newRequestQueue(this);
        recyclerView = findViewById(R.id.listRecycler);
        currLocation =findViewById(R.id.textView_currentLocation);
        mainActivity = this;
        officialAdapter = new OfficialAdapter(officialList, this);
        recyclerView.setAdapter(officialAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        warning = findViewById(R.id.textView_warning);

        if (networkCheck()){
            mFusedLocationClient =
                    LocationServices.getFusedLocationProviderClient(this);

            locationString = determineLocation();

            GetData dataLoader = new GetData(this);

            if(!locationString.equals("Unspecified Location"))
                dataLoader.execute(locationString);
            else{
                GetWarning("No location, please restart the app and grant location access");
            }
            warningClose();
        } else {
            GetWarning("No Network connection: Data cannot be accessed/loaded without an internet connection");
        }
    }

    @SuppressLint("MissingPermission")
    private String determineLocation() {
        if (checkAppPermissions()) {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        // Got last known location. In some situations this can be null.
                        if (location != null) {
                            locationString = getPlace(location);
                            currLocation.setText(locationString);
                            GetData dataLoader = new GetData(this);
                            dataLoader.execute(locationString);
                        }
                    })
                    .addOnFailureListener(this, e -> Toast.makeText(MainActivity.this,
                            e.getMessage(), Toast.LENGTH_LONG).show());
            return locationString;
        }
        return locationString;
    }

    private boolean checkAppPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION
                    }, LOCATION_REQUEST);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_REQUEST) {
            if (permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    determineLocation();
                } else {
                    currLocation.setText(R.string.deniedText);
                    GetWarning("Location Permission is denied please grant access in order to use the app.");
                }
            }
        }
    }

    private String getPlace(Location loc) {

        StringBuilder sb = new StringBuilder();

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        String addressLine = "Unresolved";

        try {
            addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
            addressLine = addresses.get(0).getAddressLine(0);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return addressLine;
    }


    @Override
    public void onClick(View view) {
        int pos = recyclerView.getChildLayoutPosition(view);
        Intent intent = new Intent(this, ActivityOfficial.class);
        intent.putExtra("location", currLocation.getText().toString());
        intent.putExtra("official", officialList.get(pos));
        startActivity(intent);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_about) {
            openAboutActivity(item);
            return true;
        } else if (item.getItemId() == R.id.menu_location) {
            searchButton();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public void openAboutActivity(MenuItem item){
        try {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        }catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "onCreate: " + e);
        }
    }

    public void searchButton(){
        LayoutInflater inflater = LayoutInflater.from(this);
        final View view = inflater.inflate(R.layout.dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enter a City, State or Zip code");
        builder.setView(view);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText inputTextView = view.findViewById(R.id.inputTextView);
                String input = inputTextView.getText().toString();

                GetData dataLoader = new GetData(mainActivity);
                String setText = "true";
                dataLoader.execute(input,setText);
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public Boolean networkCheck(){
        cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo !=null && netInfo.isConnectedOrConnecting())return true;
        else return false;
    }
    public void GetWarning(String update){
        warning.setVisibility(View.VISIBLE);
        warning.setText(update);
    }

    public String getLocationText(String input){
        StringBuilder sb = new StringBuilder();
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        String addressLine = "Unresolved";
        try {
            addresses = geocoder.getFromLocationName(input,1);
            String city = addresses.get(0).getLocality();
            String locality = addresses.get(0).getSubAdminArea();
            String state = addresses.get(0).getAdminArea();
            String zipcode = addresses.get(0).getPostalCode();
            addressLine = "";
            if(city != null)
                addressLine = city;
            if(locality != null)
                addressLine = addressLine + ", " + locality;
            if(state != null)
                addressLine = addressLine + ", " + state;
            if(zipcode != null)
                addressLine = addressLine + ", " + zipcode;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return addressLine;
    }


    @SuppressLint("SetTextI18n")
    public void errorLocation(){
        officialList.clear();
        currLocation.setText("Location Unavailable");
        GetWarning("Please try again with a valid location in United States Region");
        officialAdapter.notifyDataSetChanged();
    }

    public void clearOfficial(){
        officialList.clear();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addOfficial(Official official){
        officialList.add(official);
        officialAdapter.notifyDataSetChanged();
        warningClose();
    }
    public void warningClose(){
        warning.setVisibility(View.INVISIBLE);
    }

    public void setCurrLocation(String loc){
        String newLoc = getLocationText(loc);
        if (newLoc.equals("Unresolved"))
            currLocation.setText(R.string.unresolved);
        else
            currLocation.setText(newLoc);
    }
}