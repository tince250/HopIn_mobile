package com.example.uberapp_tim13;

import static android.app.PendingIntent.getActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.uberapp_tim13.fragments.DriverStatisticsFragment;
import com.example.uberapp_tim13.tools.FragmentTransition;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DriverAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Account");
        setContentView(R.layout.activity_driver_acc_settings);

        setBottomNavigationBar();
    }

    public void onClickStatistics(View v){
        startActivity(new Intent(DriverAccountActivity.this, DriverStatisticsActivity.class));
    }

    public void onClickSettings(View v){
        startActivity(new Intent(DriverAccountActivity.this, DriverAccountSettingsActivity.class));
    }

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
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);
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