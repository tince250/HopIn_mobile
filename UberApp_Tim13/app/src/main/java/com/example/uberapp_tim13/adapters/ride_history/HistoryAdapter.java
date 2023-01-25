package com.example.uberapp_tim13.adapters.ride_history;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.dtos.LocationNoIdDTO;
import com.example.uberapp_tim13.dtos.RideReturnedDTO;
import com.example.uberapp_tim13.dtos.RouteDTO;
import com.example.uberapp_tim13.rest.RestUtils;
import com.example.uberapp_tim13.services.AuthService;
import com.example.uberapp_tim13.services.ReviewService;
import com.example.uberapp_tim13.tools.Globals;

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
            view_new = activity.getLayoutInflater().inflate(R.layout.ride_history_item, viewGroup, false);
        }

        ((TextView)view_new.findViewById(R.id.routeTV)).setText(ride.getRouteDepartureDestinationTitle());
        ((TextView)view_new.findViewById(R.id.dateTV)).setText(ride.getStartDateTextView());
        ((TextView)view_new.findViewById(R.id.passengersTV)).setText("Passengers: " + ride.getPassengers().size());
        ((TextView)view_new.findViewById(R.id.priceTV)).setText("Price: " + ride.getTotalCost() + " RSD");
        ((TextView)view_new.findViewById(R.id.rideDistanceTV)).setText("distance: " + ride.getDistance() + " km");;
        setRatingBar(view_new, ride.getId());

        ImageView favIcon = view_new.findViewById(R.id.addToFavImg);
        ImageView repeatIcon = view_new.findViewById(R.id.repeatImg);

        repeatIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        Log.d("KLIK", ride.getId() + "");
        Call<Boolean> call = RestUtils.passengerAPI.isFavouriteRoute(AuthService.tokenDTO.getAccessToken(), ride.getId());
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    Boolean isFav = response.body();
                    if (isFav) {
                        paintRed(favIcon);
                    } else {
                        setAddToFavsListener(favIcon, ride);
                    }
                } else {
                    Log.e("FAV", "Oops! Error while trying to fetch fav routes.");
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.e("FAV", "Oops! Error while trying to fetch fav routes.");
            }
        });

        fitFragmentToRole(view_new, ride);

        return view_new;
    }

    private void paintRed(ImageView favIcon) {
        ImageViewCompat.setImageTintList(favIcon, ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.red)));
    }

    private void setAddToFavsListener(ImageView favIcon, RideReturnedDTO ride) {
        RouteDTO dto = new RouteDTO(0, ride.getDistance(), ride.getLocations().get(0).getDeparture(), ride.getLocations().get(0).getDestination());

        favIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<Void> call = RestUtils.passengerAPI.addFavouriteRoute(AuthService.tokenDTO.getAccessToken(), Globals.user.getId(), dto);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            favIcon.setOnClickListener(null);
                            Toast.makeText(activity, "Route added to favorites!", Toast.LENGTH_LONG).show();
                            HistoryAdapter.this.notifyDataSetChanged();
                        } else {
                            Toast.makeText(activity, "Error happened while trying to add ride to favs :(", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(activity, "Error happened while trying to add ride to favs!", Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
    }

    private void setRatingBar(View view_new, int rideId) {
        Intent intent = new Intent(this.activity, ReviewService.class);
        intent.putExtra("method", "calculateRideReviews");
        intent.putExtra("rideId", rideId);
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
                view_new.findViewById(R.id.driverTV).setVisibility(View.GONE);
                break;
        }
    }
}
