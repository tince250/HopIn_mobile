package com.example.uberapp_tim13.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.dtos.RideReturnedDTO;

public class RateRideDialog extends Dialog {
    RideReturnedDTO ride;
    Activity activity;

    public RateRideDialog(@NonNull Activity activity, RideReturnedDTO ride) {
        super(activity);
        this.activity = activity;
        this.ride = ride;
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_rate_ride_after);
    }
}
