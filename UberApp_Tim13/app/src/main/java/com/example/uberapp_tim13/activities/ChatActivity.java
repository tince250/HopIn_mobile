package com.example.uberapp_tim13.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.adapters.ChatAdapter;
import com.example.uberapp_tim13.dtos.AllMessagesDTO;
import com.example.uberapp_tim13.dtos.MessageDTO;
import com.example.uberapp_tim13.dtos.MessageReturnedDTO;
import com.example.uberapp_tim13.model.Message;
import com.example.uberapp_tim13.services.UserService;
import com.google.android.material.button.MaterialButton;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView chatRecycler;
    private ChatAdapter chatAdapter;
    private EditText messageET;
    private int receiverId;
    private int rideId;
    private List<Message> allMessages;
    Timer t ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Chat");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        receiverId = (int) savedInstanceState.get("driverId");
        rideId = (int) savedInstanceState.get("rideId");

        allMessages = new ArrayList<Message>();
        chatRecycler = (RecyclerView) findViewById(R.id.chatRV);
        chatAdapter = new ChatAdapter(this, this.allMessages);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        layoutManager.setReverseLayout(true);
        chatRecycler.setLayoutManager(layoutManager);
        chatRecycler.setAdapter(chatAdapter);

        setBroadcastLoadMessages();
        t = new Timer();
        t.schedule(new TimerTask() {

            public void run() {
                Intent intentUserService = new Intent(getBaseContext(), UserService.class);
                intentUserService.putExtra("method", "getMessages");
                getParent().startService(intentUserService);
            }
        }, 3000);

        messageET = findViewById(R.id.enterMessageET);
        MaterialButton sendBtn = findViewById(R.id.chatSendBtn);
        sendBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (messageET.getText().toString().equals("")) {
                    Toast.makeText(getParent(),"Enter message!",Toast.LENGTH_SHORT).show();
                    return;
                }
                allMessages.add(new Message(messageET.getText().toString(), 1, LocalDateTime.of(LocalDate.now(), LocalTime.now()).toString()));
                MessageDTO message = new MessageDTO(receiverId, messageET.getText().toString(), "RIDE", rideId);
                Intent intentUserService = new Intent(getBaseContext(), UserService.class);
                intentUserService.putExtra("method", "sendMessage");
                intentUserService.putExtra("message", message);
                getParent().startService(intentUserService);
            }
        });
    }

    private void setBroadcastLoadMessages() {
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle extras = intent.getExtras();
                AllMessagesDTO dto = (AllMessagesDTO) extras.get("messages");
                allMessages.clear();
                for(MessageReturnedDTO m : dto.getResults()) {
                    if (m.getSenderId() == receiverId) {
                        allMessages.add(new Message(m.getMessage(), 0, m.getTimeOfSending()));
                    }
                    else if (m.getReceiverId() == receiverId) {
                        allMessages.add(new Message(m.getMessage(), 1, m.getTimeOfSending()));
                    }
                }
                chatAdapter.notifyDataSetChanged();
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("chatActivity"));

    }
}
