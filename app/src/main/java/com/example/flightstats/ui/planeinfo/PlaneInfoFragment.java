package com.example.flightstats.ui.planeinfo;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flightstats.PlaneInfosActivity;
import com.example.flightstats.R;
import com.example.flightstats.data.Plane;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class PlaneInfoFragment extends Fragment implements OnMapReadyCallback {
    private static final String TAG = "PlaneInfoFragment";
    private static final String ICAO24       = "icao24";


    private TextView icao24;
    private TextView origin;
    private TextView last_contact;
    private TextView latitude;
    private TextView on_ground;
    private TextView true_track;
    private TextView sensor;
    private TextView squawk;
    private TextView position_src;
    private TextView callsign;
    private TextView time_position;
    private TextView longitude;
    private TextView barometre_altitude;
    private TextView velocity;
    private TextView vertical_rate;
    private TextView geo_altitude;
    private TextView spi;
    private Button lastFlights;

    private float lat;
    private float longi;


    private PlaneInfoViewModel mViewModel;

    private MapView mMapView;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";



    public static PlaneInfoFragment newInstance()
    {
        return new PlaneInfoFragment();
    }
    public static PlaneInfoFragment newInstance(String icao24)
    {
        PlaneInfoFragment fragment = newInstance();
        Bundle b =new Bundle();
        b.putString(ICAO24,icao24);
        fragment.setArguments(b);
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
    public  View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable  Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.plane_info_fragment, container, false);



                icao24 =root.findViewById(R.id.icao24_value);
                origin = root.findViewById(R.id.origin_value);
                last_contact = root.findViewById(R.id.last_con_value);
                latitude = root.findViewById(R.id.latitude_value);
                on_ground = root.findViewById(R.id.on_ground_value);
                true_track =root.findViewById(R.id.true_track_value);
                sensor =root.findViewById(R.id.sensor_value);
                squawk = root.findViewById(R.id.squawk_value);
                position_src = root.findViewById(R.id.pos_src_value);
                callsign= root.findViewById(R.id.callsign_value);
                time_position = root.findViewById(R.id.time_pos_value);
                longitude = root.findViewById(R.id.longitude_value);
                barometre_altitude = root.findViewById(R.id.bar_alt_value);
                velocity = root.findViewById(R.id.velocity_value);
                vertical_rate = root.findViewById(R.id.vertical_rate_value);
                geo_altitude = root.findViewById(R.id.geo_altitude_value);
                spi = root.findViewById(R.id.spi_value);
                lastFlights = root.findViewById(R.id.derniers_vols_btn);


        mViewModel = ViewModelProviders.of(this).get(PlaneInfoViewModel.class);
        Bundle arguments = getArguments();
        if(arguments!=null)
            mViewModel.loadData(getArguments().getString(ICAO24));
            mViewModel.planeDetails.observe(getViewLifecycleOwner(), new Observer<Plane>() {
                @Override
                public void onChanged(Plane plane) {

                    if(plane.getStates()!=null){
                        List list = Arrays.asList(plane.getStates());

                        List Etats = (List) list.get(0);
                        Log.d(TAG, "********************************* ICAO24 : "+Etats.get(0));

                        longi = Float.parseFloat( Etats.get(5).toString());
                        lat = Float.parseFloat(Etats.get(6).toString());

                        //Ajout des donnees dans leurs textviews
                        icao24.setText(Etats.get(0).toString());
                        callsign.setText(Etats.get(1).toString());
                        origin.setText(Etats.get(2).toString());
                        time_position.setText( Etats.get(3).toString());
                        last_contact.setText( Etats.get(4).toString());
                        longitude.setText( Etats.get(5).toString());
                        latitude.setText( Etats.get(6).toString());
                        barometre_altitude.setText( Etats.get(7).toString());
                        on_ground.setText( Etats.get(8).toString());
                        velocity.setText(Etats.get(9).toString());
                        true_track.setText(Etats.get(10).toString());
                        vertical_rate.setText(Etats.get(11).toString());
                        if(Etats.get(12)!=null){sensor.setText( Etats.get(12).toString());}
                        geo_altitude.setText(Etats.get(13).toString());
                        squawk.setText(Etats.get(14).toString());
                        spi.setText( Etats.get(15).toString());
                        position_src.setText(Etats.get(16).toString());

                    }
                    else{
                        icao24.setText(getArguments().getString(ICAO24));
                    }

                }



            });



        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
                mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
            }
            mMapView = (MapView) root.findViewById(R.id.plane_map);
        mMapView.onCreate(mapViewBundle);

        mMapView.getMapAsync(this);

        lastFlights.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                LastFlights();
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
    public void onMapReady(final GoogleMap map) {

        //La j'ai fait un Handler car la methode onMapReady() recevais la latitude et longitude avant que les valeurs ne soient mises dedans  lat=0 et long=0
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // Actions to do after 10 seconds
                Log.d(TAG, "////////////////////////// long :"+longi+" lat : "+lat);
                if(lat!=0 && longi!=0){

                    map.addMarker(new MarkerOptions().position(new LatLng(lat,longi)).title("Coordonnée de l'avion"));}

            }
        }, 1000);

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

    private void LastFlights(){
        String icao24 = getArguments().getString(ICAO24,"icao24");
        Log.i(TAG,"///////////////////ICAO24 :"+icao24);
        Toast.makeText(getContext(),"ICAO24 :"+icao24,Toast.LENGTH_SHORT).show();
        Intent intent =new Intent(getContext(), PlaneInfosActivity.class);
        intent.putExtra("icao24",icao24);
        intent.putExtra("ecran",2);
        getContext().startActivity(intent);
    }
}
