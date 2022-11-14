package com.example.uberapp_tim13.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.tools.Globals;

public class PaymentInfoFragment extends Fragment {
    public static PaymentInfoFragment newInstance() {
        return new PaymentInfoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment_info, container, false);
        fitFragmentToRole(view);
        return view;
    }

    private void fitFragmentToRole(View view) {
        ((EditText) view.findViewById(R.id.editTextNameOnCard)).setText(Globals.currentUser.getName() + " " + Globals.currentUser.getSurName());
        ((EditText) view.findViewById(R.id.editTextCardNumber)).setText(Globals.currentUser.getCard().getNumber());
        ((EditText) view.findViewById(R.id.editTextDD)).setText(Globals.currentUser.getCard().getMonth());
        ((EditText) view.findViewById(R.id.editTextYY)).setText(Globals.currentUser.getCard().getYear());
        ((EditText) view.findViewById(R.id.editTextCVC)).setText(Globals.currentUser.getCard().getCvc());
    }
}
