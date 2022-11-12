package com.example.uberapp_tim13;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.uberapp_tim13.tools.BottomNavBarHandler;

public class DriverProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_profile);
        setTitle("Profile");

        BottomNavBarHandler.setBottomNavigationBar(findViewById(R.id.bottomNavigationView), this);
    }
}