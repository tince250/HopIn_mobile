package com.example.uberapp_tim13.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.example.uberapp_tim13.R;

public class LocationDialog extends AlertDialog.Builder {


    public LocationDialog(Context context) {
        super(context);
        setUpDialog();
    }

    private void setUpDialog() {
        setTitle(R.string.oops);
        setMessage(R.string.location_disabled_message);
        setCancelable(false);

        setPositiveButton(R.string.enable, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                getContext().startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                dialog.dismiss();
            }
        });

        setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {
                dialog.cancel();
            }
        });
    }

    public AlertDialog prepareDialog() {
        AlertDialog dialog = create();
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }
}
