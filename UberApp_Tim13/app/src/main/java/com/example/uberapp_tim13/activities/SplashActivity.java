package com.example.uberapp_tim13.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uberapp_tim13.R;

import java.util.Timer;
import java.util.TimerTask;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        TextView slogan = findViewById(R.id.sloganTV);

        Animation zoomInAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in);

        slogan.postDelayed(new Runnable() {
            public void run() {
                slogan.setVisibility(View.VISIBLE);
                //slogan.setAnimation(zoomInAnimation);
            }
        }, 3000);

        TextView companyName = findViewById(R.id.companyNameTV);

        companyName.postDelayed(new Runnable() {
            public void run() {
                companyName.setVisibility(View.VISIBLE);
                //slogan2.setAnimation(zoomInAnimation);
            }
        }, 4000);

        int SPLASH_TIME_OUT = 5000;
        if (this.isConnectedToInternet(SplashActivity.this)) {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }
            }, SPLASH_TIME_OUT);
        } else {
            Toast.makeText(SplashActivity.this, "You have to be connected to internet", Toast.LENGTH_LONG).show();
            this.finishAffinity();
        }
    }

    public boolean isConnectedToInternet(Activity a) {
        boolean isConnectedToWifi = false;
        boolean isConnectedToMobileData = false;
        try {

            ConnectivityManager cm = (ConnectivityManager) a.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo[] networkInfos = cm.getAllNetworkInfo();
            for (NetworkInfo networkInfo : networkInfos) {
                if (networkInfo.getTypeName().equalsIgnoreCase("wifi"))
                    if (networkInfo.isConnected())
                        isConnectedToWifi = true;
                if (networkInfo.getTypeName().equalsIgnoreCase("mobile"))
                    if (networkInfo.isConnected())
                        isConnectedToMobileData = true;
            }
            Log.d("rbrbr", String.valueOf(isConnectedToWifi));
            Log.d("rrbrb", String.valueOf(isConnectedToMobileData));
            return isConnectedToWifi || isConnectedToMobileData;
        }
        catch (Exception ex) {
            Log.d("error", "error");
        }
        return false;
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