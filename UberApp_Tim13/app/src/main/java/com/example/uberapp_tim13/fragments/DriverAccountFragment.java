package com.example.uberapp_tim13.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.uberapp_tim13.DriverAccountSettingsActivity;
import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.UserLoginActivity;
import com.example.uberapp_tim13.tools.Globals;

public class DriverAccountFragment extends Fragment {
    public static DriverAccountFragment newInstance() {
        return new DriverAccountFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_driver_account, container, false);

        fitFragmentToRole(view);

        view.findViewById(R.id.accountSettingBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), DriverAccountSettingsActivity.class));
            }
        });

        view.findViewById(R.id.logoutBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), UserLoginActivity.class));
            }
        });

        return view;
    }

    private void fitFragmentToRole(View view) {
        switch (Globals.userRole) {
            case "driver":
                view.findViewById(R.id.payment_info_card).setVisibility(View.GONE);
                break;
            case "passenger":
                view.findViewById(R.id.vehicle_info_card).setVisibility(View.GONE);
                break;
        }
        ((TextView) view.findViewById(R.id.user_role)).setText(Globals.userRole);
    }
}
