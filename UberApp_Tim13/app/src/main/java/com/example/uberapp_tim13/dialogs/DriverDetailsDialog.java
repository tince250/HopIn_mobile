package com.example.uberapp_tim13.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.model.User;
import com.example.uberapp_tim13.tools.Mockap;

public class DriverDetailsDialog extends Dialog {
    int rideNum;
    Activity activity;

    public DriverDetailsDialog(@NonNull Activity activity, int rideNum) {
        super(activity);
        this.activity = activity;
        this.rideNum = rideNum;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_driver_details);
        setComponents();
    }

    private void setComponents(){
        User driver = Mockap.rides.get(rideNum).getDriver();
        ((TextView) findViewById(R.id.detailsNameTV)).setText(driver.getName()+" "+driver.getSurName());
        ((TextView) findViewById(R.id.detailsEmailTV)).setText(driver.getEmail());
        ((TextView) findViewById(R.id.detailsContactTV)).setText("+381"+driver.getPhone());
        ((TextView) findViewById(R.id.detailsPlatesTV)).setText("TBD");
        ((TextView) findViewById(R.id.detailsVehicleTV)).setText("TBD");
    }
}

