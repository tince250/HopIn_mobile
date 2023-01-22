package com.example.uberapp_tim13.fragments;

import android.os.Bundle;
import android.os.PatternMatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.dtos.ChangePasswordDTO;
import com.example.uberapp_tim13.rest.RestUtils;
import com.example.uberapp_tim13.services.AuthService;
import com.example.uberapp_tim13.tools.Globals;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordFragment extends Fragment {

    private EditText oldPasswordET;
    private EditText newPasswordET;
    private EditText confNewPasswordET;

    public static ChangePasswordFragment newInstance() {
        return new ChangePasswordFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_password_change, container, false);

        oldPasswordET = (EditText) view.findViewById(R.id.oldPasswordET);
        newPasswordET = (EditText) view.findViewById(R.id.newPasswordET);
        confNewPasswordET = (EditText) view.findViewById(R.id.confirmPasswordET);

        view.findViewById(R.id.changePasswordBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateFields())
                    return;

                Call<String> call = RestUtils.userApi.changePassword(AuthService.tokenDTO.getAccessToken(), Globals.user.getId(), new ChangePasswordDTO(newPasswordET.getText().toString(), oldPasswordET.getText().toString()));
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getActivity(), "Password successfully changed!", Toast.LENGTH_LONG).show();
                        }
                        else
                            Toast.makeText(getActivity(), "Error. Current password not matching the input?", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(getActivity(), "Oops! There was an error :(", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        return view;
    }

    private boolean validateFields() {
        boolean valid = true;
        if (oldPasswordET.getText().toString().isEmpty()) {
            oldPasswordET.setError("Old password can't be empty.");
            valid = false;
        }
        if (newPasswordET.getText().toString().isEmpty()) {
            newPasswordET.setError("New password can't be empty.");
            valid = false;
        }
        if (confNewPasswordET.getText().toString().isEmpty()) {
            confNewPasswordET.setError("Confirm password can't be empty.");
            valid = false;
        } else {
            if (!newPasswordET.getText().toString().equals(confNewPasswordET.getText().toString())) {
                confNewPasswordET.setError("Passwords don't match.");
            }
        }

        return valid;
    }
}
