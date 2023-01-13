package com.example.uberapp_tim13.fragments;

import android.os.Bundle;
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

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.adapters.invited_passengers.InvitedPassengersAdapter;
import com.example.uberapp_tim13.dtos.UserDTO;
import com.example.uberapp_tim13.dtos.UserInRideDTO;
import com.example.uberapp_tim13.model.User;
import com.example.uberapp_tim13.services.RideService;
import com.example.uberapp_tim13.services.UserService;
import com.example.uberapp_tim13.tools.FragmentTransition;
import com.example.uberapp_tim13.tools.Mockap;

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
    private UserService userService;
    private RideService rideService;


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

        this.userService = new UserService();
        this.rideService = new RideService();

        view.findViewById(R.id.finishBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDataInRide();
                rideService.orderRide();
                if (RideService.success) {
                    Toast.makeText(getActivity(),"You successfully ordered a ride.",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(),"Sorry, you can't order right now. Try again!",Toast.LENGTH_SHORT).show();
                }
                FragmentTransition.to(DriverHomeFragment.newInstance(), getActivity(), true, R.id.passengerFL);
            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.inviteBtn:
                String email = this.emailTV.getText().toString();
                userService.getUserByEmail(email);
                UserDTO user = userService.returnedUser;

                if (user == null) {
                    Toast.makeText(getActivity(),"User does not exist!",Toast.LENGTH_SHORT).show();
                    return;
                }

                for (UserDTO u : this.addedUsers) {
                    if (u.getEmail().equals(email)) {
                        user = null;
                        Toast.makeText(getActivity(),"User already added!",Toast.LENGTH_SHORT).show();
                    }
                }

                if (user != null) {
                    addedUsers.add(user);
                    adapter.notifyDataSetChanged();
                }
        }
    }

    private void setDataInRide() {
        for (UserDTO u : addedUsers) {
            RideService.rideInCreation.getPassengers().add(new UserInRideDTO(u.getId(), u.getEmail()));
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

}
