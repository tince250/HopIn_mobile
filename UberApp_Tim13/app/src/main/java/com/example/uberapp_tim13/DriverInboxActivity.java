package com.example.uberapp_tim13;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.uberapp_tim13.tools.BottomNavBarHandler;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DriverInboxActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_inbox);
        setTitle("Inbox");

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        BottomNavBarHandler.setBottomNavigationBar(bottomNav, this);
        bottomNav.setSelectedItemId(R.id.nav_inbox);
    }
}