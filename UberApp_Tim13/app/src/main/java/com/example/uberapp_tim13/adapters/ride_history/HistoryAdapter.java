package com.example.uberapp_tim13.adapters.ride_history;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.dtos.AllPassengerRidesDTO;
import com.example.uberapp_tim13.dtos.CompleteRideReviewDTO;
import com.example.uberapp_tim13.dtos.RideReturnedDTO;
import com.example.uberapp_tim13.dtos.VehicleDTO;
import com.example.uberapp_tim13.model.Ride;
import com.example.uberapp_tim13.rest.RestUtils;
import com.example.uberapp_tim13.services.DriverService;
import com.example.uberapp_tim13.services.ReviewService;
import com.example.uberapp_tim13.tools.Globals;
import com.example.uberapp_tim13.tools.Mockap;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryAdapter extends BaseAdapter {

    private Activity activity;
    List<RideReturnedDTO> items;

    public HistoryAdapter(List<RideReturnedDTO> rides, Activity activity) {
        this.activity = activity;
        this.items = rides;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        RideReturnedDTO ride = items.get(i);
        View view_new = view;

        if (view == null) {
            view_new = activity.getLayoutInflater().inflate(R.layout.ride_history_item, null);
        }

        ((TextView)view_new.findViewById(R.id.routeTV)).setText(ride.getRouteDepartureDestinationTitle());
        ((TextView)view_new.findViewById(R.id.dateTV)).setText(ride.getStartDateTextView());
        ((TextView)view_new.findViewById(R.id.passengersTV)).setText("Passengers: " + ride.getPassengers().size());
        ((TextView)view_new.findViewById(R.id.priceTV)).setText("Price(RSD): " + ride.getTotalCost());
        setRatingBar(view_new, ride.getId());

        fitFragmentToRole(view_new, ride);

        return view_new;
    }

    private void setRatingBar(View view_new, int rideId) {
        Intent intent = new Intent(this.activity, ReviewService.class);
        intent.putExtra("method", "calculateRideReviews");
        intent.putExtra("rideId", rideId);
        Log.d("ovojeid", String.valueOf(rideId));
        this.activity.startService(intent);

        View finalView_new = view_new;
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle extras = intent.getExtras();
                float rating = (float) extras.get("allRideReviewsRating");
                ((RatingBar) finalView_new.findViewById(R.id.ratingBar)).setRating(rating);
            }
        };
        LocalBroadcastManager.getInstance(this.activity).registerReceiver(broadcastReceiver, new IntentFilter("historyAdapter"));
    }

    private void fitFragmentToRole(View view_new, RideReturnedDTO ride) {
        switch (Globals.userRole) {
            case "driver":
                view_new.findViewById(R.id.addToFavImg).setVisibility(View.GONE);
                view_new.findViewById(R.id.driverTV).setVisibility(View.GONE);
                break;
            case "passenger":
                ((TextView)view_new.findViewById(R.id.driverTV)).setText("Driver: " + ride.getDriver().getEmail());
                break;
        }
    }
}
