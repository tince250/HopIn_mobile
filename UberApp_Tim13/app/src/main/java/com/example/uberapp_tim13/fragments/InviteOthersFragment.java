package com.example.uberapp_tim13.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.adapters.invited_passengers.InvitedPassengersAdapter;
import com.example.uberapp_tim13.model.User;
import com.example.uberapp_tim13.tools.Mockap;

import java.util.ArrayList;
import java.util.List;

public class InviteOthersFragment extends Fragment implements View.OnClickListener {
    public static InviteOthersFragment newInstance() {
        return new InviteOthersFragment();
    }

    List<User> addedUsers;
    ImageView inviteBtn;
    InvitedPassengersAdapter adapter;
    private ListView listView;
    TextView emailTV;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_invite_others, container, false);

        this.addedUsers = new ArrayList<User>();
        this.adapter = new InvitedPassengersAdapter(getActivity(), this.addedUsers);
        listView = (ListView) view.findViewById(R.id.list);
        listView.setAdapter(adapter);

        inviteBtn = (ImageView) view.findViewById(R.id.inviteBtn);
        inviteBtn.setOnClickListener(this);

        this.emailTV = (TextView) view.findViewById(R.id.emailET);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.inviteBtn:
                String email = this.emailTV.getText().toString();
                User user = null;
                for (User u : Mockap.getUsers()) {
                    if (u.getEmail().equals(email)) {
                        user = u;
                        break;
                    }
                }

                if (user != null) {
                    addedUsers.add(user);
                    adapter.notifyDataSetChanged();
                }

        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

}
