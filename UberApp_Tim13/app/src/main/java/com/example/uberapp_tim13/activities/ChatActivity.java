package com.example.uberapp_tim13.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
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
import java.util.Collections;
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
        Bundle extras = getIntent().getExtras();
        receiverId = (int) extras.get("receiverId");
        rideId = (int) extras.get("rideId");

        allMessages = new ArrayList<Message>();
        chatRecycler = (RecyclerView) findViewById(R.id.chatRV);
        chatAdapter = new ChatAdapter(this, this.allMessages);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        layoutManager.setReverseLayout(true);
        chatRecycler.setLayoutManager(layoutManager);
        chatRecycler.setAdapter(chatAdapter);

        setBroadcastLoadMessages();
        getMessages();

        messageET = findViewById(R.id.enterMessageET);
        MaterialButton sendBtn = findViewById(R.id.chatSendBtn);
        sendBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (messageET.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(),"Enter message!",Toast.LENGTH_SHORT).show();
                    return;
                }
                allMessages.add(0, new Message(messageET.getText().toString(), 1, LocalDateTime.of(LocalDate.now(), LocalTime.now()).toString()));
                chatAdapter.notifyDataSetChanged();
                MessageDTO message = new MessageDTO(receiverId, messageET.getText().toString(), "RIDE", rideId);
                Intent intentUserService = new Intent(getApplicationContext(), UserService.class);
                intentUserService.putExtra("method", "sendMessage");
                intentUserService.putExtra("message", message);
                startService(intentUserService);
            }
        });
    }

    private void getMessages() {
        Intent intentUserService = new Intent(getApplicationContext(), UserService.class);
        intentUserService.putExtra("method", "getMessages");
        startService(intentUserService);
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
                Collections.reverse(allMessages);
                chatAdapter.notifyDataSetChanged();
                t = new Timer();
                t.schedule(new TimerTask() {

                    public void run() {
                        getMessages();
                    }
                }, 10000);
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("chatActivity"));

    }

//    private void setUpTimer(){
//        Intent intentUserService = new Intent(this, UserService.class);
//        intentUserService.putExtra("method", "getMessages");
//        pendingIntent = PendingIntent.getService(this, 0, intentUserService, PendingIntent.FLAG_IMMUTABLE);
//
//        //koristicemo sistemski AlarmManager pa je potrebno da dobijemo
//        //njegovu instancu.
//        manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
//
//        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 3000, pendingIntent);
//
//    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        if (manager == null) {
//            setUpTimer();
//        }
//
//        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 3000, pendingIntent);
//    }
}
