package com.example.flightstats;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.flightstats.ui.flightlist.FlightListFragment;

import androidx.appcompat.app.AppCompatActivity;

public class GlobalActivity extends AppCompatActivity {
    private static final String TAG = "GlobalActivity";
    private static final String BEGIN      = "begin";
    private static final String END        = "end";
    private static final String IS_ARRIVAL = "isArrival";
    private static final String ICAO       = "icao";

    public static void startActivity(Activity activity, long begin, long end, boolean isArrival, String icao)
    {

        Intent i = new Intent(activity, GlobalActivity.class);
        i.putExtra(BEGIN, begin);
        i.putExtra(END, end);
        i.putExtra(IS_ARRIVAL, isArrival);
        i.putExtra(ICAO, icao);
        activity.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.global_activity);

        Intent intent = getIntent();

        long begin = intent.getLongExtra(BEGIN, -1);
        long end = intent.getLongExtra(END, -1);
        boolean isArrival = intent.getBooleanExtra(IS_ARRIVAL, false);
        String icao = intent.getStringExtra(ICAO);
        Log.i(TAG,"begin: "+begin +"end: "+end+"isArrival: "+isArrival+"icao: "+icao);
        if (savedInstanceState == null)
        {
            Log.i(TAG,"savedistance == null");
            getSupportFragmentManager().beginTransaction().replace(R.id.container, FlightListFragment.newInstance(begin, end, isArrival, icao)).commitNow();

        }
    }
}
