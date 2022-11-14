package com.example.uberapp_tim13.items;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uberapp_tim13.R;

import java.util.List;

public class RideHistoryAdapter extends RecyclerView.Adapter<RideHolder> {

    Context context;
    List<RideItem> rides;

    public RideHistoryAdapter(Context context, List<RideItem> rides) {
        this.context = context;
        this.rides = rides;
    }

    @NonNull
    @Override
    public RideHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RideHolder(LayoutInflater.from(context).inflate(R.layout.ride_history_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RideHolder holder, int position) {
        RideItem ride = rides.get(position);
        holder.route.setText(ride.getPickUpLocation()+" -> "+ride.getDestination());
        String stops = "";
        for (String s : ride.getStops()){
            stops = stops + s +", ";
        }
        holder.stops.setText("Stops: "+ stops);
        holder.date.setText(ride.getStartTime());
        holder.passengers.setText("Passengers: " + ride.getPassengers().size());
        holder.price.setText("Price (RSD)" + ride.getPrice());
    }

    @Override
    public int getItemCount() {
        return rides.size();
    }
}

