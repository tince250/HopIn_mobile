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
import com.example.uberapp_tim13.dtos.ReviewReturnedDTO;
import com.example.uberapp_tim13.services.UserService;

import java.util.List;

public class RatingsAdapter extends BaseAdapter {

    private Activity activity;
    List<ReviewReturnedDTO> items;

    public RatingsAdapter(Activity activity, List<ReviewReturnedDTO> items) {
        this.activity = activity;
        this.items = items;
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
        ReviewReturnedDTO review = items.get(i);
        View view_new = view;
        if (view == null) {
            view_new = activity.getLayoutInflater().inflate(R.layout.fragment_ride_reviews, null);
        }

        ((RatingBar)view_new.findViewById(R.id.rating)).setRating(review.getRating());
        ((TextView)view_new.findViewById(R.id.commentTV)).setText(review.getComment());
        setReviewerName(view_new, review.getPassenger().getId());
        return view_new;
    }

    private void setReviewerName(View view_new, int userId){
        Intent intent = new Intent(this.activity, UserService.class);
        intent.putExtra("method", "getUserName");
        intent.putExtra("userId", userId);
        intent.putExtra("sender", "ratingsAdapter");
        this.activity.startService(intent);

        BroadcastReceiver broadcastReceiver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle extras = intent.getExtras();
                String name = (String) extras.get("userName");
                ((TextView)view_new.findViewById(R.id.nameTV)).setText(name);

            }
        };
        LocalBroadcastManager.getInstance(this.activity).registerReceiver(broadcastReceiver, new IntentFilter("ratingsAdapter"));
    }
}
