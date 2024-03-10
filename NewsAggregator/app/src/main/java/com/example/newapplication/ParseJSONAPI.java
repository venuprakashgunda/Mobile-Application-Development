package com.example.newapplication;

import android.net.Uri;

import org.json.*;
import java.util.*;
import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public class ParseJSONAPI {

    private static MainActivity mainActivity;

    //api key generated from news.org
    private static final String API = "328edbdcc706464f9618d885730fccd8";
    private static final String NEWSSOURCE_URL ="https://newsapi.org/v2/sources";
    private static final String SOURCEAPI_URL = "https://newsapi.org/v2/top-headlines";

    private static HashSet<String> menuitemsSet = new HashSet<String>();
    private static List<Source> sources;
    private static News newsData;
    private static List<Article> articleDetails;

//    String sourceApiUrl = "https://newsapi.org/v2/sources?apiKey=" + apiKey;
    private static RequestQueue queue;
    private static RequestQueue articlesQueue;

    public static News getAPIData(MainActivity mainAct) {
        mainActivity = mainAct;
        queue = Volley.newRequestQueue(mainActivity);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, getURL(), null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        newsData = parseJSON(response);
                        mainAct.updateData(newsData);

                        menuitemsSet.add("all");
                        sources = newsData.getSources();

                        for (Source ele: sources){
                            menuitemsSet.add(ele.getArticleCat());
                        }
                        mainAct.updateMenuItem(menuitemsSet);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("User-Agent", "News-App");
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
        return newsData;
    }

    public static List<Article> getArticles(String source, MainActivity mainAct){
        mainActivity = mainAct;
        articlesQueue = Volley.newRequestQueue(mainActivity);

        //Uri.Builder buildURL = Uri.parse(sourceURL).buildUpon();
        // buildURL.appendQueryParameter("apiKey", apiKey);
        // String urlToUse = buildURL.build().toString();

        Uri.Builder buildURL = Uri.parse(SOURCEAPI_URL).buildUpon();
        buildURL.appendQueryParameter("sources", source);
        buildURL.appendQueryParameter("apiKey", API);

        JsonObjectRequest JSONRequest = new JsonObjectRequest
                (Request.Method.GET, buildURL.build().toString(), null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        articleDetails = parseDetails(response);
                        mainAct.updateViewPager(articleDetails);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("User-Agent", "News-App");
                return headers;
            }
        };
        articlesQueue.add(JSONRequest);
        return articleDetails;
    }

    private static News parseJSON(JSONObject response) {
        String sourceID, sourceName, sourceDescription;
        try{
            List<Source> sources = new ArrayList<Source>();
            String strStat  = response.getString("status");

            // Uri.Builder buildURL = Uri.parse(storyURL).buildUpon();
            // buildURL.appendQueryParameter("sources",idSource);
            // buildURL.appendQueryParameter("apiKey", apiKey);
            //String urlToUse = buildURL.build().toString();

            if(response.getJSONArray("sources") != null){
                JSONArray jsonArray = response.getJSONArray("sources");
                int length = jsonArray.length();
                int k =0;
                while (k < length) {
                    JSONObject item = jsonArray.getJSONObject(k);
                    sourceDescription = item.getString("category");
                    sourceName = item.getString("name");
                    sourceID = item.getString("id");
                    sources.add(new Source(sourceID, sourceName, sourceDescription));
                    k++;
                }
            }
            newsData = new News(strStat, sources);
        }


//         Request a string response from the provided URL.
//        JsonObjectRequest sourceJsonObjectRequest =
//                new JsonObjectRequest(Request.Method.GET, sourceApiUrl.toString(),
//                        null, listener, error) {
//                    @Override
//                    public Map<String, String> getHeaders() throws AuthFailureError {
//                        Map<String, String> headers = new HashMap<>();
//                        headers.put("User-Agent", "News-App");
//                        return headers;
//                    }
//                };

        catch (Exception ex){
            ex.printStackTrace();
        }
        return newsData;
    }

    private static String getURL() {
        Uri.Builder buildURL = Uri.parse(NEWSSOURCE_URL).buildUpon();
        buildURL.appendQueryParameter("apiKey", API);
        String strRes = buildURL.build().toString();
        return strRes;
    }

    private static List<Article> parseDetails(JSONObject response) {
        List<Article> arrPar = new ArrayList<Article>();
        try{
            JSONArray jsonArray = response.getJSONArray("articles");
            int k =0;
            while(k < jsonArray.length())
            {
                Article obj = new Article();
                JSONObject jsonItem = jsonArray.getJSONObject(k);

                String description = jsonItem.getString("description");
                String author_name = jsonItem.getString("author");
                String urlToImage = jsonItem.getString("urlToImage");
                String URL = jsonItem.getString("url");
                String date = jsonItem.getString("publishedAt");
                String headline = jsonItem.getString("title");

                obj.setArtAuth(author_name);
                obj.setArtUrl(URL);
                obj.setArtDes(description);
                obj.setArtIURL(urlToImage);
                obj.setArtDt(date);
                obj.setArtHead(headline);

                arrPar.add(obj);
                k++;
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return arrPar;
    }

    public static List<Source> updateDrawerLayoutData(String title) {
        List<Source> updateList = new ArrayList<Source>();
        int length = sources.size();
        int k=0;
        while (k < length)
        {
            String matchCategory = sources.get(k).getArticleCat();
            if(matchCategory.equalsIgnoreCase(title)){
                updateList.add(sources.get(k));
            }
            k++;
        }
        return updateList;
    }
}
