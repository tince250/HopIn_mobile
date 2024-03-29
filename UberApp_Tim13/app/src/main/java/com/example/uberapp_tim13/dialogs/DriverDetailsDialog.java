package com.example.uberapp_tim13.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.dtos.RideReturnedDTO;
import com.example.uberapp_tim13.dtos.UserReturnedDTO;
import com.example.uberapp_tim13.dtos.VehicleDTO;
import com.example.uberapp_tim13.services.DriverService;
import com.example.uberapp_tim13.services.UserService;
import com.example.uberapp_tim13.tools.Utils;

public class DriverDetailsDialog extends Dialog {
    RideReturnedDTO ride;
    Activity activity;

    public DriverDetailsDialog(@NonNull Activity activity, RideReturnedDTO ride) {
        super(activity);
        this.activity = activity;
        this.ride = ride;

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_driver_details);
        setBroadcast();
    }

    private void setComponents(){
        ((TextView) findViewById(R.id.detailsNameTV)).setText(driver.getName()+" "+driver.getSurname());
        ((TextView) findViewById(R.id.detailsEmailTV)).setText("Email: " +driver.getEmail());
        ((TextView) findViewById(R.id.detailsContactTV)).setText("Phone: " + driver.getTelephoneNumber());
        ((TextView) findViewById(R.id.detailsPlatesTV)).setText("Plates: " + vehicle.getLicenseNumber());
        ((TextView) findViewById(R.id.detailsVehicleTV)).setText("Model: " + vehicle.getModel());
        ((TextView) findViewById(R.id.vehicleTypeTV)).setText("Type: " + vehicle.getVehicleType());

        if (driver.getProfilePicture() != null) {
            ImageView profilePic = findViewById(R.id.ic_profile);
            profilePic.setImageBitmap(Utils.convertBase64ToBitmap(driver.getProfilePicture()));
        }
    }

    private void setBroadcast(){
        Intent intentUserService = new Intent(getContext(), UserService.class);

        intentUserService.putExtra("method", "getById");
        intentUserService.putExtra("userId", ride.getDriver().getId());
        this.activity.startService(intentUserService);

        Intent intentDriverService = new Intent(getContext(), DriverService.class);
        intentDriverService.putExtra("method", "getVehicle");
        intentDriverService.putExtra("driverId", ride.getDriver().getId());
        this.activity.startService(intentDriverService);

        BroadcastReceiver broadcastReceiver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle extras = intent.getExtras();
                if (driver == null)
                    driver = (UserReturnedDTO) extras.get("userById");
                if (vehicle == null)
                    vehicle = (VehicleDTO) extras.get("vehicle");
                if (vehicle != null && driver != null)
                    setComponents();
            }
        };
        LocalBroadcastManager.getInstance(this.activity).registerReceiver(broadcastReceiver, new IntentFilter("userDetailsDialog"));

    }

    private UserReturnedDTO driver = null;
    private VehicleDTO vehicle = null;
}

