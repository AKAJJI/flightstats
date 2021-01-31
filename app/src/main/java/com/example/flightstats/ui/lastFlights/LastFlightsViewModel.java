package com.example.flightstats.ui.lastFlights;

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
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class LastFlightsViewModel extends AndroidViewModel {
    private static final String TAG = "LastFlightsViewModel";

    public LastFlightsViewModel(@NonNull Application application) {
        super(application);
    }

    MutableLiveData<List<Flight>> flightListLiveData = new MutableLiveData<>();




    public void loadData(String icao24) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getApplication().getApplicationContext());
        long end = Calendar.getInstance().getTimeInMillis()/1000;
        long begin = Calendar.getInstance().getTimeInMillis()/1000 - 3*24*3600; // la date du systeme moins 3 jours

        StringBuilder urlBuilder = new StringBuilder("https://opensky-network.org/api/flights/aircraft?");
        urlBuilder.append("icao24=").append(icao24).append("&begin=").append(begin).append("&end=")
                .append(end);


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

    }

    private JsonArray getFlightsRequestJson(String jsonString){
        JsonParser parser = new JsonParser();
        JsonElement jsonElement =parser.parse(jsonString);
        return jsonElement.getAsJsonArray();
    }


}
