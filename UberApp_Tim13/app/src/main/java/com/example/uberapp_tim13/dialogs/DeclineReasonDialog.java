package com.example.uberapp_tim13.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.uberapp_tim13.R;
import com.google.android.material.button.MaterialButton;

public class DeclineReasonDialog extends Dialog implements android.view.View.OnClickListener {

    Activity activity;
    MaterialButton passengersReasonBtn;
    MaterialButton familyReasonBtn;
    MaterialButton otherReasonBtn;
    MaterialButton submitBtn;
    EditText otherReasonET;

    public DeclineReasonDialog(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_decline_reason);
        setComponents();
        setListeners();
    }

    private void setComponents() {
        passengersReasonBtn = findViewById(R.id.passengersReasonBtn);
        familyReasonBtn = findViewById(R.id.familyReasonBtn);
        otherReasonBtn = findViewById(R.id.otherReasonBtn);
        otherReasonET = findViewById(R.id.otherReasonET);
        submitBtn = findViewById(R.id.submitBtn);
    }

    private void setListeners(){
        passengersReasonBtn.setOnClickListener(this);
        familyReasonBtn.setOnClickListener(this);
        otherReasonBtn.setOnClickListener(this);
        submitBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.submitBtn) {
            Toast.makeText(activity, "submited", Toast.LENGTH_SHORT).show();
            dismiss();
        }
        else {
            resetComponents();
            switch (v.getId()) {
                case R.id.passengersReasonBtn:
                    passengersReasonBtn.setTextColor(activity.getResources().getColor(R.color.teal_200));
                    passengersReasonBtn.setStrokeColor(ColorStateList.valueOf(activity.getResources().getColor(R.color.teal_200)));
                    break;
                case R.id.familyReasonBtn:
                    familyReasonBtn.setTextColor(activity.getResources().getColor(R.color.teal_200));
                    familyReasonBtn.setStrokeColor(ColorStateList.valueOf(activity.getResources().getColor(R.color.teal_200)));
                    break;
                case R.id.otherReasonBtn:
                    otherReasonBtn.setTextColor(activity.getResources().getColor(R.color.teal_200));
                    otherReasonBtn.setStrokeColor(ColorStateList.valueOf(activity.getResources().getColor(R.color.teal_200)));
                    otherReasonET.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
        }

    }

    private void resetComponents(){
        passengersReasonBtn.setTextColor(activity.getResources().getColor(R.color.disabled_gray));
        familyReasonBtn.setTextColor(activity.getResources().getColor(R.color.disabled_gray));
        otherReasonBtn.setTextColor(activity.getResources().getColor(R.color.disabled_gray));
        passengersReasonBtn.setStrokeColor(ColorStateList.valueOf(activity.getResources().getColor(R.color.disabled_gray)));
        familyReasonBtn.setStrokeColor(ColorStateList.valueOf(activity.getResources().getColor(R.color.disabled_gray)));
        otherReasonBtn.setStrokeColor(ColorStateList.valueOf(activity.getResources().getColor(R.color.disabled_gray)));
        otherReasonET.setVisibility(View.GONE);
    }
}
