package com.example.uberapp_tim13.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

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
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class InboxFragment extends Fragment {

    RecyclerView recyclerView;
    List<InboxReturnedDTO> inboxes = new ArrayList<>();
    List<InboxReturnedDTO> inboxesToShow = new ArrayList<>();
    InboxAdapter adapter;
    private NewChatDialog newChatDialog;

    public static String type;
    Spinner inboxSpinner;

    private EditText searchET;

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

        searchET = view.findViewById(R.id.searchET);
        searchET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                String searched = textView.getText().toString().trim();
                if(searched.equals("")) {
                    inboxesToShow = new ArrayList<>(inboxes);
                    adapter = new InboxAdapter(view.getContext(), inboxesToShow);
                    recyclerView.setAdapter(adapter);
                    return false;
                }

                inboxesToShow.clear();
                for (InboxReturnedDTO inbox : inboxes) {
                    String name;

                    if (inbox.getFirstUser().getId() == Globals.user.getId()) {
                        name = (inbox.getSecondUser().getName() + " " + inbox.getSecondUser().getSurname()).toLowerCase();
                    } else {
                        name = (inbox.getFirstUser().getName() + " " + inbox.getFirstUser().getSurname()).toLowerCase();
                    }
                    
                    if (!type.equals("all")) {
                        if (name.contains(searched) && inbox.getType().toLowerCase().equals(type)) {
                            inboxesToShow.add(inbox);
                        }
                    } else {
                        if (name.contains(searched)) {
                            inboxesToShow.add(inbox);
                        }
                    }
                }
                adapter.notifyDataSetChanged();

                return false;
            }
        });

        setSpinner(view);

        newChatDialog = new NewChatDialog(getActivity());

        return view;
    }

    private void setSpinner(View view) {
        inboxSpinner = view.findViewById(R.id.spinnerInbox);
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(view.getContext(),
                android.R.layout.simple_spinner_item, view.getResources().getStringArray(R.array.inbox_filter));
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inboxSpinner.setAdapter(adapterSpinner);

        inboxSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                type = (String) inboxSpinner.getSelectedItem();
                Log.d("tipm", String.valueOf(inboxes.size()));
                if (type.equals("all")) {
                    if (inboxes.size() > 0) {
                        inboxesToShow = new ArrayList<>(inboxes);
                        adapter = new InboxAdapter(view.getContext(), inboxesToShow);
                        recyclerView.setAdapter(adapter);
                    }
                    return;
                }
                inboxesToShow.clear();
                for (InboxReturnedDTO inbox : inboxes) {
                    //Log.d("tipm", inbox.getType());
                    if (inbox.getType().toLowerCase().equals(type)) {
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
                    inboxesToShow = new ArrayList<>(inboxes);
                    adapter = new InboxAdapter(view.getContext(), inboxesToShow);
                    recyclerView.setAdapter(adapter);
                    inboxSpinner.setSelection(0);
                }
            }

            @Override
            public void onFailure(Call<List<InboxReturnedDTO>> call, Throwable t) {

            }
        });
    }
}