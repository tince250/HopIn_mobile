package com.example.uberapp_tim13;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uberapp_tim13.items.User;
import com.example.uberapp_tim13.tools.Globals;
import com.example.uberapp_tim13.tools.Mockap;

import java.util.List;

public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

    }

    public void login(View v) {
        List<User> users = Mockap.getUsers();
        TextView email = findViewById(R.id.emailET);
        TextView password = (TextView) findViewById(R.id.passwordET);

        for(User user : users){
            if (email.getText().toString().equals(user.getEmail()) &&
                password.getText().toString().equals(user.getPassword())){
                Globals.currentUser = user;
                Globals.userRole = user.getRole();
                break;
            }
        }

        if (Globals.userRole.equals("passenger")) {
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