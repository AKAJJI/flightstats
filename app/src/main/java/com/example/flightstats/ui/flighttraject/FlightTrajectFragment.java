package com.example.flightstats.ui.flighttraject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.flightstats.PlaneInfosActivity;
import com.example.flightstats.R;
import com.example.flightstats.data.Airport;
import com.example.flightstats.data.AirportManager;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * This shows how to create a simple activity with a raw MapView and add a marker to it. This
 * requires forwarding all the important lifecycle methods onto MapView.
 */
public class FlightTrajectFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = "FlightTrajectFragment";
    private static final String AIRPORT_DEP      = "airport_dep";
    private static final String AIRPORT_ARR        = "airport_arr";
    private static final String VOL_NUM = "vol_num";
    private static final String ICAO24       = "icao24";


    private MapView mMapView;

    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    public static FlightTrajectFragment newInstance(){return new FlightTrajectFragment();}

    public static FlightTrajectFragment newInstance(String airport_dep,String airport_arr,String vol_num,String icao24){
        FlightTrajectFragment fragment = newInstance();
        Bundle b = new Bundle();
        b.putString(AIRPORT_DEP, airport_dep);
        b.putString(AIRPORT_ARR, airport_arr);
        b.putString(VOL_NUM, vol_num);
        b.putString(ICAO24, icao24);
        fragment.setArguments(b);
        Log.i(TAG,"begin: "+airport_dep +"end: "+airport_arr+"numero de vol: "+vol_num+"icao24: "+icao24);
        return fragment;

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() !=null){
            Log.i(TAG,"arguments is not null");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.flight_traject_fragment,container,false);

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mMapView = (MapView) root.findViewById(R.id.traject_map);
        mMapView.onCreate(mapViewBundle);

        mMapView.getMapAsync(this);
        Button searchButton = root.findViewById(R.id.plane_details_btn);
        searchButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                showDetails();
            }
        });

        return root;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        //afin que l application ne crash pas j 'ai mis des valeurs par defauts pour la latitude et longitude de depart et arrivé car parfois l'API retourne des valeurs NULL dans les aeroport d'arrivés ou de departs
        LatLng depart = null;
        LatLng arrive = null;

        List<Airport> airportList = AirportManager.getInstance().getAirportList();
        for(Airport airport : airportList){

            if(airport.getIcao().equals(getArguments().getString(AIRPORT_DEP,"airport_dep"))){
                depart=new LatLng(Float.parseFloat(airport.getLat()),Float.parseFloat(airport.getLon()));

            }
            if(airport.getIcao().equals(getArguments().getString(AIRPORT_ARR,"airport_arr"))){
                arrive = new LatLng(Float.parseFloat(airport.getLat()),Float.parseFloat(airport.getLon()));

            }

        }
        //teste des conditions car parfois parmis les donnees retournés par L'API on voit qu'il ya des airoport de depart ou d'arrivé qui sont NULL dans leur champs.
        if(depart==null && arrive!=null){
        map.addMarker(new MarkerOptions().position(arrive).title("Aeroport d'arrivé"));}
        else if(depart!=null && arrive==null){
            map.addMarker(new MarkerOptions().position(depart).title("Aeroport de depart"));
        }else if(depart==null && arrive ==null){
            map.addMarker(new MarkerOptions().position(new LatLng(0,0)).title("Null"));
        }else {

            map.addPolyline(new PolylineOptions()
                    .add(depart, arrive)
                    .width(15f)
                    .color(Color.BLUE));
        }

    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    private void showDetails(){
        String icao24 = getArguments().getString(ICAO24,"icao24");
        Log.i(TAG,"///////////////////ICAO24 :"+icao24);
        Toast.makeText(getContext(),"ICAO24 :"+icao24,Toast.LENGTH_SHORT).show();
        Intent intent =new Intent(getContext(), PlaneInfosActivity.class);
        intent.putExtra("icao24",icao24);
        intent.putExtra("ecran",1);
        getContext().startActivity(intent);

    }
}