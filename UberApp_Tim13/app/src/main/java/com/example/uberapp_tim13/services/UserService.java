package com.example.uberapp_tim13.services;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.uberapp_tim13.activities.LoginActivity;
import com.example.uberapp_tim13.activities.PassengerRegisterActivity;
import com.example.uberapp_tim13.activities.ResetPasswordActivity;
import com.example.uberapp_tim13.dtos.AllMessagesDTO;
import com.example.uberapp_tim13.dtos.InboxReturnedDTO;
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
                } else if (method.equals("resetPassword")){
                    String email = (String) extras.get("email");
                    resetPassword(email);
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
        Call<InboxReturnedDTO> call = RestUtils.userApi.getInbox(AuthService.tokenDTO.getAccessToken(), id);
        call.enqueue(new Callback<InboxReturnedDTO>() {

            @Override
            public void onResponse(Call<InboxReturnedDTO> call, Response<InboxReturnedDTO> response){
                //Log.d("EMAIL_REZ", response.body().toString());
                if (response.body() != null) {
                    getMessagesBroadcast(response.body());
                }
                else {
                    Log.d("MESS", "SENDING ERROR");
                }
            }

            @Override
            public void onFailure(Call<InboxReturnedDTO> call, Throwable t) {
                Log.d("EMAIL_REZ", t.getMessage() != null ? t.getMessage() : "error");
            }
        });
    }

    private void resetPassword(String email) {
        Log.d("email", email);
        Call<String> call = RestUtils.userApi.resetPassword(email);
        call.enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response){
                if (response.isSuccessful()) {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(getApplicationContext(), "Request for reset password sent! Check your email!", Toast.LENGTH_LONG).show();
//                        getApplicationContext().startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        Intent mIntent = new Intent(getApplicationContext(),LoginActivity.class);
                        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getApplicationContext().startActivity(mIntent);
                    });
                } else {
                    Log.d("neuspeh", String.valueOf(response.code()));
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(getApplicationContext(), "User with that email doesn't exist", Toast.LENGTH_LONG).show();
                    });
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("EMAIL_REZ", t.getMessage() != null ? t.getMessage() : "error");
            }
        });
    }




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

    private void getMessagesBroadcast(InboxReturnedDTO dto){
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
