package com.example.uberapp_tim13.services;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.example.uberapp_tim13.dtos.AllMessagesDTO;
import com.example.uberapp_tim13.dtos.MessageDTO;
import com.example.uberapp_tim13.dtos.MessageReturnedDTO;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.uberapp_tim13.dtos.UserReturnedDTO;
import com.example.uberapp_tim13.rest.RestUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserService extends Service {

    ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle extras = intent.getExtras();
        String method = (String) extras.get("method");
//        int userId = (int) extras.get("userId");
        String sender = (String) extras.get("sender");
        executor.execute(new Runnable() {
            @Override
            public void run() {
                if (method.equals("getById")){
                    int userId = (int) extras.get("userId");
                    getById(userId, method);
                }
                else if (method.equals("getByEmail")) {
                    String email = (String) extras.get("email");
                    getByEmail(email);
                }
                else if (method.equals("sendMessage")) {
                    MessageDTO message = (MessageDTO) extras.get("message");
                    sendMessage(message);
                }
                else if (method.equals("getMessages")) {
                    int id = (int) extras.get("id");
                    getMessages(id);
                }
//                else if (method.equals("getUserName"))
//                    getById(userId, method);
            }
        });
        stopSelf();
        return START_NOT_STICKY;
    }

    private void getById(int userId, String method){
        Call<UserReturnedDTO> call = RestUtils.userApi.doGetUser(AuthService.tokenDTO.getAccessToken(), userId);
        Log.d("userid", String.valueOf(userId));
        call.enqueue(new Callback<UserReturnedDTO>() {

            @Override
            public void onResponse(Call<UserReturnedDTO> call, Response<UserReturnedDTO> response){
                Log.d("allDrivers", response.body().toString());
                if (method.equals("getById"))
                    sendUserByIdBroadcast(response.body());
                else if (method.equals("getUserName"))
                    sendUserNameBroadcast(response.body());
            }

            @Override
            public void onFailure(Call<UserReturnedDTO> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null ? t.getMessage() : "error");
            }
        });
    }

    private void getByEmail(String email){
        Call<UserReturnedDTO> call = RestUtils.userApi.getUserByEmail(AuthService.tokenDTO.getAccessToken(), email);
        Log.d("email", String.valueOf(email));
        call.enqueue(new Callback<UserReturnedDTO>() {

            @Override
            public void onResponse(Call<UserReturnedDTO> call, Response<UserReturnedDTO> response){
                //Log.d("EMAIL_REZ", response.body().toString());
                if(response.code() == 200) {
                    sendUserByEmailBroadcast(response.body());
                }
                else {
                    sendUserByEmailBroadcast(null);
                }
            }

            @Override
            public void onFailure(Call<UserReturnedDTO> call, Throwable t) {
                Log.d("EMAIL_REZ", t.getMessage() != null ? t.getMessage() : "error");
            }
        });
    }

    private void sendMessage(MessageDTO message) {

        Call<MessageReturnedDTO> call = RestUtils.userApi.sendMessage(AuthService.tokenDTO.getAccessToken(), message.getReceiverId(), message);
        call.enqueue(new Callback<MessageReturnedDTO>() {

            @Override
            public void onResponse(Call<MessageReturnedDTO> call, Response<MessageReturnedDTO> response){
                //Log.d("EMAIL_REZ", response.body().toString());
                if (response.body() != null)
                    Log.d("MESS", response.body().toString());
                else
                    Log.d("MESS", "SENDING ERROR");
            }

            @Override
            public void onFailure(Call<MessageReturnedDTO> call, Throwable t) {
                Log.d("EMAIL_REZ", t.getMessage() != null ? t.getMessage() : "error");
            }
        });
    }

    private void getMessages(int id) {
        Call<AllMessagesDTO> call = RestUtils.userApi.getMessages(AuthService.tokenDTO.getAccessToken(), id);
        call.enqueue(new Callback<AllMessagesDTO>() {

            @Override
            public void onResponse(Call<AllMessagesDTO> call, Response<AllMessagesDTO> response){
                //Log.d("EMAIL_REZ", response.body().toString());
                if (response.body() != null) {
                    getMessagesBroadcast(response.body());
                }
                else {
                    Log.d("MESS", "SENDING ERROR");
                }
            }

            @Override
            public void onFailure(Call<AllMessagesDTO> call, Throwable t) {
                Log.d("EMAIL_REZ", t.getMessage() != null ? t.getMessage() : "error");
            }
        });    }


    private void sendUserByIdBroadcast(UserReturnedDTO user){
        Intent intent = new Intent("userDetailsDialog");
        intent.putExtra("userById", user);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void sendUserNameBroadcast(UserReturnedDTO user){
        Intent intent = new Intent("ratingsAdapter");
        intent.putExtra("userName", user.getName() + " " + user.getSurname());
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void sendUserByEmailBroadcast(UserReturnedDTO user){
        Intent intent = new Intent("inviteOthersFragment");
        intent.putExtra("userByEmail", user);
        //Log.d("provera", user.getName());
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void getMessagesBroadcast(AllMessagesDTO dto){
        Intent intent = new Intent("chatActivity");
        intent.putExtra("messages", dto);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
