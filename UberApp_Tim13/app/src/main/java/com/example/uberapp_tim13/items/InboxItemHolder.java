package com.example.uberapp_tim13.items;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uberapp_tim13.R;

public class InboxItemHolder extends RecyclerView.ViewHolder {

    ImageView userImage;
    TextView name, displayMess, dateTime;
    View coloredMargin;

    public InboxItemHolder(@NonNull View itemView) {
        super(itemView);
        userImage = itemView.findViewById(R.id.inboxImage);
        name = itemView.findViewById(R.id.name);
        displayMess = itemView.findViewById(R.id.displayMessage);
        dateTime = itemView.findViewById(R.id.dateTime);
        coloredMargin = itemView.findViewById(R.id.colored_margin);
    }
}
