package com.example.flightstats.ui.lastFlights;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.flightstats.R;
import com.example.flightstats.data.Airport;
import com.example.flightstats.data.AirportManager;
import com.example.flightstats.data.Flight;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class LastFlightsAdapter extends RecyclerView.Adapter<LastFlightsAdapter.LastViewHolder> {
    private static final String TAG = "LastFlightsAdapter";

    List<Flight> mFlightList = new ArrayList<>();
    List<Airport> airportList = AirportManager.getInstance().getAirportList();
    private Context mContext;

    public LastFlightsAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public LastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.i(TAG,"i am on onCreateViewHolder()");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.flight_list_fragment_items,parent,false);
        return new LastViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull LastViewHolder holder, int position) {

        String depart=null;
        String arrive=null;
        Log.i(TAG,"i am on onBindViewHolder()");


        List<Airport> airportList = AirportManager.getInstance().getAirportList();
        for(Airport airport : airportList){

            if(airport.getIcao().equals(mFlightList.get(position).getEstDepartureAirport())){
                depart= airport.getName();

            }
            if(airport.getIcao().equals(mFlightList.get(position).getEstArrivalAirport())){
                arrive = airport.getName();

            }

        }


        if(depart==null && arrive==null) {
            holder.airport_dep.setText(mFlightList.get(position).getEstDepartureAirport());
            holder.airport_arr.setText(mFlightList.get(position).getEstArrivalAirport());
        }else if(depart == null && arrive!=null){
            holder.airport_dep.setText(mFlightList.get(position).getEstDepartureAirport());
            holder.airport_arr.setText(arrive);
        }else if(depart!=null && arrive==null){
            holder.airport_dep.setText(depart);
            holder.airport_arr.setText(mFlightList.get(position).getEstArrivalAirport());
        }else {
            holder.airport_dep.setText(depart);
            holder.airport_arr.setText(arrive);
        }





        holder.date_dep.setText(getDate( mFlightList.get(position).getFirstSeen()));
        holder.date_arr.setText(getDate( mFlightList.get(position).getLastSeen()));

        holder.numVol.setText(mFlightList.get(position).getCallsign());
        holder.dureeVol.setText(getHour( mFlightList.get(position).getLastSeen()-mFlightList.get(position).getFirstSeen()));

    }

    @Override
    public int getItemCount() {
        return mFlightList.size();
    }

    public void setFlights(List<Flight> flighstList){
        Log.i(TAG,"i am on setFlights()");
        mFlightList.clear();
        mFlightList.addAll(flighstList);
        notifyDataSetChanged();

    }
    private String getDate(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time*1000L);
        String date = DateFormat.format("dd/MM/yyyy HH:mm", cal).toString();
        return date;
    }
    private String getHour(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time*1000L);
        String date = DateFormat.format("HH:mm", cal).toString();
        return date;
    }

    public class LastViewHolder extends RecyclerView.ViewHolder {
        TextView airport_dep;
        TextView airport_arr;
        TextView date_dep;
        TextView date_arr;
        TextView numVol;
        TextView dureeVol;
        ConstraintLayout LastFlightsItems;
        public LastViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.i(TAG,"i am on Flightviewholder class");
            airport_arr = itemView.findViewById(R.id.airport_arr_textView);
            airport_dep = itemView.findViewById(R.id.airport_dep_textView);
            date_dep = itemView.findViewById(R.id.date_dep_textView);
            date_arr = itemView.findViewById(R.id.date_arr_textView);
            numVol = itemView.findViewById(R.id.num_vol_textView);
            dureeVol = itemView.findViewById(R.id.duree_vol_textView);
            LastFlightsItems = itemView.findViewById(R.id.last_flights_list_items);
        }


    }
}
