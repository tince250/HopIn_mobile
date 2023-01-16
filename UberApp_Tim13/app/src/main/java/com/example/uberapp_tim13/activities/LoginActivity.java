package com.example.uberapp_tim13.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.dtos.CredentialsDTO;
import com.example.uberapp_tim13.services.AuthService;
import com.example.uberapp_tim13.tools.Globals;

public class LoginActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
    }

    public void login(View v) {
        TextView email = findViewById(R.id.emailET);
        TextView password = (TextView) findViewById(R.id.passwordET);

        Intent intent = new Intent(this, AuthService.class);
        intent.putExtra("credentials", new CredentialsDTO("driver@gmail.com", "123"));
        startService(intent);

        if (Globals.userRole.equals("S")) {
            startActivity(new Intent(LoginActivity.this, PassengerMainActivity.class));
        }else if (Globals.userRole.equals("driver")) {
            startActivity(new Intent(LoginActivity.this, DriverMainActivity.class));
        }else {
            Toast.makeText(this, "Wrong email or password", Toast.LENGTH_LONG);
        }


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