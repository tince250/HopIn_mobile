package com.example.uberapp_tim13.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import com.example.uberapp_tim13.dtos.InvitationResponseDTO;
import com.example.uberapp_tim13.dtos.RideInInviteDTO;
import com.example.uberapp_tim13.dtos.RideInviteDTO;
import com.example.uberapp_tim13.dtos.UserDTO;
import com.example.uberapp_tim13.dtos.UserInRideDTO;
import com.example.uberapp_tim13.services.RideService;
import com.example.uberapp_tim13.services.UserService;
import com.example.uberapp_tim13.tools.FragmentTransition;
import com.example.uberapp_tim13.tools.Globals;
import com.example.uberapp_tim13.tools.Mockap;
import com.example.uberapp_tim13.tools.StompManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

public class InviteOthersFragment extends Fragment implements View.OnClickListener {
    private boolean listeningInvites = false;

    public static InviteOthersFragment newInstance() {
        return new InviteOthersFragment();
    }

    List<UserDTO> addedUsers;
    List<Boolean> accepted;
    ImageView inviteBtn;
    InvitedPassengersAdapter adapter;
    private ListView listView;
    TextView emailTV;
    UserDTO user;
    private StompClient mStompClient;
    private UserDTO loggedUser;
    Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_invite_others, container, false);

        getActivity().setTitle("Other passengers");

        this.addedUsers = new ArrayList<UserDTO>();
        this.accepted = new ArrayList<Boolean>();
        this.adapter = new InvitedPassengersAdapter(getActivity(), this.addedUsers, this.accepted);
        listView = (ListView) view.findViewById(R.id.list);
        listView.setAdapter(adapter);

        inviteBtn = (ImageView) view.findViewById(R.id.inviteBtn);
        inviteBtn.setOnClickListener(this);

        this.emailTV = (TextView) view.findViewById(R.id.emailET);

        this.mStompClient = StompManager.stompClient;

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
                FragmentTransition.to(RideLoadingFragment.newInstance(), getActivity(), false, R.id.passengerFL);
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
        RideService.rideInCreation.getPassengers().add(new UserInRideDTO(Globals.user.getId(), Globals.user.getEmail()));
        for (int i = 0; i < addedUsers.size(); i++) {
            if (accepted.get(i) != null)
                if (accepted.get(i))
                    RideService.rideInCreation.getPassengers().add(new UserInRideDTO(addedUsers.get(i).getId(), addedUsers.get(i).getEmail()));
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
                        //TODO: dodati karticu za korisnika

                        // sending invite
                        Gson gson = new Gson();
                        RideInviteDTO invite = new RideInviteDTO(Globals.user, new RideInInviteDTO(RideService.rideInCreation));
                        mStompClient.send("/ws/send/invite/" + user.getId(), gson.toJson(invite)).subscribe();
                        Log.d("EVO", user.getId() + "");
                        // implementing reaction to invite response
                        Toast.makeText(getActivity(),"Waiting for answer!",Toast.LENGTH_SHORT).show();
                        UserDTO userCopy = user;
                        accepted.add(null);
                        addedUsers.add(userCopy);
                        adapter.notifyDataSetChanged();
                        int index = accepted.size()-1;
                        Log.d("user", userCopy.getName());
                        if (!listeningInvites) {
                            mStompClient.topic("/topic/invite-response/" + Globals.userId).subscribe(topicMessage -> {
                                ///TODO: promeniti ikonicu u korisnickoj kartici (true - stiklica, false - iksic)
                                //TODO: true - dodas u listu addeddUsers, ako je false izbaci iz liste
                                Log.d("JUHU", topicMessage.getPayload());
                                handler.post(new Runnable() {

                                    @Override
                                    public void run() {
                                        InvitationResponseDTO response = Globals.gson.fromJson(topicMessage.getPayload(), InvitationResponseDTO.class);
                                        if (response.isResponse()){
                                            //todo nacrtaj
                                            accepted.set(index, true);
                                        } else {
                                            //nacrtaj
                                            accepted.set(index, false);
                                        }
                                        Log.d("user", userCopy.getName());
                                        adapter.notifyDataSetChanged();
                                    }
                                });
                            });
                            listeningInvites = true;
                        }
                        
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
