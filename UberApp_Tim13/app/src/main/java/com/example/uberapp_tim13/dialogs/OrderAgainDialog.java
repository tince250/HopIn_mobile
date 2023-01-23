package com.example.uberapp_tim13.dialogs;


import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.activities.LoginActivity;
import com.example.uberapp_tim13.activities.PassengerMainActivity;
import com.example.uberapp_tim13.dtos.LocationDTO;
import com.example.uberapp_tim13.dtos.RouteDTO;
import com.example.uberapp_tim13.services.RideService;
import com.example.uberapp_tim13.tools.Utils;
import com.google.android.material.button.MaterialButton;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Calendar;

public class OrderAgainDialog extends Dialog implements View.OnClickListener {

    private RouteDTO route;
    private Activity activity;
    private Button confirmBtn;
    private Button cancelBtn;
    private EditText pickUpTime;
    private TextView timeError;
    private Button asapBtn;

    public OrderAgainDialog(@NonNull Activity activity, RouteDTO route) {
        super(activity);
        this.activity = activity;
        this.route = route;

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_order_again);
        setComponents();
        setListeners();
    }

    private void setComponents(){
        confirmBtn = findViewById(R.id.orderAgainButton);
        cancelBtn = findViewById(R.id.cancelOrderAgainButton);
        ((TextView) findViewById(R.id.locationTV)).setText(route.getDeparture().getAddress() + " -> " + route.getDestination().getAddress());
        pickUpTime = findViewById(R.id.pickUpTimeAgainET);
        timeError = findViewById(R.id.timeErrorTV);
        asapBtn = findViewById(R.id.asapButton);

        pickUpTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(activity, R.style.TimePickerTheme, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        Utils.changeBtnToGray((MaterialButton) asapBtn, activity);

                        pickUpTime.setText( selectedHour + ":" + selectedMinute);
                        LocalDateTime time = LocalDateTime.of(LocalDate.now(), LocalTime.of(selectedHour, selectedMinute, 0));
                        if (time.isBefore(LocalDateTime.now())) {
                            time = time.plusDays(1);
                        }
                        if (time.minusHours(5).isAfter(LocalDateTime.now())) {
                            pickUpTime.setError("Schedule max 5h in advance.");
                            timeError.setVisibility(View.VISIBLE);
                            confirmBtn.setEnabled(false);
                        } else {
                            confirmBtn.setEnabled(true);
                            pickUpTime.setError(null);
                            timeError.setVisibility(View.GONE);
                            RideService.rideInCreation.setScheduledTime(time.toString());
                        }

                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });
    }

    private void setListeners(){
        confirmBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        asapBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.orderAgainButton:
                RideService.rideInCreation.setLocations(new ArrayList<LocationDTO>() {{ add(new LocationDTO(route.getDeparture(), route.getDestination())); }});
                Intent i = new Intent(activity, PassengerMainActivity.class);
                i.putExtra("orderAgain", "true");
                activity.startActivity(i);
                break;
            case R.id.cancelOrderAgainButton:
                dismiss();
                break;
            case R.id.asapButton:
                Utils.changeBtnToBlue((MaterialButton) asapBtn, activity);
                confirmBtn.setEnabled(true);
                pickUpTime.setText(null);
                pickUpTime.setError(null);
                timeError.setVisibility(View.GONE);
                RideService.rideInCreation.setScheduledTime(null);
                break;
            default:
                break;
        }
    }


}
