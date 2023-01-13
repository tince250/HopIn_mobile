package com.example.uberapp_tim13.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.uberapp_tim13.dtos.UserDTO;
import com.example.uberapp_tim13.rest.RestUtils;
import com.example.uberapp_tim13.rest.UserAPI;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserService extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

//        Intent ints = new Intent(MainActivity.SYNC_DATA);
//        int status = ReviewerTools.getConnectivityStatus(getApplicationContext());
//        ints.putExtra(RESULT_CODE, status);

        //ima konekcije ka netu skini sta je potrebno i sinhronizuj bazu
//        if(status == ReviewerTools.TYPE_WIFI || status == ReviewerTools.TYPE_MOBILE){
//            TagToSend tts = new TagToSend();
//            tts.setName("Test tag");
//            tts.setDateModified("2017-05-09");

        /*
         * Poziv REST servisa se odvija u pozadini i mi ne moramo da vodimo racuna o tome
         * Samo je potrebno da registrujemo sta da se desi kada odgovor stigne od nas
         * Taj deo treba da implementiramo dodavajuci Callback<List<Event>> unutar enqueue metode
         *
         * Servis koji pozivamo izgleda:
         * http://<service_ip_adress>:<service_port>/rs.ftn.reviewer.rest/rest/proizvodi/
         * */
        Call<UserDTO> call = RestUtils.userApi.doGetUser();
        call.enqueue(new Callback<UserDTO>() {
            @Override
            public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                if (response.code() == 200) {
                    Log.d("REZ", "Meesage recieved");
                } else {
                    Log.d("REZ", "Meesage recieved: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<UserDTO> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null ? t.getMessage() : "error");
            }
        });

//        sendBroadcast(ints);

        stopSelf();

        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
}
