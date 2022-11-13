package com.example.uberapp_tim13.items;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uberapp_tim13.R;

import java.util.List;

public class InboxAdapter extends RecyclerView.Adapter<InboxItemHolder> {

    Context context;
    List<InboxItem> items;

    public InboxAdapter(Context context, List<InboxItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public InboxItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InboxItemHolder(LayoutInflater.from(context).inflate(R.layout.inbox_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull InboxItemHolder holder, int position) {
        InboxItem item = items.get(position);
        holder.name.setText(item.getName());
        holder.displayMess.setText(item.getDisplayMessage());
        holder.dateTime.setText(item.getDateTime());
        holder.userImage.setImageResource(item.getImage());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
