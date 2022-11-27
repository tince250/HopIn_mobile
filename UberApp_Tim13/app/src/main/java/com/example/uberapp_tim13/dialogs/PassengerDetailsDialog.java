package com.example.uberapp_tim13.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.adapters.ride_history.PassengerDetailsAdapter;

public class PassengerDetailsDialog extends Dialog {
    int rideNum;
    Activity activity;
    ListView listView;

    public PassengerDetailsDialog(@NonNull Activity activity, int rideNum) {
        super(activity);
        this.activity = activity;
        this.rideNum = rideNum;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_passenger_details);
        setComponents();
    }

    private void setComponents(){
        listView = (ListView) findViewById(android.R.id.list);
        PassengerDetailsAdapter adapter = new PassengerDetailsAdapter(activity, rideNum);
        listView.setAdapter(adapter);
    }
}
