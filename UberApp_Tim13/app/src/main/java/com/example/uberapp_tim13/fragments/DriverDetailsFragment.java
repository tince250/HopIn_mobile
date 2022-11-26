package com.example.uberapp_tim13.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.adapters.ride_history.PassengerDetailsAdapter;
import com.example.uberapp_tim13.model.Ride;
import com.example.uberapp_tim13.model.User;
import com.example.uberapp_tim13.tools.Mockap;

import org.w3c.dom.Text;

import java.util.List;

public class DriverDetailsFragment  extends DialogFragment {
    public static int rideNum;

    public static DriverDetailsFragment newInstance(int r) {
        DriverDetailsFragment fragment = new DriverDetailsFragment();
        rideNum = r;
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_driver_details, container, false);
        User driver = Mockap.rides.get(rideNum).getDriver();
        ((TextView) view.findViewById(R.id.detailsNameTV)).setText(driver.getName()+" "+driver.getSurName());
        ((TextView) view.findViewById(R.id.detailsEmailTV)).setText(driver.getEmail());
        ((TextView) view.findViewById(R.id.detailsContactTV)).setText("+381"+driver.getPhone());
        ((TextView) view.findViewById(R.id.detailsPlatesTV)).setText("TBD");
        ((TextView) view.findViewById(R.id.detailsVehicleTV)).setText("TBD");
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }
}
