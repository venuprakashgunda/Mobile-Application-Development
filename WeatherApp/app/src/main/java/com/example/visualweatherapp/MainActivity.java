package com.example.visualweatherapp;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.visualweatherapp.domain.Daily;
import com.example.visualweatherapp.domain.UserSetting;
import com.example.visualweatherapp.domain.Weather;
import com.example.visualweatherapp.services.LocationService;
import com.example.visualweatherapp.services.WeatherServiceRunnable;
import com.example.visualweatherapp.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private SwipeRefreshLayout swiper;
    TextView location;
    TextView timestamp;
    TextView temperature;
    TextView feelLike;
    TextView winds;
    TextView brokenCloudAndWind;
    TextView humidity;
    TextView uvIndex;
    TextView visibility;
    TextView temp1;
    TextView temp2;
    TextView temp3;
    TextView temp4;
    TextView sunrise;
    TextView sunset;
    ImageView imageView;

    RecyclerView recyclerView;
    WeatherRecyclerViewAdapter weatherRecyclerViewAdapter;
    private static final String TAG = "MainActivity";
    private String UNIT = "imperial";
    private double lat = 41.8675766;
    private double lon = -87.616232;

    private boolean isNetwork = true;
    private Menu menu;
    private ArrayList<Daily> dailyArrayList;

    private void assignTextBox() {
        location = findViewById(R.id.userlocation);
        timestamp = findViewById(R.id.timerightnow);
        temperature = findViewById(R.id.temperaturerightnow);
        feelLike = findViewById(R.id.feelLiketemperature);
        winds = findViewById(R.id.windscondition);
        brokenCloudAndWind = findViewById(R.id.cloudwindcondition);
        humidity = findViewById(R.id.humiditycondition);
        uvIndex = findViewById(R.id.uvIndexcurrent);
        visibility = findViewById(R.id.visibilitycurrent);
        temp1 = findViewById(R.id.temp1eight);
        temp2 = findViewById(R.id.temp2one);
        temp3 = findViewById(R.id.temp3five);
        temp4 = findViewById(R.id.temp4eleven);

        //temp5 = findViewById(R.id.timerightnow);
        //temp6 = findViewById(R.id.uvIndexcurrent);
        //temp7 = findViewById(R.id.feelLiketemperature);
        //temp8 = findViewById(R.id.temperaturerightnow);


        sunrise = findViewById(R.id.sunrise);
        sunset = findViewById(R.id.sunset);
        imageView = findViewById(R.id.cloud);

        recyclerView = findViewById(R.id.weatherlist);

        weatherRecyclerViewAdapter = new WeatherRecyclerViewAdapter(this);
        recyclerView.setAdapter(weatherRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        swiper = findViewById(R.id.swiper);
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    private void assignStart() {
        assignTextBox();
        LocationService locationService = new LocationService(this);
        WeatherServiceRunnable weatherServiceRunnable = new WeatherServiceRunnable(this);
        weatherServiceRunnable.setUnit(UNIT);
        weatherServiceRunnable.setLocation(lat, lon);
        new Thread(weatherServiceRunnable).start();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //String sInput = location.getText().toString();
        //this.setTitle(location) ;
        if (!hasNetworkConnection()) {
            isNetwork = false;
            setContentView(R.layout.nointernet);
        } else {
            isNetwork = true;
            setContentView(R.layout.activity_main);
            assignStart();
        }
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Refreshweather();
                swiper.setRefreshing(false); // This stops the busy-circle
            }
        });

    }


    public void Refreshweather() {
        Toast.makeText(this,"App Refreshed!", Toast.LENGTH_SHORT).show();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.temptype, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean hasNetworkConnection() {
        ConnectivityManager connectivityManager = getSystemService(ConnectivityManager.class);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnectedOrConnecting());
    }

    private void showToast(String message) {
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

    private void showToast() {
        Toast.makeText(this,"Enter Valid input", Toast.LENGTH_SHORT).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getLocationAlert() {
        EditText textbox = new EditText(this);
        WeatherServiceRunnable weatherServiceRunnable = new WeatherServiceRunnable(this);
        LocationService locationService = new LocationService(this);
        weatherServiceRunnable.setUnit(UNIT);

        Log.d(TAG, "in getLocationAlert");

        new AlertDialog.Builder(this)
                .setTitle("Enter a Location")
                .setMessage("For US locations enter as 'City', or 'City,State' \n \n For International locations enter as 'City,Country' \n")
                .setView(textbox)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String txt = textbox.getText().toString();
                        if (txt == null) {
                            showToast();
                        } else {
                            double[] ar = locationService.getLatLon(txt);
                            if (ar == null) {
                                showToast();
                            } else {
                                lat = ar[0];
                                lon = ar[1];
                                weatherServiceRunnable.setLocation(ar[0], ar[1]);
                                new Thread(weatherServiceRunnable).start();
                            }
                        }
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.temptype: {
                Log.d(TAG, "unit type changed!!");
                if (!hasNetworkConnection()) {
                    isNetwork = false;
                    setContentView(R.layout.nointernet);
                    showToast("no network connection");
                    return true;
                } else if (!isNetwork) {
                    isNetwork = true;
                    setContentView(R.layout.activity_main);
                    assignStart();
                }
                WeatherServiceRunnable weatherServiceRunnable = new WeatherServiceRunnable(this);
                if (UNIT == "metric") {
                    UNIT  = "imperial";
                    item.setIcon(R.drawable.units_f);
                } else {
                    UNIT = "metric";
                    item.setIcon(R.drawable.units_c);
                }

//                saveUser(lat, lon, UNIT);

                weatherServiceRunnable.setUnit(UNIT);
                weatherServiceRunnable.setLocation(lat, lon);
                new Thread(weatherServiceRunnable).start();
                return true;
            }
            case R.id.calender: {
                Log.d(TAG, "calender pressed");
                if (!hasNetworkConnection()) {
                    setContentView(R.layout.nointernet);
                    showToast("no network connection");
                    return true;
                } else if (!isNetwork) {
                    isNetwork = true;
                    setContentView(R.layout.activity_main);
                    assignStart();
                }
                Intent daily = new Intent(this, DailyForeCastActivity.class);
                daily.putExtra("dailyList", dailyArrayList);
                startActivity(daily);
                return true;
            }
            case R.id.userlocation: {
                Log.d(TAG, "location pressed");
                if (!hasNetworkConnection()) {
                    setContentView(R.layout.nointernet);
                    showToast("no network connection");
                    return true;
                } else if (!isNetwork) {
                    isNetwork = true;
                    setContentView(R.layout.activity_main);
                    assignStart();
                }
                getLocationAlert();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public int getIcon(String name) {
        return this.getResources().getIdentifier("_"+name, "drawable", this.getPackageName());
    }

    public void apiFailedToFetch() {
        showToast("Failed to Fetch the weather");
    }

    public void getWeather(Weather weather) {

        dailyArrayList = weather.getDailyArrayList();

        weatherRecyclerViewAdapter.setHourlyArrayList(weather.getHourlyArrayList());
        recyclerView.setAdapter(weatherRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        location.setText(weather.getTimezone());
        timestamp.setText(weather.getCurrent().getDatetime());
        temperature.setText(weather.getCurrent().getTemp());
        imageView.setImageResource(getIcon(weather.getCurrent().getWeatherIcon()));
        feelLike.setText(weather.getCurrent().getFeelslike());
        winds.setText(weather.getCurrent().getWinds());
        brokenCloudAndWind.setText(weather.getCurrent().getWeatherDesc());
        humidity.setText(weather.getCurrent().getHumidity());
        uvIndex.setText(weather.getCurrent().getUvi());

        visibility.setText(weather.getCurrent().getVisibility());


        Daily daily = dailyArrayList.get(0);

        temp1.setText(daily.getMon());
        temp2.setText(daily.getDay());
        temp3.setText(daily.getEve());
        temp4.setText(daily.getNight());
        sunrise.setText(weather.getCurrent().getSunrise());
        sunset.setText(weather.getCurrent().getSunset());
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent();
        i.setComponent(new ComponentName("com.google.android.calendar", "com.android.calendar.LaunchActivity"));
        startActivity(i);
    }

    public void saveUser(double lati, double longi, String unit) {
        Log.d(TAG, "saveNote: Saving JSON File");
        try {
            FileOutputStream fos = getApplicationContext().openFileOutput("UserSetting.json", Context.MODE_PRIVATE);
            PrintWriter printWriter = new PrintWriter(fos);
            printWriter.print(new UserSetting(lati, longi, "", unit));
            printWriter.close();
            fos.close();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public UserSetting getUserSetting() {
        Log.d(TAG, "loadFile: Loading JSON File");

        try {
            InputStream inputStream = getApplicationContext().openFileInput("UserSetting.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            Log.d(TAG, sb.toString());

            JSONObject jsonObject = new JSONObject(sb.toString());

            lat = jsonObject.getDouble("lat");
            lon = jsonObject.getDouble("lon");
            UNIT = jsonObject.getString("unit");

            //if (UNIT == "metric") {
            //   menu.findItem(R.id.t1type).setIcon(R.drawable.unitc);
            //} else {
            //  menu.findItem(R.id.t2type).setIcon(R.drawable.unitf);
            //}


            if (UNIT == "metric") {
                menu.findItem(R.id.temptype).setIcon(R.drawable.units_c);
            } else {
                menu.findItem(R.id.temptype).setIcon(R.drawable.units_f);
            }

            return new UserSetting(lat, lon, jsonObject.getString("name"), UNIT);

        } catch (FileNotFoundException e) {
            Log.d(TAG, "File Not Found: JSON File not found");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new UserSetting(lat, lon, "", UNIT);
    }

}



