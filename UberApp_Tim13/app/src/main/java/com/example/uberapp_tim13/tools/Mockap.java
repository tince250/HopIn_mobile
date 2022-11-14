package com.example.uberapp_tim13.tools;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.items.InboxItem;
import com.example.uberapp_tim13.items.RatingItem;
import com.example.uberapp_tim13.items.RideItem;
import com.example.uberapp_tim13.items.User;

import java.util.ArrayList;
import java.util.List;

public class Mockap {
    public static List<InboxItem> items;
    public static List<User> users;
    public static List<RideItem> rides;

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
        if (users == null){
            users = new ArrayList<User>();
            users.add(new User(1, "Mika", "Mikic", "668979414", "mika@gmail.com", "123", "Bulevar Oslobodjenja 1", "Novi Sad", "passenger", "1" ));
            users.add(new User(2, "Zika", "Zikic", "668979414", "zika@gmail.com", "123", "Bulevar Oslobodjenja 2", "Novi Sad", "driver", "2"));
            users.add(new User(1, "Pika", "Pika", "668979414", "pika@gmail.com", "123", "Bulevar Oslobodjenja 3", "Novi Sad", "passenger", "3" ));
        }
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
            //rides.add(new Ride(1, "Zika", "Zikic", "668979414", "zika@gmail.com", "123", "Bulevar Oslobodjenja 1", "Novi Sad", "driver", "1"));
        }
        return rides;
    }
}
