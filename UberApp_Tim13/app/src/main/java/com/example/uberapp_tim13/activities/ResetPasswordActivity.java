package com.example.uberapp_tim13.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.services.UserService;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText emailET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Reset password");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_reset_password);

        emailET = findViewById(R.id.emailET);
    }

    public void resetPassword(View v){
        if (isEmailValid()){
            Intent intent = new Intent(getApplicationContext(), UserService.class);
            intent.putExtra("method", "resetPassword");
            intent.putExtra("email", emailET.getText().toString());
            startService(intent);
        } else {
            Toast.makeText(ResetPasswordActivity.this, "Bad email input! Try again", Toast.LENGTH_LONG);
        }
    }

    private boolean isEmailValid(){
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
        return valid;
    }


}
