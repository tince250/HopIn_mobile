package com.example.uberapp_tim13.items;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<InboxItemHolder> {
    private Context context;
    private List<MessageItem> messages;

    public ChatAdapter(Context context, List<MessageItem> messages) {
        this.context = context;
        this.messages = messages;
    }

    @NonNull
    @Override
    public InboxItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull InboxItemHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}