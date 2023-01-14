package com.example.uberapp_tim13.rest;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestUtils {
    //EXAMPLE: http://192.168.43.73:8080/rs.ftn.reviewer.rest/rest/proizvodi/
    public static final String SERVICE_API_PATH = "http://192.168.1.102:4321/api/";
    public static final String USER_GET = "user/1";
    public static final String LOGIN = "user/login";
    public static final String RIDE_GET_ID = "ride/3";

    /*
     * Ovo ce nam sluziti za debug, da vidimo da li zahtevi i odgovoru idu
     * odnosno dolaze i kako izgeldaju.
     * */
    public static OkHttpClient test(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(interceptor).build();

        return client;
    }

    /*
     * Prvo je potrebno da definisemo retrofit instancu preko koje ce komunikacija ici
     * */
    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(SERVICE_API_PATH)
            .addConverterFactory(GsonConverterFactory.create())
            .client(test())
            .build();

    /*
     * Definisemo konkretnu instancu servisa na intnerntu sa kojim
     * vrsimo komunikaciju
     * */
//    public static ReviewerService reviewerService = retrofit.create(ReviewerService.class);
    public static UserAPI userApi = retrofit.create(UserAPI.class);
    public static RideAPI rideAPI = retrofit.create(RideAPI.class);
    public static DriverAPI driverAPI = retrofit.create(DriverAPI.class);
    public static PassengerAPI passengerAPI = retrofit.create(PassengerAPI.class);
    public static ReviewAPI reviewAPI = retrofit.create(ReviewAPI.class);
}
