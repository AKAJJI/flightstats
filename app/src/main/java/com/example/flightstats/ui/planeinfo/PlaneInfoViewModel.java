package com.example.flightstats.ui.planeinfo;

import android.app.Application;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.flightstats.data.Plane;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class PlaneInfoViewModel extends AndroidViewModel {
    private static final String TAG = "PlaneInfoViewModel";

    MutableLiveData<Plane> planeDetails = new MutableLiveData<>();

    public PlaneInfoViewModel(@NonNull Application application) {
        super(application);
    }

    public void loadData(String icao24) {


        RequestQueue queue = Volley.newRequestQueue(getApplication().getApplicationContext());

        StringBuilder urlBuilder = new StringBuilder("https://opensky-network.org/api/states/all?");
        urlBuilder.append("icao24=").append(icao24).append("&time=").append(0);


        Log.i(TAG, "URL is " + urlBuilder.toString());

        // Request a string response from the provided URL.
        String url ="https://opensky-network.org/api/states/all?icao24=3c6489&time=0";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlBuilder.toString(), new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Log.i(TAG, "String response is "+response);

                JsonElement elt = getFlightsRequestJson(response);
                Gson gson = new Gson();
                Plane plane = gson.fromJson(elt.getAsJsonObject(),Plane.class);

                Log.i(TAG, "/////////////////////Time : " + plane.getTime()+"state : "+plane.getStates());
                /*List list = Arrays.asList(plane.getStates());
                Log.d(TAG, "onResponse: "+list.get(0));
                List Etats = (List) list.get(0);
                Log.d(TAG, "********************************* ICAO24 : "+Etats.get(0));*/
                planeDetails.setValue(plane);
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
    private JsonObject getFlightsRequestJson(String jsonString){
        JsonParser parser = new JsonParser();
        JsonElement jsonElement =parser.parse(jsonString);
        return jsonElement.getAsJsonObject();
    }

}






