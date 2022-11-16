package com.example.uberapp_tim13.tools;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.items.CreditCard;
import com.example.uberapp_tim13.items.InboxItem;
import com.example.uberapp_tim13.items.RatingItem;
import com.example.uberapp_tim13.items.RideItem;
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
    public static List<InboxItem> items;
    public static List<User> users;
    public static List<RideItem> rides;
    public static List<MessageItem> messages;


    public static List<InboxItem> getInboxItems() {
        if (items == null) {
            items = new ArrayList<InboxItem>();
            items.add(new InboxItem("HopIn Support", "Your message is received and pending.", "today at 13:40", R.drawable.support, "support"));
            items.add(new InboxItem("Ride notification", "Passenger ready for pickup.", "yesterday at 7:30", R.drawable.car, "ride"));
            items.add(new InboxItem("Panic", "Panic crew on their way.", "11.11.2022. 7:30", R.drawable.panic, "panic"));
        }
        return items;
    }

    public static List<User> getUsers(){

            users = new ArrayList<User>();
            users.add(new User(1, "Mika", "Mikic", "668979414", "mika@gmail.com",
                    "123", "Bulevar Oslobodjenja 1", "Novi Sad", "passenger",
                    "1", new CreditCard("MasterCard", "111 1111 1111 123", "111", "03", "2022")));
            users.add(new User(1, "Zika", "Zikic", "668979414", "zika@gmail.com",
                    "123", "Bulevar Oslobodjenja 1", "Novi Sad", "driver",
                    "1", new CreditCard("DinaCard", "111 1111 1111 321", "222", "10", "2020")));

            return users;
    }

    public static List<RideItem> getRides(){
        if (rides == null){
            rides = new ArrayList<RideItem>();

            List<User> firstRidePassengers = new ArrayList<User>();
            firstRidePassengers.add(0, users.get(0));
            List<String> firstRideStops = new ArrayList<String>();
            firstRideStops.add("Stara Pazova, Kralja Milana 5");
            List<RatingItem> firstRideRatings = new ArrayList<RatingItem>();
            firstRideRatings.add(new RatingItem("Very nice ride", 4, users.get(0)));

            rides.add(new RideItem(users.get(1), firstRidePassengers, "15.11.2022. 11:30", "15.11.2022. 12:30", "Novi Sad, Kralja Petra 1 ", firstRideStops, "Belgrade, Knez Mihajlova 10", 95, 1200, firstRideRatings));
            rides.add(new RideItem(users.get(1), firstRidePassengers, "15.11.2022. 11:30", "15.11.2022. 12:30", "Sabac, Kralja Mile 3 ", firstRideStops, "Belgrade, Mihajlova 12", 123, 1250, firstRideRatings));
//            rides.add(new Ride(1, "Zika", "Zikic", "668979414", "zika@gmail.com", "123", "Bulevar Oslobodjenja 1", "Novi Sad", "driver", "1"));
        }
        return rides;
    }


    public static List<MessageItem> getMessages(){
        if (messages == null) {
        
          User user = users.get(0);
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
        }
        return messages;
    }


}
