package com.example.uberapp_tim13.tools;

import com.example.uberapp_tim13.dtos.UserReturnedDTO;
import com.example.uberapp_tim13.model.User;
import com.google.gson.Gson;

import ua.naiksoftware.stomp.StompClient;

public class Globals {
    public static String userRole;
    public static int userId = 0;
    public static User currentUser;
    public static boolean isActive = false;

    public static UserReturnedDTO user;

    public static StompClient stompClient;

    public static Gson gson = new Gson();
}
