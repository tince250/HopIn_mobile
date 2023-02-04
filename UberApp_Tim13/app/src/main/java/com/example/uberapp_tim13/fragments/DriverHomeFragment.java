package com.example.uberapp_tim13.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.uberapp_tim13.R;

public class DriverHomeFragment extends Fragment {

    private MapFragment mapFragment;
    public static DriverHomeFragment newInstance() {
        return new DriverHomeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_driver_home, container, false);
        mapFragment = new MapFragment();
        getParentFragmentManager().beginTransaction().replace(R.id.map_container_aroundyou, mapFragment).commit();
        //mapFragment.pozovi();
        return view;
    }
}
