package com.example.uberapp_tim13;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.uberapp_tim13.tools.BottomNavBarHandler;

public class DriverRideHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_ride_history);
        setTitle("Ride history");

        BottomNavBarHandler.setBottomNavigationBar(findViewById(R.id.bottomNavigationView), this);
    }
}