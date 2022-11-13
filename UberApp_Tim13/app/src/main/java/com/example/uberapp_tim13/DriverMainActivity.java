package com.example.uberapp_tim13;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class DriverMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_main);
        setTitle(R.string.home_nav);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.driver_upper_menu, menu);

        MenuItem switchItem = menu.findItem(R.id.activeToggle);
        switchItem.setActionView(R.layout.toggle_button_bar);
        final Switch switchBtn = (Switch) menu.findItem(R.id.activeToggle).getActionView().findViewById(R.id.active_switch);
        switchBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(getBaseContext(), "checked", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getBaseContext(), "not checked", Toast.LENGTH_LONG).show();
                }
            }
        });

        return true;
    }
}