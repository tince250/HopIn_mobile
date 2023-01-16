package com.example.uberapp_tim13.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.adapters.invited_passengers.InvitedPassengersAdapter;
import com.example.uberapp_tim13.dtos.UserDTO;
import com.example.uberapp_tim13.dtos.UserInRideDTO;
import com.example.uberapp_tim13.services.RideService;
import com.example.uberapp_tim13.services.UserService;
import com.example.uberapp_tim13.tools.FragmentTransition;

import java.util.ArrayList;
import java.util.List;

public class InviteOthersFragment extends Fragment implements View.OnClickListener {
    public static InviteOthersFragment newInstance() {
        return new InviteOthersFragment();
    }

    List<UserDTO> addedUsers;
    ImageView inviteBtn;
    InvitedPassengersAdapter adapter;
    private ListView listView;
    TextView emailTV;
    UserDTO user;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_invite_others, container, false);

        getActivity().setTitle("Other passengers");

        this.addedUsers = new ArrayList<UserDTO>();
        this.adapter = new InvitedPassengersAdapter(getActivity(), this.addedUsers);
        listView = (ListView) view.findViewById(R.id.list);
        listView.setAdapter(adapter);

        inviteBtn = (ImageView) view.findViewById(R.id.inviteBtn);
        inviteBtn.setOnClickListener(this);

        this.emailTV = (TextView) view.findViewById(R.id.emailET);

        setBroadcast();
        view.findViewById(R.id.finishBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: call order ride from rideService
                setDataInRide();
                addedUsers.clear();
                adapter.notifyDataSetChanged();
                Intent intentRideService = new Intent(getContext(), RideService.class);
                intentRideService.putExtra("method", "orderRide");
                requireActivity().startService(intentRideService);
                FragmentTransition.to(PassengerHomeFragment.newInstance(), getActivity(), true, R.id.passengerFL);
            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.inviteBtn:
                user = null;
                String email = this.emailTV.getText().toString();
                Intent intentUserService = new Intent(getContext(), UserService.class);
                intentUserService.putExtra("method", "getByEmail");
                intentUserService.putExtra("email", email);
                requireActivity().startService(intentUserService);
        }
    }

    private void setDataInRide() {
        for (UserDTO u : addedUsers) {
            RideService.rideInCreation.getPassengers().add(new UserInRideDTO(u.getId(), u.getEmail()));
        }
    }

    private void setBroadcast() {
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle extras = intent.getExtras();
                if (user == null) {
                    Log.d("REC", extras.get("userByEmail").toString());

                    user = (UserDTO) extras.get("userByEmail");
                    String email = user.getEmail();
                    if (user == null) {
                        Toast.makeText(getActivity(),"User does not exist!",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    for (UserDTO u : addedUsers) {
                        if (u.getEmail().equals(email)) {
                            user = null;
                            Toast.makeText(getActivity(),"User already added!",Toast.LENGTH_SHORT).show();
                        }
                    }

                    if (user != null) {
                        addedUsers.add(user);
                        adapter.notifyDataSetChanged();
                    }
                    user = null;
                }
            }
        };
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver, new IntentFilter("inviteOthersFragment"));

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
