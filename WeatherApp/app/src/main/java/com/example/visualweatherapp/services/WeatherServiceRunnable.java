package com.example.visualweatherapp.services;

import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.visualweatherapp.MainActivity;
import com.example.visualweatherapp.domain.Current;
import com.example.visualweatherapp.domain.Daily;
import com.example.visualweatherapp.domain.Hourly;
import com.example.visualweatherapp.domain.UserSetting;
import com.example.visualweatherapp.domain.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

@RequiresApi(api = Build.VERSION_CODES.O)
public class WeatherServiceRunnable implements Runnable {
    //lat latitude //long longitude
    private static final String TAG = "WeatherServiceRunnable";
    String s = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd"));
    private static final String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd"));
    private final MainActivity mainActivity;

    private static final String UNITF = "°F";
    private static final String UNITC = "°C";

    private String unit;

    private double lat;
    private double lon;

    UserSetting userSetting;

    public WeatherServiceRunnable(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setLocation(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    private String getDirection(double degrees) {
        if (degrees >= 337.5 || degrees < 22.5)
            return "N";
        if (degrees >= 22.5 && degrees < 67.5)
            return "NE";
        if (degrees >= 67.5 && degrees < 112.5)
            return "E";
        if (degrees >= 112.5 && degrees < 157.5)
            return "SE";
        if (degrees >= 157.5 && degrees < 202.5)
            return "S";
        if (degrees >= 202.5 && degrees < 247.5)
            return "SW";
        if (degrees >= 247.5 && degrees < 292.5)
            return "W";
        if (degrees >= 292.5 && degrees < 337.5)
            return "NW";
        return "X";
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void run() {
        //https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/41.8781%2C%20-87.6298?unitGroup=us&lang=en&key=VDSKFHFXX5KPKANJVRK7PHAZY
        //String WEATHER_URL ="https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/41.8675766,-87.616232?unitGroup=us&lang=en&key=VDSKFHFXX5KPKANJVRK7PHAZY";
        //String WEATHER_URL ="https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/41.8781%2C%20-87.6298?unitGroup=us&lang=en&key=VDSKFHFXX5KPKANJVRK7PHAZY";
        //  String WEATHER_URL ="https://api.openweathermap.org/data/2.5/onecall?lat=41.8781&lon=-87.6298&appid=6bfc226f3d6885de5b239a8c33047524&units=%22+unit+%22&lang=en&exclude=minutely";
        String WEATHER_URL = "https://api.openweathermap.org/data/2.5/onecall?lat="+lat+"&lon="+lon+"&appid=6bfc226f3d6885de5b239a8c33047524&units="+unit+"&lang=en&exclude=minutely";
        Uri dataUri = Uri.parse(WEATHER_URL);
        String urlToUse = dataUri.toString();
        Log.d(TAG, "string weather data: " + urlToUse);

        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlToUse);

            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            if (conn.getResponseCode() != HttpsURLConnection.HTTP_OK) {
                Log.d(TAG, "run: HTTP ResponseCode NOT OK: " + conn.getResponseCode());
                handleResults(null);
                return;
            }

            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }

            Log.d(TAG, "weather data: " + sb.toString());

        } catch (Exception e) {
            Log.e(TAG, "run: ", e);
            handleResults(null);
            return;
        }

        handleResults(sb.toString());

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void handleResults(String s) {
        if (s == null) {
            Log.d(TAG, "handleResults: Failure in data download");
            mainActivity.runOnUiThread(mainActivity::apiFailedToFetch);
            return;
        }

        final Weather weather = parseJSON(s);
        mainActivity.runOnUiThread(() -> {
            if (weather != null) mainActivity.getWeather(weather);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getDateTime(long time, long timeZoneOffset, String format) {
        return LocalDateTime.ofEpochSecond(time + timeZoneOffset, 0, ZoneOffset.UTC).format(
                DateTimeFormatter.ofPattern(format, Locale.getDefault())
        );
    }

    private String getTemp(double temp) {
        return (long)Math.ceil(temp) + (unit.equals("metric") ? UNITC : UNITF);
    }

    private Current getCurrent(JSONObject jCurrent, long timezoneOffset) throws JSONException {
        JSONObject currentWeather = (JSONObject) jCurrent.getJSONArray("weather").get(0);

        return new Current(
                getDateTime(jCurrent.getLong("dt"), timezoneOffset, "EEE MMM dd h:mm a, yyyy"),
                getTemp(jCurrent.getDouble("temp")),
                "Feels Like "+getTemp(jCurrent.getDouble("feels_like")),
                "Winds: " + getDirection(jCurrent.getDouble("wind_deg")) + " at " + (long)(Math.ceil(jCurrent.getDouble("wind_speed"))) + (unit.equals("metric") ? " m/s" : " mph"),
                "Humidity: "+jCurrent.getString("humidity"),
                "uvi: "+jCurrent.getString("uvi"),
                "Visibility: "+(jCurrent.getDouble("visibility")/1000)+(unit.equals("metric") ? " km" : " mi"),
                getDateTime(jCurrent.getLong("sunrise"), timezoneOffset, "h:mm a"),
                getDateTime(jCurrent.getLong("sunset"), timezoneOffset, "h:mm a"),
                jCurrent.getString("wind_speed"),
                currentWeather.getString("description") + " (" + jCurrent.getString("clouds") + "% " + currentWeather.getString("main") + ")",
                currentWeather.getString("icon")
        );
    }

    private ArrayList<Daily> getDailyArr(JSONArray dailyArr, long timezoneOffset) throws JSONException {
        ArrayList<Daily> dailyArrayList = new ArrayList<>();

        for (int i = 0; i < dailyArr.length(); i++) {
            JSONObject jDaily = (JSONObject) dailyArr.get(i);
            JSONObject temp = jDaily.getJSONObject("temp");
            JSONObject jWeather = (JSONObject) jDaily.getJSONArray("weather").get(0);

            dailyArrayList.add(
                    new Daily(
                            getTemp(temp.getDouble("morn")),
                            getTemp(temp.getDouble("day")),
                            getTemp(temp.getDouble("eve")),
                            getTemp(temp.getDouble("night")),
                            getDateTime(jDaily.getLong("dt"), timezoneOffset, "EEEE,MM/dd"),
                            getTemp(temp.getDouble("min")),
                            getTemp(temp.getDouble("max")),
                            jWeather.getString("description"),
                            jDaily.getString("pop"),
                            jDaily.getString("uvi"),
                            jWeather.getString("icon")
                    )
            );
        }
        return dailyArrayList;
    }

    private ArrayList<Hourly> getHourlyArr(JSONArray hourlyArr, long timezoneOffset) throws JSONException {
        ArrayList<Hourly> hourlyArrayList = new ArrayList<>();

        for (int i = 0; i < hourlyArr.length(); i++) {
            JSONObject jHourly = (JSONObject) hourlyArr.get(i);
            JSONObject jWeather = (JSONObject) jHourly.getJSONArray("weather").get(0);
            String dayName;
            if (today.equals(getDateTime(jHourly.getLong("dt"), timezoneOffset, "dd"))) {
                dayName = "Today";
            } else {
                dayName = getDateTime(jHourly.getLong("dt"), timezoneOffset, "EEE");
            }

            hourlyArrayList.add(new Hourly(
                    dayName,
                    getDateTime(jHourly.getLong("dt"), timezoneOffset, "h:mm a"),
                    jWeather.getString("icon"),
                    getTemp(jHourly.getDouble("temp")),
                    jWeather.getString("description")
            ));
        }
        return hourlyArrayList;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private Weather parseJSON(String s) {
        try {
            JSONObject jMain = new JSONObject(s);

            String timezone = jMain.getString("timezone");
            long timezoneOffset = jMain.getLong("timezone_offset");

//            new LocationService(mainActivity).getLocationName(timezone)

//            userSetting = mainActivity.getUserSetting();
//
//            setUnit(userSetting.getUnit());

            return new Weather(new LocationService(mainActivity).getLocation(lat, lon),
                    getCurrent(jMain.getJSONObject("current"), timezoneOffset),
                    getDailyArr(jMain.getJSONArray("daily"), timezoneOffset),
                    getHourlyArr(jMain.getJSONArray("hourly"), timezoneOffset),
                    unit
//                    userSetting.getUnit()
            );

        } catch (Exception e) {
            Log.d(TAG, "parseJSON: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}