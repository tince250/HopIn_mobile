package com.example.uberapp_tim13.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.dtos.PanicRideDTO;
import com.example.uberapp_tim13.dtos.ReasonDTO;
import com.example.uberapp_tim13.dtos.RideReturnedDTO;
import com.example.uberapp_tim13.rest.RestUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PanicReasonDialog extends Dialog {

    RideReturnedDTO ride;
    EditText reasonText;

    public PanicReasonDialog(@NonNull Context context, RideReturnedDTO ride) {
        super(context);

        this.ride = ride;
        setupDialog();
    }

    private void setupDialog() {
        setTitle(R.string.why);
        setCancelable(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_panic_reason);

        reasonText = findViewById(R.id.panicReasonTF);

        findViewById(R.id.panicReasonBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReasonDTO reason = new ReasonDTO(reasonText.getText().toString());
                Call<PanicRideDTO> call = RestUtils.rideAPI.panicRide(ride.getId(), reason);
                call.enqueue(new Callback<PanicRideDTO>() {
                    @Override
                    public void onResponse(Call<PanicRideDTO> call, Response<PanicRideDTO> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(getContext(), R.string.failPanic, Toast.LENGTH_LONG).show();
                            dismiss();
                            return;
                        }
                        Toast.makeText(getContext(), R.string.succPanic, Toast.LENGTH_LONG).show();

                        dismiss();
                    }

                    @Override
                    public void onFailure(Call<PanicRideDTO> call, Throwable t) {
                        Log.d("error", "Error while trying to call panic endpoint.");
                        cancel();
                    }
                });
            }
        });
    }
}
