package com.example.uberapp_tim13.adapters.inbox;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.dtos.InboxReturnedDTO;
import com.example.uberapp_tim13.dtos.MessageReturnedDTO;
import com.example.uberapp_tim13.dtos.UserReturnedDTO;
import com.example.uberapp_tim13.model.Inbox;
import com.example.uberapp_tim13.tools.Globals;
import com.example.uberapp_tim13.tools.Utils;

import java.util.List;

public class InboxAdapter extends RecyclerView.Adapter<InboxItemHolder> {

    Context context;
    List<InboxReturnedDTO> items;

    public InboxAdapter(Context context, List<InboxReturnedDTO> items) {
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
        InboxReturnedDTO item = items.get(position);
        MessageReturnedDTO lastMessage = item.getMessages().get(0);
        UserReturnedDTO recipient = item.getFirstUser().getId() == Globals.user.getId() ? item.getSecondUser() : item.getFirstUser();

        holder.name.setText(recipient.getName() + " " + recipient.getSurname());
        holder.displayMess.setText(lastMessage.getMessage());
        holder.dateTime.setText(formatDate(lastMessage.getTimeOfSending()));
        if (recipient.getProfilePicture() != null)
            holder.userImage.setImageBitmap(Utils.convertBase64ToBitmap(recipient.getProfilePicture()));

        Log.d("INBOKSI", lastMessage.getType().toLowerCase());
        switch (lastMessage.getType().toLowerCase()) {
            case "support":
                holder.coloredMargin.setBackgroundColor(ContextCompat.getColor(context.getApplicationContext(), R.color.orange));
                break;
            case "ride":
                holder.coloredMargin.setBackgroundColor(ContextCompat.getColor(context.getApplicationContext(), R.color.dark_blue));
                break;
            case "panic":
                holder.coloredMargin.setBackgroundColor(ContextCompat.getColor(context.getApplicationContext(), R.color.red));
                break;
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private String formatDate(String dateString) {
        String[] tokens = dateString.split("T");
        String[] date = tokens[0].split("-");
        String[] time = tokens[1].split(":");
        return time[0] + ":" + time[1] + ", " + date[2] + "." + date[1] + "." + date[0] + ".";
    }
}
