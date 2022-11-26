package com.example.uberapp_tim13.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.uberapp_tim13.R;

public class AccountDetailsDialog extends Dialog implements android.view.View.OnClickListener {

    Activity activity;

    public AccountDetailsDialog(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_account_details);
    }

    @Override
    public void onClick(View view) {

    }
}
