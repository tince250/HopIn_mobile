package com.example.uberapp_tim13;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.uberapp_tim13.tools.Globals;

public class DriverStatisticsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Statistics");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_driver_statistics);

        ((TextView) findViewById(R.id.driverNameTV)).setText("Hey, " + Globals.currentUser.getName());

        setSpinner();
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

    private void setSpinner() {
        Spinner areaNumSpinner = findViewById(R.id.spinnerStatisticsOptions);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.statistic_options));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        areaNumSpinner.setAdapter(adapter);
    }
}
