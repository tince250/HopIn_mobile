package com.example.uberapp_tim13.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.activities.LoginActivity;

public class LogOutDialog extends Dialog implements android.view.View.OnClickListener {
    Activity activity;
    Button cancelBtn;
    Button logOutBtn;

    public LogOutDialog(@NonNull Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_log_out);
        setComponents();
        setListeners();
    }

    private void setComponents(){
        logOutBtn = findViewById(R.id.logOutBtn);
        cancelBtn = findViewById(R.id.cancelBtn);
    }

    private void setListeners(){
        logOutBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.logOutBtn:
                activity.startActivity((new Intent(activity, LoginActivity.class)));
                break;
            case R.id.cancelBtn:
                dismiss();
                break;
            default:
                break;
        }
    }
}
