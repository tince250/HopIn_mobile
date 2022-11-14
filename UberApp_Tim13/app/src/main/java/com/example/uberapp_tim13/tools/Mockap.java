package com.example.uberapp_tim13.tools;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.items.CreditCard;
import com.example.uberapp_tim13.items.InboxItem;
import com.example.uberapp_tim13.items.MessageItem;
import com.example.uberapp_tim13.items.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import kotlin.collections.UCollectionsKt;

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
        users.add(new User(1, "Mika", "Mikic", "668979414", "mika@gmail.com",
                "123", "Bulevar Oslobodjenja 1", "Novi Sad", "passenger",
                "1", new CreditCard("MasterCard", "111 1111 1111 123", "111", "03", "2022")));
        users.add(new User(1, "Zika", "Zikic", "668979414", "zika@gmail.com",
                "123", "Bulevar Oslobodjenja 1", "Novi Sad", "driver",
                "1", new CreditCard("DinaCard", "111 1111 1111 321", "222", "10", "2020")));

        return users;
    }

    public static List<MessageItem> getMessages(){
        User user = (new User(1, "Mika", "Mikic", "668979414", "mika@gmail.com", "123", "Bulevar Oslobodjenja 1", "Novi Sad", "passenger", "1" ));
        User admin = new User();
        messages = new ArrayList<MessageItem>();
        messages.add(new MessageItem("Hello", user, "11:59, 11/11"));
        messages.add(new MessageItem("Hello", admin, "11:59, 11/11"));
        messages.add(new MessageItem("I need help", user, "12:00, 11/11"));
        messages.add(new MessageItem("My vehicle is not here", user, "12:01, 11/11"));
        messages.add(new MessageItem("Tell me your adress", admin, "12:02, 11/11"));
        messages.add(new MessageItem("We will see what we can do", admin, "12:02, 11/11"));
        messages.add(new MessageItem("Thank you", user, "12:03, 11/11"));
        messages.add(new MessageItem("Bye", user, "12:04, 11/11"));
        messages.add(new MessageItem("Bye", admin, "12:04, 11/11"));

        Collections.reverse(messages);
        return messages;
    }

}
