package com.example.uberapp_tim13.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.activities.DriverMainActivity;
import com.example.uberapp_tim13.activities.LoginActivity;
import com.example.uberapp_tim13.dtos.WorkingHoursDTO;
import com.example.uberapp_tim13.dtos.WorkingHoursEndDTO;
import com.example.uberapp_tim13.rest.RestUtils;
import com.example.uberapp_tim13.services.AuthService;
import com.example.uberapp_tim13.tools.Globals;

import java.time.LocalDateTime;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
                if (Globals.userRole.equals("driver") && Globals.isActive == true) { setDriverInactive(); }
                activity.startActivity((new Intent(activity, LoginActivity.class)));
                break;
            case R.id.cancelBtn:
                dismiss();
                break;
            default:
                break;
        }
    }

    private void setDriverInactive() {
        WorkingHoursEndDTO end = new WorkingHoursEndDTO(LocalDateTime.now().withNano(0).toString());
        Call<WorkingHoursDTO> call = RestUtils.driverAPI.updateWorkingHours(AuthService.tokenDTO.getAccessToken(), DriverMainActivity.workingHours.getId(), end);
        call.enqueue(new Callback<WorkingHoursDTO>() {
            @Override
            public void onResponse(Call<WorkingHoursDTO> call, Response<WorkingHoursDTO> response) {
                if (response.isSuccessful()) {
                    DriverMainActivity.workingHours = response.body();
                    Globals.isActive = false;
                    Log.d("hours", DriverMainActivity.workingHours.toString());
                } else {
                    Log.d("hours", String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<WorkingHoursDTO> call, Throwable t) {
                Log.d("EVOME", t.toString());
            }
        });
    }
}
