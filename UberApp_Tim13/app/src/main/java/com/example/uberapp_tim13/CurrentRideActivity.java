package com.example.uberapp_tim13;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.uberapp_tim13.fragments.DriverHomeFragment;
import com.example.uberapp_tim13.fragments.PassengerHomeFragment;
import com.example.uberapp_tim13.tools.FragmentTransition;
import com.example.uberapp_tim13.tools.Globals;

public class CurrentRideActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Current ride");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_current_ride);

        fitActivityToRole();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                if (Globals.userRole.equals("driver")) {
                    startActivity(new Intent(this, DriverMainActivity.class));
                    return true;
                } else {
                    startActivity(new Intent(this, PassengerMainActivity.class));
                    return true;
                }
        }
        return super.onOptionsItemSelected(item);
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

    private void fitActivityToRole() {
        TextView name = findViewById(R.id.currentUser);
        TextView detail = findViewById(R.id.currentUserDetails);
        switch (Globals.userRole) {
            case "driver":
                name.setText("Passengers");
                detail.setText("Details about passengers");
                break;
            case "passenger":
                name.setText("Driver");
                detail.setText("Details about driver");
                break;
        }
    }
}
