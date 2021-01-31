package com.example.flightstats;

import android.os.Bundle;
import android.util.Log;

import com.example.flightstats.ui.lastFlights.LastFlightsFragment;
import com.example.flightstats.ui.planeinfo.PlaneInfoFragment;

import androidx.appcompat.app.AppCompatActivity;


public class PlaneInfosActivity extends AppCompatActivity {
    private static final String TAG = "PlaneInfosActivity";

    protected void onCreate(Bundle savedIntanceState) {

        super.onCreate(savedIntanceState);
        setContentView(R.layout.planeinfos_activity);
        if(getIntent().hasExtra("icao24")&&getIntent().hasExtra("ecran")){
            String icao24 = getIntent().getStringExtra("icao24");
            int ecran = getIntent().getIntExtra("ecran",0);

            Log.i(TAG,"icao24 : "+icao24+" ecran : "+ecran);

            if(savedIntanceState == null){
                Log.i(TAG, "savedistance == null");
                if(ecran==1){
                getSupportFragmentManager().beginTransaction().replace(R.id.planeinfos_activity, PlaneInfoFragment.newInstance(icao24)).commitNow();}
                else if(ecran==2){
                    getSupportFragmentManager().beginTransaction().replace(R.id.planeinfos_activity, LastFlightsFragment.newInstance(icao24)).commitNow();

            }
            }
        }
    }
}
