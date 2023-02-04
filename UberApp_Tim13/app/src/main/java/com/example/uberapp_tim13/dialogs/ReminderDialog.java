package com.example.uberapp_tim13.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.activities.CurrentRideActivity;
import com.example.uberapp_tim13.dtos.RideReturnedDTO;
import com.example.uberapp_tim13.model.Ride;
import com.example.uberapp_tim13.rest.RestUtils;
import com.example.uberapp_tim13.services.AuthService;
import com.example.uberapp_tim13.services.RideService;
import com.example.uberapp_tim13.tools.Globals;
import com.example.uberapp_tim13.tools.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReminderDialog extends Dialog {

    RideReturnedDTO ride;
    TextView rideTV;
    TextView dateTV;
    Context activity;

    public ReminderDialog(@NonNull Context activity, RideReturnedDTO ride) {
        super(activity);
        this.activity = activity;
        this.ride = ride;
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_reminder);

        this.rideTV = (TextView) findViewById(R.id.rideTV);
        this.dateTV = (TextView) findViewById(R.id.dateTV);

        this.rideTV.setText(ride.getLocations().get(0).getDeparture().getAddress() + " -> " + ride.getLocations().get(0).getDestination().getAddress());
        this.dateTV.setText(Utils.formatDate(ride.getScheduledTime()));

        Button closeBtn = findViewById(R.id.closeBtn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        if (Globals.userRole.equals("driver")) {
            Button startBtn = findViewById(R.id.startBtn);
            startBtn.setVisibility(View.VISIBLE);
            startBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Call<RideReturnedDTO> call = RestUtils.rideAPI.startRideToDeparture(AuthService.tokenDTO.getAccessToken(), ride.getId());
                    call.enqueue(new Callback<RideReturnedDTO>() {
                        @Override
                        public void onResponse(Call<RideReturnedDTO> call, Response<RideReturnedDTO> response) {
                            if (response.isSuccessful()) {
                                RideService.returnedRide = response.body();
                                Intent i = new Intent(activity, CurrentRideActivity.class);
                                i.putExtra("ride", response.body());
                                activity.startActivity(i);
                                dismiss();
                            }
                        }

                        @Override
                        public void onFailure(Call<RideReturnedDTO> call, Throwable t) {

                        }
                    });
                }
            });
        } else {
            //TODO: resi align close dugmeta
        }
    }

}
