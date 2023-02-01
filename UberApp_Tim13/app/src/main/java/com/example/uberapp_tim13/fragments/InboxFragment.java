package com.example.uberapp_tim13.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.uberapp_tim13.activities.ChatActivity;
import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.adapters.inbox.InboxAdapter;
import com.example.uberapp_tim13.dialogs.NewChatDialog;
import com.example.uberapp_tim13.dtos.InboxReturnedDTO;
import com.example.uberapp_tim13.rest.RestUtils;
import com.example.uberapp_tim13.services.AuthService;
import com.example.uberapp_tim13.tools.Globals;
import com.example.uberapp_tim13.tools.Mockap;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class InboxFragment extends Fragment {

    RecyclerView recyclerView;
    List<InboxReturnedDTO> inboxes = new ArrayList<>();
    List<InboxReturnedDTO> inboxesToShow = new ArrayList<>();
    InboxAdapter adapter;
    private NewChatDialog newChatDialog;

    private String type;

    View view;

    public static InboxFragment newInstance() {
        return new InboxFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_inbox, container, false);

        recyclerView = view.findViewById(R.id.inboxRW);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter = new InboxAdapter(view.getContext(), inboxesToShow);
        recyclerView.setAdapter(adapter);


        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Log.d("INBOKSI", "klik");
                        Intent i = new Intent(getActivity(), ChatActivity.class);
                        i.putExtra("inbox", inboxesToShow.get(position));
                        startActivity(i);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

        view.findViewById(R.id.addInboxFB).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("INBOKSI", "KLIK FLOATIING");
                newChatDialog.show();
            }
        });

        setSpinner(view);

        newChatDialog = new NewChatDialog(getActivity());

        return view;
    }

    private void setSpinner(View view) {
        Spinner inboxSpinner = view.findViewById(R.id.spinnerInbox);
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(view.getContext(),
                android.R.layout.simple_spinner_item, view.getResources().getStringArray(R.array.inbox_filter));
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inboxSpinner.setAdapter(adapterSpinner);

        inboxSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                type = (String) inboxSpinner.getSelectedItem();
                inboxesToShow.clear();
                for (InboxReturnedDTO inbox : inboxes) {
                    if (inbox.getType().equals(type)) {
                        inboxesToShow.add(inbox);
                    }
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        Call<List<InboxReturnedDTO>> call = RestUtils.userApi.getInboxes(AuthService.tokenDTO.getAccessToken(), Globals.user.getId());
        call.enqueue(new Callback<List<InboxReturnedDTO>>() {
            @Override
            public void onResponse(Call<List<InboxReturnedDTO>> call, Response<List<InboxReturnedDTO>> response) {
                if (response.isSuccessful()) {
                    Log.d("INBOKSI", response.body().toString());
                    inboxes = response.body();
                    inboxesToShow = inboxes;
                    adapter = new InboxAdapter(view.getContext(), inboxes);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<InboxReturnedDTO>> call, Throwable t) {

            }
        });
    }
}