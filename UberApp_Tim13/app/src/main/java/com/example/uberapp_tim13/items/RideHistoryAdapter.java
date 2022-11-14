package com.example.uberapp_tim13.items;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.tools.Globals;

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
        holder.price.setText("Price(RSD): " + ride.getPrice());
        fitFragmentToRole(holder, ride);
    }

    @Override
    public int getItemCount() {
        return rides.size();
    }


    private void fitFragmentToRole(RideHolder holder, RideItem ride) {
        switch (Globals.currentUser.getRole()) {
            case "driver":
                holder.addToFavs.setVisibility(View.GONE);
                holder.driverInfo.setVisibility(View.GONE);
                break;
            case "passenger":
                holder.driverInfo.setText("Driver: " + ride.getDriver().getName() + " " + ride.getDriver().getSurName());
                break;
        }
    }
}

