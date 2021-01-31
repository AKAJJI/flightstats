package com.example.flightstats.ui.lastFlights;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.flightstats.R;
import com.example.flightstats.data.Flight;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class LastFlightsFragment extends Fragment {
    private static final String TAG = "LastFlightsFragment";

    private static final String ICAO24       = "icao24";

    private LastFlightsAdapter mAdapter;
    private LastFlightsViewModel mViewModel;

    public static LastFlightsFragment newInstance()
    {
        return new LastFlightsFragment();
    }

    public static LastFlightsFragment newInstance(String icao24)
    {

        LastFlightsFragment fragment = newInstance();
        Bundle b = new Bundle();

        b.putString(ICAO24, icao24);
        fragment.setArguments(b);
        Log.i(TAG,"icao24: "+icao24);
        return fragment;

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.last_flights_fragment, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.last_flights_recyclerview);
        mAdapter = new LastFlightsAdapter(getContext());
        recyclerView.setAdapter(mAdapter);
        RecyclerView.ItemDecoration divider =new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), RecyclerView.VERTICAL, false));
        recyclerView.addItemDecoration(divider);
        return root;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(LastFlightsViewModel.class);
        Bundle arguments = getArguments();
        if(arguments!=null)
            mViewModel.loadData(arguments.getString(ICAO24));
        Log.i(TAG,"//////////////////////////////////// ICAO24: "+arguments.getString(ICAO24));
        mViewModel.flightListLiveData.observe(getViewLifecycleOwner(), new Observer<List<Flight>>()
        {
            @Override
            public void onChanged(List<Flight> flights)
            {
                Log.i(TAG, "**************************************updating list with size = " + flights.size());
                mAdapter.setFlights(flights);
            }
        });
    }



}
