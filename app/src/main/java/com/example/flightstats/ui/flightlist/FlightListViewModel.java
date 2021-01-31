package com.example.flightstats.ui.flightlist;

import android.app.Application;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.flightstats.data.Flight;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;


public class FlightListViewModel extends AndroidViewModel {


    private static final String TAG = FlightListViewModel.class.getSimpleName();
    // TODO: Implement the ViewModel
    MutableLiveData<List<Flight>> flightListLiveData = new MutableLiveData<>();

    public FlightListViewModel(@NonNull Application application) {
        super(application);
    }


    public void loadData(long begin, long end, boolean isArrival, String icao) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getApplication().getApplicationContext());
        /*StringBuilder urlBuilder = new StringBuilder("https://opensky-network.org/api/flights/");
        urlBuilder.append(isArrival ? "arrival" : "departure").append("?").append("airport=").append(icao).append("&begin=").append(begin).append("&end=")
                .append(end);*/
        StringBuilder urlBuilder = new StringBuilder("https://opensky-network.org/api/flights/");
        urlBuilder.append(isArrival ? "arrival" : "departure").append("?").append("airport=").append(icao).append("&begin=").append(begin).append("&end=")
                .append(end);
        //String url = "https://opensky-network.org/api/flights/arrival?airport=LFPO&begin=1572172110&end=1572258510";

        Log.i(TAG, "URL is " + urlBuilder.toString());

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlBuilder.toString(), new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Log.i(TAG, "String response is " + response.substring(0,1000));

                List<Flight> flightList = new ArrayList<>();
                Gson gson = new Gson();

                JsonArray flightsJsonArray = getFlightsRequestJson(response);
                for (JsonElement flightObject : flightsJsonArray)
                {
                    flightList.add(gson.fromJson(flightObject.getAsJsonObject(), Flight.class));
                }
                Log.i(TAG, "flight list has size" + flightList.size());
                flightListLiveData.setValue(flightList);
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.i(TAG,"nothing happened :///////////////////////"+error.getMessage());
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                0,
                0));

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

       /* RequestQueue queue = Volley.newRequestQueue(getApplication().getApplicationContext());
        String url ="http://www.google.com";

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.i(TAG,"Response is: "+ response.substring(0,1000));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG,"That didn't work!");
            }
        });*/


// Add the request to the RequestQueue.
       // queue.add(stringRequest);
    }

    private JsonArray getFlightsRequestJson(String jsonString){
        JsonParser parser = new JsonParser();
        JsonElement jsonElement =parser.parse(jsonString);
        return jsonElement.getAsJsonArray();
    }
}
