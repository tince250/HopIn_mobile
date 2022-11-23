package com.example.uberapp_tim13.fragments;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.uberapp_tim13.activities.AccountSettingsActivity;
import com.example.uberapp_tim13.activities.DriverReportsActivity;
import com.example.uberapp_tim13.activities.DriverStatisticsActivity;
import com.example.uberapp_tim13.activities.FavoriteRoutesActivity;
import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.activities.LoginActivity;
import com.example.uberapp_tim13.activities.PassengerReportsActivity;
import com.example.uberapp_tim13.tools.Globals;
import com.google.android.material.button.MaterialButton;

public class AccountFragment extends Fragment {
    public static AccountFragment newInstance() {
        return new AccountFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        fitFragmentToRole(view);

        view.findViewById(R.id.accountSettingBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AccountSettingsActivity.class));
            }
        });


        view.findViewById(R.id.logoutBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });

        view.findViewById(R.id.statisticsBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (Globals.currentUser.getRole()) {
                    case "driver":
                        startActivity(new Intent(getActivity(), DriverStatisticsActivity.class));
                        break;
                    case "passenger":
                        startActivity(new Intent(getActivity(), FavoriteRoutesActivity.class));
                        break;
                    default:
                        break;
                }

            }
        });

        view.findViewById(R.id.reportsBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (Globals.currentUser.getRole()) {
                    case "driver":
                        startActivity(new Intent(getActivity(), DriverReportsActivity.class));
                        break;
                    case "passenger":
                        startActivity(new Intent(getActivity(), PassengerReportsActivity.class));
                        break;
                    default:
                        break;
                }

            }
        });

        return view;
    }

    private void fitFragmentToRole(View view) {
        MaterialButton button = view.findViewById(R.id.statisticsBtn);
        switch (Globals.userRole) {
            case "driver":
                view.findViewById(R.id.payment_info_card).setVisibility(View.GONE);
                button.setIcon(ContextCompat.getDrawable(this.getContext(), R.drawable.statistics));
                button.setText("statistics");
                break;
            case "passenger":
                view.findViewById(R.id.vehicle_info_card).setVisibility(View.GONE);
                button.setIcon(ContextCompat.getDrawable(this.getContext(), R.drawable.heart_solid));
                button.setIconTint(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                button.setText("routes");
                ((TextView) view.findViewById(R.id.cardTV)).setText("**** **** *** " + Globals.currentUser.getCard().getNumber().split(" ")[3]);
                ((TextView) view.findViewById(R.id.cardTypeTV)).setText(Globals.currentUser.getCard().getType());
                break;
        }
        fitTextViewsToUser(view);
    }

    private void fitTextViewsToUser(View view) {
        ((TextView) view.findViewById(R.id.userRoleTV)).setText(Globals.userRole);
        ((TextView) view.findViewById(R.id.driverNameTitleTV)).setText(Globals.currentUser.getName() + " " + Globals.currentUser.getSurName());
        ((TextView) view.findViewById(R.id.emailTV)).setText(Globals.currentUser.getEmail());
        ((TextView) view.findViewById(R.id.phoneTV)).setText(Globals.currentUser.getPhone());
        ((TextView) view.findViewById(R.id.cityTV)).setText(Globals.currentUser.getCity());
        ((TextView) view.findViewById(R.id.streetTV)).setText(Globals.currentUser.getStreet());
        ((TextView) view.findViewById(R.id.streetNumTV)).setText(Globals.currentUser.getStreetNum());
    }
}
