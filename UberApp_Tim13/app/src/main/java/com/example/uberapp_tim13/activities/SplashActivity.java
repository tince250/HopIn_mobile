package com.example.uberapp_tim13.activities;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.dialogs.LocationDialog;

import java.util.Timer;
import java.util.TimerTask;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends Activity {

    private LocationManager locationManager;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        locationManager = (LocationManager) SplashActivity.this.getSystemService(Context.LOCATION_SERVICE);

        TextView slogan = findViewById(R.id.sloganTV);

        slogan.postDelayed(new Runnable() {
            public void run() {
                slogan.setVisibility(View.VISIBLE);
            }
        }, 3000);

        TextView companyName = findViewById(R.id.companyNameTV);

        companyName.postDelayed(new Runnable() {
            public void run() {
                companyName.setVisibility(View.VISIBLE);
            }
        }, 4000);

        boolean connectedToInternet = this.isConnectedToInternet(SplashActivity.this);
        boolean locationEnabled = this.isLocationEnabled();

        if (connectedToInternet && locationEnabled) {
            moveToLogin(5000);
        } else if (!connectedToInternet){
            Toast.makeText(SplashActivity.this, "You have to be connected to internet", Toast.LENGTH_LONG).show();
            this.finishAffinity();
        } else {
            this.showLocationDialog();
        }
    }

    private void moveToLogin(int SPLASH_TIME_OUT) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    public boolean isLocationEnabled(){
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean wifi = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        return gps || wifi;
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

    private void showLocationDialog() {
        if (dialog == null) {
            dialog = new LocationDialog(SplashActivity.this).prepareDialog();
        } else {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }

        dialog.show();
        setDialogButtonsAndColors();
    }

    public void setDialogButtonsAndColors() {
        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setTextColor(getApplicationContext().getResources().getColor(R.color.dark_blue));
        Button negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        negativeButton.setTextColor(getApplicationContext().getResources().getColor(R.color.dark_blue));
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (isLocationEnabled()){
            moveToLogin(0);
        } else {
            Toast.makeText(SplashActivity.this, "You have to enable location", Toast.LENGTH_LONG).show();
            this.finishAffinity();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //locationManager.removeUpdates(this);
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