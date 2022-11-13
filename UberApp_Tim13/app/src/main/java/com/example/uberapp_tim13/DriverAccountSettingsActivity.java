package com.example.uberapp_tim13;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.uberapp_tim13.fragments.ChangePasswordFragment;
import com.example.uberapp_tim13.fragments.DriverAccountSettingsFragment;
import com.example.uberapp_tim13.fragments.PaymentInfoFragment;
import com.example.uberapp_tim13.tools.FragmentTransition;

public class DriverAccountSettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Account Settings");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_driver_acc_settings);
        FragmentTransition.to(DriverAccountSettingsFragment.newInstance(), this, false, R.id.driver_acc_sett_fl);
    }

    public void switchToChangePassword(View view) {
        setTitle("Change password");
        FragmentTransition.to(ChangePasswordFragment.newInstance(), this, true, R.id.driver_acc_sett_fl);
        overridePendingTransition(0, 0);
    }

    public void switchToPaymentInfo(View view) {
        setTitle("Payment info");
        FragmentTransition.to(PaymentInfoFragment.newInstance(), this, true, R.id.driver_acc_sett_fl);
        overridePendingTransition(0, 0);
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
