package com.example.uberapp_tim13.adapters.ride_history;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.dtos.CompleteRideReviewDTO;
import com.example.uberapp_tim13.dtos.ReviewReturnedDTO;
import com.example.uberapp_tim13.services.UserService;
import com.example.uberapp_tim13.tools.Globals;

import java.util.List;

public class RatingsAdapter extends BaseAdapter {

    private Activity activity;
    List<CompleteRideReviewDTO> reviews;

    public RatingsAdapter(Activity activity, List<CompleteRideReviewDTO> items) {
        this.activity = activity;
        this.reviews = items;
    }

    @Override
    public int getCount() {
        return reviews.size();
    }

    @Override
    public Object getItem(int i) {
        return reviews.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        CompleteRideReviewDTO review = reviews.get(i);
        View view_new = view;
        if (view == null) {
            view_new = activity.getLayoutInflater().inflate(R.layout.fragment_ride_reviews, null);
        }

        ((RatingBar)view_new.findViewById(R.id.ratingDriver)).setRating(review.getDriverReview().getRating());
        ((RatingBar)view_new.findViewById(R.id.ratingVehicle)).setRating(review.getVehicleReview().getRating());
        ((TextView)view_new.findViewById(R.id.commentDriverTV)).setText(review.getDriverReview().getComment());
        ((TextView)view_new.findViewById(R.id.commentVehicleTV)).setText(review.getVehicleReview().getComment());
        if (review.getDriverReview().getPassenger().getId() != Globals.user.getId())
            ((TextView)view_new.findViewById(R.id.nameTV)).setText(review.getDriverReview().getPassenger().getName() + " " + review.getDriverReview().getPassenger().getSurname() + ":");
        else
            ((TextView)view_new.findViewById(R.id.nameTV)).setText("You:");
        return view_new;
    }

}
