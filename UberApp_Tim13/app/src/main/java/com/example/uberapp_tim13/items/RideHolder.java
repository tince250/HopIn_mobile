package com.example.uberapp_tim13.items;

import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uberapp_tim13.R;

public class RideHolder extends RecyclerView.ViewHolder {

    TextView route, stops, date, passengers, price;
    RatingBar rating;

    public RideHolder(@NonNull View v) {
        super(v);
        route = v.findViewById(R.id.textViewRoute);
        stops = v.findViewById(R.id.textViewStops);
        date = v.findViewById(R.id.textViewDate);
        passengers = v.findViewById(R.id.textViewPassengers);
        price = v.findViewById(R.id.textViewPrice);
        rating = v.findViewById(R.id.rating);
    }
}
