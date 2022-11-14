package com.example.uberapp_tim13.tools;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.items.InboxItem;
import com.example.uberapp_tim13.items.MessageItem;
import com.example.uberapp_tim13.items.User;

import java.util.ArrayList;
import java.util.List;

public class Mockap {
    public static List<MessageItem> messages;
    public static List<InboxItem> getInboxItems() {
        List<InboxItem> items = new ArrayList<InboxItem>();
        items.add(new InboxItem("HopIn Support", "Your message is received and pending.", "today at 13:40", R.drawable.support, "support"));
        items.add(new InboxItem("Ride notification", "Passenger ready for pickup.", "yesterday at 7:30", R.drawable.car, "ride"));
        items.add(new InboxItem("Panic", "Panic crew on their way.", "11.11.2022. 7:30", R.drawable.panic, "panic"));

        return items;
    }

    public static List<User> getUsers(){
        List<User> users = new ArrayList<User>();
        users.add(new User(1, "Mika", "Mikic", "668979414", "mika@gmail.com", "123", "Bulevar Oslobodjenja 1", "Novi Sad", "passenger", "1" ));
        users.add(new User(1, "Zika", "Zikic", "668979414", "zika@gmail.com", "123", "Bulevar Oslobodjenja 1", "Novi Sad", "driver", "1"));

        return users;
    }

    public static List<MessageItem> getMessages(){
        User user = (new User(1, "Mika", "Mikic", "668979414", "mika@gmail.com", "123", "Bulevar Oslobodjenja 1", "Novi Sad", "passenger", "1" ));
        User admin = new User();
        messages.add(new MessageItem("dobar dan", user, "11:59"));
        messages.add(new MessageItem("dobar dan1", user, "12:59"));
        messages.add(new MessageItem("dobar dan2", admin, "13:59"));
        messages.add(new MessageItem("dobar dan3", user, "14:59"));
        messages.add(new MessageItem("dobar dan4", admin, "15:59"));
        messages.add(new MessageItem("dobar dan5", user, "16:59"));
        return messages;
    }

}
