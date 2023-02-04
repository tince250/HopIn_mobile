package com.example.uberapp_tim13.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.dtos.PanicRideDTO;
import com.example.uberapp_tim13.dtos.ReasonDTO;
import com.example.uberapp_tim13.dtos.RideReturnedDTO;
import com.example.uberapp_tim13.rest.RestUtils;
import com.example.uberapp_tim13.services.AuthService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RideStateDialog extends Dialog {

    private String type;
    private TextView info;
    public RideStateDialog(@NonNull Context context, String type) {
        super(context);

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.type = type;
        setupDialog();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_accepted_ride);

        setupDialog();

        findViewById(R.id.closeBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }

    private void setupDialog() {
        info = findViewById(R.id.infoTV);
        if (type.equals("ACCEPTED")) {
            info.setText(R.string.dialogAcceptedRideInfo);
        } else if (type.equals("NOT_FOUND")) {
            info.setText(R.string.dialogRideNotFoundInfo);
        } else {
            info.setText(R.string.dialogRideFinishedInfo);
        }
    }
}
