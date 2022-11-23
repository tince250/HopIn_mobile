package com.example.uberapp_tim13.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.uberapp_tim13.R;

public class AcceptanceRideActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Ride offer");
        setContentView(R.layout.activity_acceptance_ride);
    }

    public void onClickDecline(View v){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Why have you declined a ride?");

// add a list
        String[] reasons = {"Passengers haven't come",
                "Health problems",
        "My dog died"};
        dialog.setItems(reasons, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        Toast.makeText(AcceptanceRideActivity.this, "1", Toast.LENGTH_SHORT).show();// horse
                    case 1:
                        Toast.makeText(AcceptanceRideActivity.this, "2", Toast.LENGTH_SHORT).show();// cow
                    case 2:
                        Toast.makeText(AcceptanceRideActivity.this, "3", Toast.LENGTH_SHORT).show();// camel
                }
            }
        });

// create and show the alert dialog
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }
}