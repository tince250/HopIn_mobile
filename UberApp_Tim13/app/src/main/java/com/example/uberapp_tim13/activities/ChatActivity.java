package com.example.uberapp_tim13.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.adapters.ChatAdapter;
import com.example.uberapp_tim13.tools.Mockap;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView chatRecycler;
    private ChatAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Chat");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatRecycler = (RecyclerView) findViewById(R.id.chatRV);
        chatAdapter = new ChatAdapter(this, Mockap.getMessages());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        layoutManager.setReverseLayout(true);
        chatRecycler.setLayoutManager(layoutManager);
        chatRecycler.setAdapter(chatAdapter);
    }
}
