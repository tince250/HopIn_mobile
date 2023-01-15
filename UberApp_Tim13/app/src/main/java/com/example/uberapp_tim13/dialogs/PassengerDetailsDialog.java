package com.example.uberapp_tim13.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.adapters.ride_history.PassengerDetailsAdapter;
import com.example.uberapp_tim13.dtos.rides.RideReturnedDTO;
import com.example.uberapp_tim13.dtos.UserDTO;
import com.example.uberapp_tim13.services.UserService;

import java.util.ArrayList;
import java.util.List;

public class PassengerDetailsDialog extends Dialog {
    RideReturnedDTO ride;
    Activity activity;
    ListView listView;
    int counter = 0;

    public PassengerDetailsDialog(@NonNull Activity activity, RideReturnedDTO ride) {
        super(activity);
        this.activity = activity;
        this.ride = ride;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_passenger_details);
        setBroadcast();
    }

    private void setComponents(){
        listView = (ListView) findViewById(android.R.id.list);
        PassengerDetailsAdapter adapter = new PassengerDetailsAdapter(activity, users);
        listView.setAdapter(adapter);
    }

    private void setBroadcast(){
        Intent intentUserService = new Intent(getContext(), UserService.class);

        for (int i = 0; i < ride.getPassengers().size(); i++){
            intentUserService.putExtra("method", "getById");
            intentUserService.putExtra("userId", ride.getPassengers().get(i).getId());
            this.activity.startService(intentUserService);
        }
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle extras = intent.getExtras();
                users.add((UserDTO) extras.get("userById"));
                counter++;
                if (counter == ride.getPassengers().size())
                    setComponents();
            }
        };
        LocalBroadcastManager.getInstance(this.activity).registerReceiver(broadcastReceiver, new IntentFilter("userDetailsDialog"));

    }

    private List<UserDTO> users = new ArrayList<UserDTO>();
}
