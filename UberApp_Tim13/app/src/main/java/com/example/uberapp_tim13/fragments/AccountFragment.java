package com.example.uberapp_tim13.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.uberapp_tim13.activities.AccountSettingsActivity;
import com.example.uberapp_tim13.activities.CurrentRideActivity;
import com.example.uberapp_tim13.activities.DriverReportsActivity;
import com.example.uberapp_tim13.activities.DriverStatisticsActivity;
import com.example.uberapp_tim13.activities.FavoriteRoutesActivity;
import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.activities.LoginActivity;
import com.example.uberapp_tim13.activities.PassengerReportsActivity;
import com.example.uberapp_tim13.dialogs.LogOutDialog;
import com.example.uberapp_tim13.dtos.VehicleDTO;
import com.example.uberapp_tim13.services.DriverService;
import com.example.uberapp_tim13.services.RideService;
import com.example.uberapp_tim13.services.UserService;
import com.example.uberapp_tim13.tools.FragmentTransition;
import com.example.uberapp_tim13.tools.Globals;
import com.example.uberapp_tim13.tools.StompManager;
import com.example.uberapp_tim13.tools.Utils;
import com.google.android.material.button.MaterialButton;

public class AccountFragment extends Fragment {

    private View currView;

    public static AccountFragment newInstance() {
        return new AccountFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        currView = view;
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
                LogOutDialog logOutDialog = new LogOutDialog(getActivity());
                logOutDialog.show();
            }
        });

        view.findViewById(R.id.statisticsBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (Globals.userRole) {
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
                startActivity(new Intent(getActivity(), PassengerReportsActivity.class));
            }
        });

        return view;
    }

    private void fitFragmentToRole(View view) {
        MaterialButton button = view.findViewById(R.id.statisticsBtn);
        switch (Globals.userRole) {
            case "driver":
                Intent intentDriverService = new Intent(getContext(), DriverService.class);
                intentDriverService.putExtra("method", "getVehicle");
                intentDriverService.putExtra("driverId", Globals.user.getId());
                requireActivity().startService(intentDriverService);
                setBroadcastGetVehicle();

                button.setIcon(ContextCompat.getDrawable(this.getContext(), R.drawable.statistics));
                button.setText("statistics");
                break;
            case "passenger":
                view.findViewById(R.id.vehicle_info_card).setVisibility(View.GONE);
                button.setIcon(ContextCompat.getDrawable(this.getContext(), R.drawable.heart_solid));
                button.setIconTint(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                button.setText("routes");
                break;
        }
        fitTextViewsToUser();
    }

    private void fitTextViewsToUser() {
        ((TextView) currView.findViewById(R.id.userRoleTV)).setText(Globals.userRole);
        ((TextView) currView.findViewById(R.id.driverNameTitleTV)).setText(Globals.user.getName() + " " + Globals.user.getSurname());
        ((TextView) currView.findViewById(R.id.emailTV)).setText(Globals.user.getEmail());
        ((TextView) currView.findViewById(R.id.phoneTV)).setText(Globals.user.getTelephoneNumber());
        ((TextView) currView.findViewById(R.id.cityTV)).setText(Globals.user.getAddress());
        if (Globals.user.getProfilePicture() != null) {
            ((ImageView) currView.findViewById(R.id.driverAvatarImg)).setImageBitmap(Utils.convertBase64ToBitmap(Globals.user.getProfilePicture()));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        fitTextViewsToUser();
    }

    private void setBroadcastGetVehicle() {
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle extras = intent.getExtras();
                VehicleDTO vehicle = (VehicleDTO) extras.get("vehicle");
                ((TextView) currView.findViewById(R.id.vehicleModelTV)).setText(vehicle.getModel());
                ((TextView) currView.findViewById(R.id.regPlatesTV)).setText(vehicle.getLicenseNumber());
            }
        };
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver, new IntentFilter("userDetailsDialog"));

    }
}
