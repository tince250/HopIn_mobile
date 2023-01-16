package com.example.uberapp_tim13.rest;

import com.example.uberapp_tim13.dtos.AllMessagesDTO;
import com.example.uberapp_tim13.dtos.CredentialsDTO;
import com.example.uberapp_tim13.dtos.MessageDTO;
import com.example.uberapp_tim13.dtos.MessageReturnedDTO;
import com.example.uberapp_tim13.dtos.TokenDTO;
import com.example.uberapp_tim13.dtos.UserDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserAPI {

    @GET(RestUtils.USER_GET)
    Call<UserDTO> doGetUser();


    @GET("user/{id}")
    Call<UserDTO> doGetUser(@Header("Authorization") String token,
                            @Path("id") int id);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST(RestUtils.LOGIN)
    Call<TokenDTO> login(@Body CredentialsDTO credentialsDTO);

    @GET("user/email?")
    Call<UserDTO> getUserByEmail(@Header("Authorization") String token,
                                 @Query("email") String email);

    @POST("user/{id}/message")
    Call<MessageReturnedDTO> sendMessage(@Header("Authorization") String token,
                                         @Path("id") int id,
                                         @Body MessageDTO message);

    @GET("user/{id}/message")
    Call<AllMessagesDTO> getMessages(@Header("Authorization") String token,
                                     @Path("id") int id);
}
