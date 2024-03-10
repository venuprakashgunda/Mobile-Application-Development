package com.example.CivilAdvocacy;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
public class GetData extends AsyncTask<String, Void, String> {

    private static final String TAG = "DataExtractor";
    private MainActivity mainActivity;
    //private String prefix = "https://www.googleapis.com/civicinfo/v2/representatives?key=AIzaSyDHxK2Hdpw33pjicemRTjBTtyuOJXVo7_w&address=";
    private String prefix = "https://www.googleapis.com/civicinfo/v2/representatives?key=AIzaSyAexoI8HWdMO9qE1MW7Zu916ThJnJbd06Y&address=";

    public GetData(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }


    @Override
    protected String doInBackground(String... strings) {
        String setText = "";
        String location = strings[0];
        if(strings.length>1) {
            setText = strings[1];
        }

        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(prefix+location);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) sb.append(line).append("\n");
            if(setText.equals("true")){
                mainActivity.setCurrLocation(location);
            }
            return sb.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        if(s!=null)
            parseJson(s);
        else
            mainActivity.errorLocation();
    }

    private void parseJson(String s) {
        try {
            JSONObject jObjMain = new JSONObject(s);

            JSONObject jNormalInput = jObjMain.getJSONObject("normalizedInput");

            String locationText = jNormalInput.getString("city")+", "+jNormalInput.getString("state")+" "+jNormalInput.getString("zip");
            JSONArray jArrayOffices = jObjMain.getJSONArray("offices");
            JSONArray jArrayOfficials = jObjMain.getJSONArray("officials");

            int length = jArrayOffices.length();
            mainActivity.clearOfficial();

            for (int i = 0; i<length; i++){
                JSONObject jObj = jArrayOffices.getJSONObject(i);
                String officeName = jObj.getString("name");

                JSONArray indicesStr = jObj.getJSONArray("officialIndices");
                ArrayList<Integer> indices = new ArrayList<>();

                for (int j = 0; j<indicesStr.length(); j++){
                    int pos = Integer.parseInt(indicesStr.getString(j));
                    Official official = new Official(officeName);
                    JSONObject jOfficial = jArrayOfficials.getJSONObject(pos);

                    official.setName(jOfficial.getString("name"));

                    JSONArray jAddresses = jOfficial.getJSONArray("address");
                    JSONObject jAddress = jAddresses.getJSONObject(0);

                    String address="";

                    if (jAddress.has("line1")) address+=jAddress.getString("line1")+'\n';
                    if (jAddress.has("line2")) address+=jAddress.getString("line2")+'\n';
                    if (jAddress.has("line3")) address+=jAddress.getString("line3")+'\n';
                    if (jAddress.has("city")) address+=jAddress.getString("city")+", ";
                    if (jAddress.has("state")) address+=jAddress.getString("state")+' ';
                    if (jAddress.has("zip")) address+=jAddress.getString("zip");

                    official.setAddress(address);

                    if (jOfficial.has("party")) official.setParty(jOfficial.getString("party"));
                    if (jOfficial.has("phones")) official.setPhone(jOfficial.getJSONArray("phones").getString(0));
                    if (jOfficial.has("urls")) official.setUrl(jOfficial.getJSONArray("urls").getString(0));
                    if (jOfficial.has("emails")) official.setEmail(jOfficial.getJSONArray("emails").getString(0));

                    if (jOfficial.has("channels")){
                        Channel channel = new Channel();

                        JSONArray jChannels = jOfficial.getJSONArray("channels");
                        for (int k = 0; k<jChannels.length(); k++){
                            JSONObject jChannel = jChannels.getJSONObject(k);
                            if (jChannel.getString("type").equals("Facebook")) channel.setFacebookId(jChannel.getString("id"));
                            if (jChannel.getString("type").equals("Twitter")) channel.setTwitterId(jChannel.getString("id"));
                            if (jChannel.getString("type").equals("YouTube")) channel.setYoutubeId(jChannel.getString("id"));
                        }
                        official.setChannel(channel);
                    }

                    if (jOfficial.has("photoUrl")) official.setPhotoUrl(jOfficial.getString("photoUrl"));
                    mainActivity.addOfficial(official);
                }

            }

        } catch (JSONException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void setUpLocation(String data)
    {
        TextView location = mainActivity.findViewById(R.id.textView_currentLocation);
        try
        {
            JSONObject normalizedInput = new JSONObject(data);
            normalizedInput = normalizedInput.getJSONObject("normalizedInput");
            String city = normalizedInput.getString("city");
            String state = normalizedInput.getString("state");
            String zip = normalizedInput.getString("zip");

            String locationText = (city.equals("")?"":city+", ") + (zip.equals("")?state:state+", ") + (zip.equals("")?"":zip);
            location.setText(locationText);
        }
        catch (Exception e)
        {
            Log.d(TAG, "EXCEPTION | parseJSON: " + e);
        }
    }

}
