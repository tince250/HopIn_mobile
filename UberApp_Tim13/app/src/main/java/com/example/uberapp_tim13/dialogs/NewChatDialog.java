package com.example.uberapp_tim13.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.activities.ChatActivity;
import com.example.uberapp_tim13.dtos.InboxReturnedDTO;
import com.example.uberapp_tim13.dtos.MessageDTO;
import com.example.uberapp_tim13.dtos.MessageReturnedDTO;
import com.example.uberapp_tim13.dtos.UserReturnedDTO;
import com.example.uberapp_tim13.rest.RestUtils;
import com.example.uberapp_tim13.services.AuthService;
import com.example.uberapp_tim13.tools.Globals;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewChatDialog extends Dialog  {

    private Activity activity;
    private EditText email;
    private EditText message;
    private Button send;

    private String type;

    public NewChatDialog(@NonNull Activity activity) {
        super(activity);
        this.activity = activity;

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_new_chat);

        this.email = findViewById(R.id.emailET);
        this.message = findViewById(R.id.messageET);
        this.send = findViewById(R.id.sendBtn);

        this.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
                    email.setError("Not a valid email.");
                    return;
                }
                Call<UserReturnedDTO> call = RestUtils.userApi.getUserByEmail(AuthService.tokenDTO.getAccessToken(), email.getText().toString());
                call.enqueue(new Callback<UserReturnedDTO>() {
                    @Override
                    public void onResponse(Call<UserReturnedDTO> call, Response<UserReturnedDTO> response) {
                        if (response.isSuccessful()) {
                            UserReturnedDTO recipient = response.body();
                            Call<MessageReturnedDTO> callMess = RestUtils.userApi.sendMessage(AuthService.tokenDTO.getAccessToken(), recipient.getId(), new MessageDTO(recipient.getId(), message.getText().toString(), type, 1));
                            callMess.enqueue(new Callback<MessageReturnedDTO>() {
                                @Override
                                public void onResponse(Call<MessageReturnedDTO> call, Response<MessageReturnedDTO> response) {
                                    if (response.isSuccessful()) {
                                        MessageReturnedDTO mess = response.body();
                                        //TODO: otvori chat
                                        Log.d("INBOKSI", "CHAT ID: " + mess.getInboxId());
                                        Call<InboxReturnedDTO> callInbox = RestUtils.userApi.getInbox(AuthService.tokenDTO.getAccessToken() ,mess.getInboxId());
                                        callInbox.enqueue(new Callback<InboxReturnedDTO>() {
                                            @Override
                                            public void onResponse(Call<InboxReturnedDTO> call, Response<InboxReturnedDTO> response) {
                                                if (response.isSuccessful()) {
                                                    Intent i = new Intent(activity, ChatActivity.class);
                                                    i.putExtra("inbox", response.body());
                                                    activity.startActivity(i);
                                                    dismiss();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<InboxReturnedDTO> call, Throwable t) {

                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onFailure(Call<MessageReturnedDTO> call, Throwable t) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<UserReturnedDTO> call, Throwable t) {

                    }
                });


            }
        });

        setSpinner();
    }


    private void setSpinner() {
        Spinner inboxSpinner = findViewById(R.id.spinnerInbox);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,
                android.R.layout.simple_spinner_item, activity.getResources().getStringArray(R.array.message_type));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inboxSpinner.setAdapter(adapter);
        inboxSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                type = (String) inboxSpinner.getSelectedItem();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


}
