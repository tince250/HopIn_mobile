package com.example.uberapp_tim13.tools;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.items.InboxItem;

import java.util.ArrayList;
import java.util.List;

public class Mockap {
    public static List<InboxItem> getInboxItems() {
        List<InboxItem> items = new ArrayList<InboxItem>();
        items.add(new InboxItem("HopIn Support", "Your message is received and pending.", "today at 13:40", R.drawable.support, "support"));
        items.add(new InboxItem("Ride notification", "Passenger ready for pickup.", "yesterday at 7:30", R.drawable.car, "ride"));
        items.add(new InboxItem("Panic", "Panic crew on their way.", "11.11.2022. 7:30", R.drawable.panic, "panic"));

        return items;
    }
}
