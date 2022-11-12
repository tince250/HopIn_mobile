package com.example.uberapp_tim13;

import static android.app.PendingIntent.getActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.uberapp_tim13.fragments.DriverStatisticsFragment;
import com.example.uberapp_tim13.tools.FragmentTransition;

public class DriverAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_account);

        /*final Button button = (Button) findViewById(R.id.statisticsBtn);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentTransition.to(DriverStatisticsFragment.newInstance(), this, false, R.id.statisticsLayout);
            }
        });*/
        //FragmentTransition.to(DriverStatisticsFragment.newInstance(), this, false, R.id.accountLinearLayout);
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