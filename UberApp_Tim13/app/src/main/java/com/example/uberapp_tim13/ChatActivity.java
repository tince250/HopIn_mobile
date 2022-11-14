package com.example.uberapp_tim13;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView mMessageRecycler;
    //private MessageListAdapter mMessageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mMessageRecycler = (RecyclerView) findViewById(R.id.recyclerViewChat);
        mMessageAdapter = new MessageListAdapter(this, messageList);
        mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));
        mMessageRecycler.setAdapter(mMessageAdapter);
    }
}
