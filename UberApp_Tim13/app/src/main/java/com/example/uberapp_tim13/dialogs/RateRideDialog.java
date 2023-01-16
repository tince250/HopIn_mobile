package com.example.uberapp_tim13.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.dtos.ReviewDTO;
import com.example.uberapp_tim13.dtos.RideReturnedDTO;
import com.example.uberapp_tim13.services.ReviewService;

public class RateRideDialog extends Dialog implements android.view.View.OnClickListener {
    RideReturnedDTO ride;
    Activity activity;
    Button submitBtn;
    EditText driverLeaveCommentET;
    EditText vehicleLeaveCommentET;
    RatingBar driverRatingBar;
    RatingBar vehicleRatingBar;

    public RateRideDialog(@NonNull Activity activity, RideReturnedDTO ride) {
        super(activity);
        this.activity = activity;
        this.ride = ride;

        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_rate_ride_after);

        setCancelable(false);
        setCanceledOnTouchOutside(false);

        setComponents();
        setListeners();
    }

    private void setComponents(){
        submitBtn = findViewById(R.id.submitBtn);
        driverLeaveCommentET = findViewById(R.id.driverLeaveCommentET);
        vehicleLeaveCommentET = findViewById(R.id.vehicleLeaveCommentET);
        driverRatingBar = findViewById(R.id.driverRatingBar);
        vehicleRatingBar = findViewById(R.id.vehicleRatingBar);
    }

    private void setListeners(){
        submitBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submitBtn:
                collectDataAndSetBroadcast();
                this.activity.finish();
                break;
            default:
                break;
        }
    }

    private void collectDataAndSetBroadcast(){

        ReviewDTO driverReviewDTO = new ReviewDTO((int) driverRatingBar.getRating(), driverLeaveCommentET.getText().toString());
        ReviewDTO vehicleReviewDTO = new ReviewDTO((int) vehicleRatingBar.getRating(), vehicleLeaveCommentET.getText().toString());

        Intent intent = new Intent(getContext(), ReviewService.class);
        intent.putExtra("method", "postBothReviews");
        intent.putExtra("rideId", ride.getId());
        intent.putExtra("driverReview", driverReviewDTO);
        intent.putExtra("vehicleReview", vehicleReviewDTO);
        this.activity.startService(intent);

        final boolean[] postedVehicle = new boolean[1];
        final boolean[] postedDriver = new boolean[1];
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle extras = intent.getExtras();
                if (!postedVehicle[0] && extras.get("postedVehicle") != null)
                    postedVehicle[0] = (boolean) extras.get("postedVehicle");
                if (!postedDriver[0]&& extras.get("postedDriver") != null)
                    postedDriver[0] = (boolean) extras.get("postedDriver");
                if (postedDriver[0] && postedVehicle[0])
                Toast.makeText(activity, "You reviewed the ride!", Toast.LENGTH_SHORT).show();
            }
        };
        LocalBroadcastManager.getInstance(this.activity).registerReceiver(broadcastReceiver, new IntentFilter("rateRideDialog"));

    }
}
