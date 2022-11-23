package com.example.uberapp_tim13.tools;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.model.CreditCard;
import com.example.uberapp_tim13.model.Inbox;
import com.example.uberapp_tim13.model.Rating;
import com.example.uberapp_tim13.model.Ride;
import com.example.uberapp_tim13.model.Message;
import com.example.uberapp_tim13.model.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Mockap {
    public static List<Inbox> items;
    public static List<User> users;
    public static List<Ride> rides;
    public static List<Message> messages;


    public static List<Inbox> getInboxItems() {
        if (items == null) {
            items = new ArrayList<Inbox>();
            items.add(new Inbox("HopIn Support", "Your message is received and pending.", "today at 13:40", R.drawable.support, "support"));
            items.add(new Inbox("Ride notification", "Passenger ready for pickup.", "yesterday at 7:30", R.drawable.car, "ride"));
            items.add(new Inbox("Panic", "Panic crew on their way.", "11.11.2022. 7:30", R.drawable.panic, "panic"));
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

    public static List<Ride> getRides(){
        if (rides == null){
            rides = new ArrayList<Ride>();

            List<User> firstRidePassengers = new ArrayList<User>();
            firstRidePassengers.add(0, users.get(0));
            List<String> firstRideStops = new ArrayList<String>();
            firstRideStops.add("Stara Pazova, Kralja Milana 5");
            List<Rating> firstRideRatings = new ArrayList<Rating>();
            firstRideRatings.add(new Rating("Very nice ride", 4, users.get(0)));

            rides.add(new Ride(users.get(1), firstRidePassengers, "15.11.2022. 11:30", "15.11.2022. 12:30", "Novi Sad, Kralja Petra 1 ", firstRideStops, "Belgrade, Knez Mihajlova 10", 95, 1200, firstRideRatings));
            rides.add(new Ride(users.get(1), firstRidePassengers, "15.11.2022. 11:30", "15.11.2022. 12:30", "Sabac, Kralja Mile 3 ", firstRideStops, "Belgrade, Mihajlova 12", 123, 1250, firstRideRatings));
//            rides.add(new Ride(1, "Zika", "Zikic", "668979414", "zika@gmail.com", "123", "Bulevar Oslobodjenja 1", "Novi Sad", "driver", "1"));
        }
        return rides;
    }


    public static List<Message> getMessages(){
        if (messages == null) {
        
          User user = users.get(0);
          User admin = new User();
          messages = new ArrayList<Message>();
          messages.add(new Message("Hello", user, "11:59, 11/11"));
          messages.add(new Message("Hello", admin, "11:59, 11/11"));
          messages.add(new Message("I need help", user, "12:00, 11/11"));
          messages.add(new Message("My vehicle is not here", user, "12:01, 11/11"));
          messages.add(new Message("Tell me your adress", admin, "12:02, 11/11"));
          messages.add(new Message("We will see what we can do", admin, "12:02, 11/11"));
          messages.add(new Message("Thank you", user, "12:03, 11/11"));
          messages.add(new Message("Bye", user, "12:04, 11/11"));
          messages.add(new Message("Bye", admin, "12:04, 11/11"));

          Collections.reverse(messages);
        }
        return messages;
    }


}
