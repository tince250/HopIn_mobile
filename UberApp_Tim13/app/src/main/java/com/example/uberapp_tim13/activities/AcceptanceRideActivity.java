package com.example.uberapp_tim13.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.dialogs.DeclineReasonDialog;

public class AcceptanceRideActivity extends AppCompatActivity {

    Dialog declineReasonDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Ride offer");
        setContentView(R.layout.activity_acceptance_ride);
        declineReasonDialog = new DeclineReasonDialog(this);
    }

    public void onClickDecline(View v){
        declineReasonDialog.show();
    }
}