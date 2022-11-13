package com.example.uberapp_tim13;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PassengerMainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_main);
        setTitle("Home");

        setBottomNavigationBar();
    }

    public void openAccountPage() {
        startActivity(new Intent(PassengerMainActivity.this, PassengerAccountActivity.class));
    }

    public void openInboxPage(View v) {
        startActivity(new Intent(PassengerMainActivity.this, PassengerInboxActivity.class));
    }


    // Sets event listeners for the bottom nav bar
    public void setBottomNavigationBar() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            //redirection to other activities/fragments
            switch (item.getItemId()) {
                case R.id.nav_inbox:
                    Toast.makeText(this, "inbox", Toast.LENGTH_LONG).show();
                    break;
                case R.id.nav_home:
                    Toast.makeText(this, "home", Toast.LENGTH_LONG).show();
                    break;
                case R.id.nav_profile:
                    Toast.makeText(this, "profile", Toast.LENGTH_LONG).show();
                    break;
                case R.id.nav_history:
                    Toast.makeText(this, "history", Toast.LENGTH_LONG).show();
                    break;
            }
            return true;
        });
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.passenger_upper_menu, menu);

        MenuItem switchItem = menu.findItem(R.id.liveRide);
        switchItem.setActionView(R.layout.current_ride_button_bar);
        final Button liveBtn = (Button) menu.findItem(R.id.liveRide).getActionView().findViewById(R.id.currentRideBarBtn);
        liveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "go to current ride", Toast.LENGTH_LONG);
                startActivity(new Intent(PassengerMainActivity.this, CurrentRideActivity.class));
            }
        });
        return true;
    }

    public void addStops(View v) {
        Toast.makeText(this, "add stops dialog should pop up", Toast.LENGTH_LONG);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}