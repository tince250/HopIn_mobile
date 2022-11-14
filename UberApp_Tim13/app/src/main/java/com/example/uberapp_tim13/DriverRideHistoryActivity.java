package com.example.uberapp_tim13;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.uberapp_tim13.fragments.DriverRideHistoryDetailsFragment;
import com.example.uberapp_tim13.fragments.DriverRideHistoryFragment;
import com.example.uberapp_tim13.tools.FragmentTransition;

public class DriverRideHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_ride_history);
        FragmentTransition.to(DriverRideHistoryFragment.newInstance(), this, false, R.id.listRideHistory);
    }

    public void switchToHistoryDetails(View v){
        FragmentTransition.to(DriverRideHistoryDetailsFragment.newInstance(), this, false, R.id.listRideHistory);
    }

}