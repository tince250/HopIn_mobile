package com.example.uberapp_tim13.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.dtos.CredentialsDTO;
import com.example.uberapp_tim13.dtos.WorkingHoursDTO;
import com.example.uberapp_tim13.dtos.WorkingHoursStartDTO;
import com.example.uberapp_tim13.rest.RestUtils;
import com.example.uberapp_tim13.services.AuthService;
import com.example.uberapp_tim13.tools.Globals;

import java.time.LocalDateTime;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        
    }

    public void login(View v) {
        TextView email = findViewById(R.id.emailET);
        TextView password = (TextView) findViewById(R.id.passwordET);

//        int id = Integer.parseInt(email.getText().toString());

        Intent intent = new Intent(this, AuthService.class);
        intent.putExtra("credentials", new CredentialsDTO(email.getText().toString(), password.getText().toString()));
//        intent.putExtra("user", id);
        startService(intent);

        setBroadcast();

    }

    private void setBroadcast() {
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean done = (boolean) intent.getExtras().get("done");

                if (done) {
                    Log.d("prov", Globals.userRole);

                    if (Globals.userRole.equals("passenger")) {
                        startActivity(new Intent(LoginActivity.this, PassengerMainActivity.class));
                    }else if (Globals.userRole.equals("driver")) {
                        startActivity(new Intent(LoginActivity.this, DriverMainActivity.class));
                    }else {
                        Toast.makeText(getParent(), "Wrong email or password", Toast.LENGTH_LONG);
                    }

                }
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("userLoggedIn"));

    }

    public void goToRegister(View v) {
        startActivity(new Intent(LoginActivity.this, PassengerRegisterActivity.class));
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}