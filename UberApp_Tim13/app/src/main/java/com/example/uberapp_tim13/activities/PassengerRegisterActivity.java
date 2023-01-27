package com.example.uberapp_tim13.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.adapters.ride_history.HistoryAdapter;
import com.example.uberapp_tim13.dtos.AllPassengerRidesDTO;
import com.example.uberapp_tim13.dtos.UserDTO;
import com.example.uberapp_tim13.dtos.UserReturnedDTO;
import com.example.uberapp_tim13.fragments.RideLoadingFragment;
import com.example.uberapp_tim13.services.PassengerService;
import com.example.uberapp_tim13.services.UserService;
import com.example.uberapp_tim13.tools.FragmentTransition;

public class PassengerRegisterActivity extends Activity {
    EditText nameET;
    EditText surnameET;
    EditText emailET;
    EditText passwordET;
    EditText confirmPasswordET;
    EditText addressET;
    EditText phoneET;
    Spinner areaNumSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_register);

        nameET = findViewById(R.id.nameET);
        surnameET = findViewById(R.id.surNameET);
        emailET = findViewById(R.id.emailRegisterET);
        passwordET = findViewById(R.id.passwordRegisterET);
        confirmPasswordET = findViewById(R.id.confirmPasswordET);
        addressET = findViewById(R.id.cityET);
        phoneET = findViewById(R.id.phoneET);
        areaNumSpinner = findViewById(R.id.spinnerAreaNum);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.area_number_options));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        areaNumSpinner.setAdapter(adapter);

        setBroadcast();

        findViewById(R.id.registerBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateForm()) {
                    return;
                }
                UserDTO user = new UserDTO(nameET.getText().toString(), surnameET.getText().toString(),
                        emailET.getText().toString(), passwordET.getText().toString(), addressET.getText().toString(),
                        areaNumSpinner.getSelectedItem().toString() + phoneET.getText().toString(), null);
                Intent intentPassengerService = new Intent(getApplicationContext(), PassengerService.class);
                intentPassengerService.putExtra("method", "register");
                intentPassengerService.putExtra("user", user);
                startService(intentPassengerService);
                Log.d("proa", "prob");
            }
        });
    }

    public boolean validateForm() {
        boolean valid = true;
        if (nameET.getText().toString().isEmpty()) {
            nameET.setError("Name can't be empty.");
            valid = false;
        }

        if (surnameET.getText().toString().isEmpty()) {
            surnameET.setError("Surname can't be empty.");
            valid = false;
        }

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
        } else {
            if (!passwordET.getText().toString().equals(confirmPasswordET.getText().toString())) {
                passwordET.setError("Bad password confirmation.");
                confirmPasswordET.setError("Bad password confirmation.");
                valid = false;
            }
        }

        if (addressET.getText().toString().isEmpty()){
            addressET.setError("City can't be empty.");
            valid = false;
        } else {
            if (!addressET.getText().toString().matches("^([a-zA-Z0-9 \\s,'-]*)$")) {
                addressET.setError("Not a valid city.");
                valid = false;
            }
        }

        if (phoneET.getText().toString().isEmpty()){
            phoneET.setError("Phone can't be empty.");
            valid = false;
        } else {
            if (!Patterns.PHONE.matcher(phoneET.getText().toString()).matches()) {
                phoneET.setError("Not a valid phone number.");
                valid = false;
            }
        }
        return valid;
    }

    public void setBroadcast() {
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle extras = intent.getExtras();
                boolean success = extras.getBoolean("success");
                if (success) {
                    Toast.makeText(getApplicationContext(),"Registration successful, activation email has been sent to " + emailET.getText().toString(),Toast.LENGTH_SHORT).show();
                    resetForm();
                } else {
                    Toast.makeText(getApplicationContext(),"Registration failed, email may already be in use.",Toast.LENGTH_SHORT).show();
                }
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("registerActivity"));

    }

    private void resetForm() {
        nameET.setText("");
        surnameET.setText("");
        emailET.setText("");
        passwordET.setText("");
        confirmPasswordET.setText("");
        addressET.setText("");
        phoneET.setText("");
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