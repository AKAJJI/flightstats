package com.example.flightstats.ui.flightlist;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flightstats.FlightTrajectActivity;
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



public class FlightListAdapter extends RecyclerView.Adapter<FlightListAdapter.FlightViewHolder> {

    private static final String TAG = FlightListAdapter.class.getSimpleName();


    List<Flight> mFlightList = new ArrayList<>();
    List<Airport> airportList = AirportManager.getInstance().getAirportList();
    private Context mContext;

    public FlightListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public FlightViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType){
        //TextView callSignTextView = new TextView(parent.getContext());
        //return new FlightViewHolder(callSignTextView);
        Log.i(TAG,"i am on onCreateViewHolder()");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.flight_list_fragment_items,parent,false);
        return new FlightViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlightViewHolder holder, final int position) {
        //Log.i(TAG,mFlightList.get(position).getCallsign());
        //String dateDep=null;
        String depart=null;
        String arrive=null;
        Log.i(TAG,"i am on onBindViewHolder()");
        //String dateArr = DateFormat.format( mFlightList.get(position).getFirstSeen());

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


        holder.FlightListItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"Vol numero :"+mFlightList.get(position).getCallsign());
                Toast.makeText(mContext,"Numero de vol :"+mFlightList.get(position).getCallsign(),Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(mContext, FlightTrajectActivity.class);
                intent.putExtra("airport_dep",mFlightList.get(position).getEstDepartureAirport());
                intent.putExtra("airport_arr",mFlightList.get(position).getEstArrivalAirport());
                intent.putExtra("vol_num",mFlightList.get(position).getCallsign());
                intent.putExtra("icao24",mFlightList.get(position).getIcao24());
                mContext.startActivity(intent);
                //FlightTrajectActivity.startActivity(getActivity(),mFlightList.get(position).getCallsign(),mFlightList.get(position).getEstDepartureAirport(),mFlightList.get(position).getEstArrivalAirport(),mFlightList.get(position).getIcao24());
            }
        });

    }

    @Override
    public int getItemCount() {
        Log.i(TAG,"//////////////////////////////////////////////////////////////// size :"+mFlightList.size());
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


    class FlightViewHolder extends RecyclerView.ViewHolder{

        //TextView callSignView;
        TextView airport_dep;
        TextView airport_arr;
        TextView date_dep;
        TextView date_arr;
        TextView numVol;
        TextView dureeVol;
        ConstraintLayout FlightListItems;

        public FlightViewHolder(@NonNull View itemView){
            super(itemView);
            //callSignView = (TextView) itemView;
            Log.i(TAG,"i am on Flightviewholder class");
            airport_arr = itemView.findViewById(R.id.airport_arr_textView);
            airport_dep = itemView.findViewById(R.id.airport_dep_textView);
            date_dep = itemView.findViewById(R.id.date_dep_textView);
            date_arr = itemView.findViewById(R.id.date_arr_textView);
            numVol = itemView.findViewById(R.id.num_vol_textView);
            dureeVol = itemView.findViewById(R.id.duree_vol_textView);
            FlightListItems = itemView.findViewById(R.id.flightlist_list_items);



        }


    }

}
