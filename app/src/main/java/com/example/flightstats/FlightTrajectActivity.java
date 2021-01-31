package com.example.flightstats;


import android.os.Bundle;
import android.util.Log;

import com.example.flightstats.ui.flighttraject.FlightTrajectFragment;

import androidx.appcompat.app.AppCompatActivity;

public class FlightTrajectActivity extends AppCompatActivity {
    private static final String TAG = "FlightTrajectActivity";

    //SupportMapFragment mapFragment;
    //GoogleMap googleMap;
    /*public static void startActivity(Activity activity,String airport_dep, String airport_arr, String vol_num, String icao24)
    {

        Intent i = new Intent(activity, GlobalActivity.class);
        i.putExtra(AIRPORT_DEP, airport_dep);
        i.putExtra(AIRPORT_ARR, airport_arr);
        i.putExtra(VOL_NUM, vol_num);
        i.putExtra(ICAO24, icao24);
        activity.startActivity(i);
    }*/

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flighttraject_activity);
        //pour que l appli ne crash pas on va voir si notre intent contient des extra alors on va proceder au prochaines etapes
        if (getIntent().hasExtra("airport_dep") && getIntent().hasExtra("airport_arr") && getIntent().hasExtra("vol_num") && getIntent().hasExtra("icao24")) {
            String airport_dep = getIntent().getStringExtra("airport_dep");
            String airport_arr = getIntent().getStringExtra("airport_arr");
            String vol_num = getIntent().getStringExtra("vol_num");
            String icao24 = getIntent().getStringExtra("icao24");
            Log.i(TAG, "begin: " + airport_dep + "end: " + airport_arr + "numero de vol: " + vol_num + "icao24: " + icao24);

            //mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.traject_map);
            /**mapFragment.getMapAsync(OnMapReadyCallback{
             googleMap = it
             });*/


            if (savedInstanceState == null) {
                Log.i(TAG, "savedistance == null");
                getSupportFragmentManager().beginTransaction().replace(R.id.container, FlightTrajectFragment.newInstance(airport_dep, airport_arr,vol_num , icao24)).commitNow();

            }

        }
    }


}
