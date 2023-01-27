package com.example.uberapp_tim13.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.dtos.CredentialsDTO;
import com.example.uberapp_tim13.services.AuthService;
import com.example.uberapp_tim13.tools.Globals;

public class LoginActivity extends Activity {

    private EditText emailET;
    private EditText passwordET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        emailET = findViewById(R.id.emailET);
        passwordET = findViewById(R.id.passwordET);
    }

    public void login(View v) {
        if (!isFormValid()){
            Toast.makeText(LoginActivity.this, "Bad credentials!", Toast.LENGTH_LONG).show();
            return;
        }
        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();

        Intent intent = new Intent(this, AuthService.class);
        intent.putExtra("credentials", new CredentialsDTO(email, password));
        startService(intent);

        setBroadcast();
    }

    private void setBroadcast() {
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean done = (boolean) intent.getExtras().get("done");

                if (done) {
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

    private boolean isFormValid(){
        boolean valid = true;
        if (emailET.getText().toString().isEmpty()){
            emailET.setError("Email can't be empty.");
            valid = false;
        } else {
            if (!Patterns.EMAIL_ADDRESS.matcher(emailET.getText().toString()).matches()) {
                emailET.setError("Not a valid email.");
                valid = false;
            }
        }
        if (passwordET.getText().toString().isEmpty()){
            passwordET.setError("Password can't be empty.");
            valid = false;
        }
        return valid;
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