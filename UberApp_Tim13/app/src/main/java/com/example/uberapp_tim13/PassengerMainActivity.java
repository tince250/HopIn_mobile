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
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PassengerMainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_main);

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