package com.example.uberapp_tim13.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.fragments.PassengerHomeFragment;
import com.example.uberapp_tim13.tools.FragmentTransition;

public class AcceptanceRideActivity extends AppCompatActivity {

    Dialog popUpDeclineReason;
    Button passengersBtn;
    Button familyBtn;
    Button otherBtn;
    EditText otherReasonET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Ride offer");
        setContentView(R.layout.activity_acceptance_ride);
        popUpDeclineReason = new Dialog(this);
    }

    public void onClickDecline(View v){
        popUpDeclineReason.setContentView(R.layout.pop_up_decline_reason);
        setListeners();
        popUpDeclineReason.show();
    }

    /*private void setListeners(){
        popUpDeclineReason.findViewById(R.id.passengersBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AcceptanceRideActivity.this, "1", Toast.LENGTH_SHORT).show();// horse
            }
        });
        popUpDeclineReason.findViewById(R.id.passengersBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AcceptanceRideActivity.this, "1", Toast.LENGTH_SHORT).show();// horse
            }
        });
        popUpDeclineReason.findViewById(R.id.passengersBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AcceptanceRideActivity.this, "1", Toast.LENGTH_SHORT).show();// horse
            }
        });
    }*/

    private void setListeners(){
        passengersBtn = popUpDeclineReason.findViewById(R.id.passengersBtn);
        familyBtn = popUpDeclineReason.findViewById(R.id.familyBtn);
        otherBtn = popUpDeclineReason.findViewById(R.id.otherBtn);
        otherReasonET = popUpDeclineReason.findViewById(R.id.otherReasonET);
        passengersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passengersBtn.setTextColor(getResources().getColor(R.color.teal_200));
                otherReasonET.setVisibility(View.GONE);
                resetButtonColor(familyBtn);
                resetButtonColor(otherBtn);
            }
        });
        familyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                familyBtn.setTextColor(getResources().getColor(R.color.teal_200));
                otherReasonET.setVisibility(View.GONE);
                resetButtonColor(passengersBtn);
                resetButtonColor(otherBtn);
            }
        });
        otherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                otherBtn.setTextColor(getResources().getColor(R.color.teal_200));
                otherReasonET.setVisibility(View.VISIBLE);
                resetButtonColor(passengersBtn);
                resetButtonColor(familyBtn);
            }
        });
    }

    private void resetButtonColor(Button button){
        button.setTextColor(getResources().getColor(R.color.disabled_gray));
    }
}