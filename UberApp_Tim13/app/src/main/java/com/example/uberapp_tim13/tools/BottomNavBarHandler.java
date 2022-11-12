package com.example.uberapp_tim13.tools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.uberapp_tim13.DriverInboxActivity;
import com.example.uberapp_tim13.DriverMainActivity;
import com.example.uberapp_tim13.DriverProfileActivity;
import com.example.uberapp_tim13.DriverRideHistoryActivity;
import com.example.uberapp_tim13.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavBarHandler {
    public static void setBottomNavigationBar(BottomNavigationView bottomNavigationView, Context context) {;

        bottomNavigationView.setOnItemSelectedListener(item -> {
            //redirection to other activities/fragments
            switch (item.getItemId()) {
                case R.id.nav_inbox:
                    if (context.getClass() != DriverInboxActivity.class) {
                        context.startActivity(new Intent(context, DriverInboxActivity.class));
                        ((Activity) context).overridePendingTransition(0, 0);
                    }

                    break;
                case R.id.nav_home:
                    if (context.getClass() != DriverMainActivity.class)
                        context.startActivity(new Intent(context, DriverMainActivity.class));
                    break;
                case R.id.nav_profile:
                    if (context.getClass() != DriverProfileActivity.class)
                        context.startActivity(new Intent(context, DriverProfileActivity.class));
                    break;
                case R.id.nav_history:
                    if (context.getClass() != DriverRideHistoryActivity.class)
                        context.startActivity(new Intent(context, DriverRideHistoryActivity.class));
                    break;
            }
            return false;
        });
    }
}
