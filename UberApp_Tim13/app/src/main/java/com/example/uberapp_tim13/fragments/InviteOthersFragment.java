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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.adapters.invited_passengers.InvitedPassengersAdapter;
import com.example.uberapp_tim13.dtos.RideInInviteDTO;
import com.example.uberapp_tim13.dtos.RideInviteDTO;
import com.example.uberapp_tim13.dtos.UserDTO;
import com.example.uberapp_tim13.dtos.UserInRideDTO;
import com.example.uberapp_tim13.dtos.VehicleDTO;
import com.example.uberapp_tim13.model.User;
import com.example.uberapp_tim13.services.RideService;
import com.example.uberapp_tim13.services.UserService;
import com.example.uberapp_tim13.tools.FragmentTransition;
import com.example.uberapp_tim13.tools.Globals;
import com.example.uberapp_tim13.tools.Mockap;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

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
    private StompClient mStompClient;

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
                setDataInRide();
                addedUsers.clear();
                adapter.notifyDataSetChanged();
//                Intent intentRideService = new Intent(getContext(), RideService.class);
//                intentRideService.putExtra("method", "orderRide");
//                requireActivity().startService(intentRideService);
                FragmentTransition.to(RideLoadingFragment.newInstance(), getActivity(), true, R.id.passengerFL);
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
                    //Log.d("REC", extras.get("userByEmail").toString());

                    user = (UserDTO) extras.get("userByEmail");
                    if (user == null) {
                        Toast.makeText(getActivity(),"User does not exist!",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String email = user.getEmail();
                    for (UserDTO u : addedUsers) {
                        if (u.getEmail().equals(email)) {
                            user = null;
                            Toast.makeText(getActivity(),"User already added!",Toast.LENGTH_SHORT).show();
                        }
                    }

                    if (user != null) {
                        // sending invite
                        Gson gson = new Gson();
                        RideInviteDTO invite = new RideInviteDTO(new UserDTO(), new RideInInviteDTO(RideService.rideInCreation));
                        mStompClient.send("/ws/send/invite/" + user.getId(), gson.toJson(invite)).subscribe();

                        // implementing reaction to invite response
                        Toast.makeText(getActivity(),"Waiting for answer!",Toast.LENGTH_SHORT).show();
                        mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://10.0.2.2:4321/api/socket/websocket");
                        mStompClient.connect();
                        mStompClient.topic("/topic/invite-response/" + Globals.userId).subscribe(topicMessage -> {
                            //TODO: don't add user if answer is negative
                            Log.d("SOCKET", topicMessage.getPayload());
                            addedUsers.add(user);
                            adapter.notifyDataSetChanged();
                        });
//                        mStompClient.disconnect();
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
