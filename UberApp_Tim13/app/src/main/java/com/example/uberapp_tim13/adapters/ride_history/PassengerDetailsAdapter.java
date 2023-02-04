package com.example.uberapp_tim13.adapters.ride_history;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.dtos.UserReturnedDTO;
import com.example.uberapp_tim13.model.User;
import com.example.uberapp_tim13.tools.Mockap;
import com.example.uberapp_tim13.tools.Utils;
import com.example.uberapp_tim13.dtos.UserReturnedDTO;

import java.util.List;

public class PassengerDetailsAdapter extends BaseAdapter {

    private Activity activity;
    List<UserReturnedDTO> users;
    Context context;

    public PassengerDetailsAdapter(Activity activity, List<UserReturnedDTO> users, Context context) {
        this.activity = activity;
        this.users = users;
        this.context = context;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int i) {
        return users.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        UserReturnedDTO user = users.get(i);
        View view_new = view;

        if (view == null) {
            view_new = activity.getLayoutInflater().inflate(R.layout.passenger_details_item, null);
        }

        ((TextView) view_new.findViewById(R.id.detailsNameTV)).setText(user.getName());
        ((TextView) view_new.findViewById(R.id.detailsEmailTV)).setText(user.getEmail());
        ((TextView) view_new.findViewById(R.id.detailsContactTV)).setText(user.getTelephoneNumber());

        if (user.getProfilePicture() != null) {
            ImageView profilePic = view_new.findViewById(R.id.ic_profile);
            profilePic.setImageBitmap(Utils.convertBase64ToBitmap(user.getProfilePicture()));
        }

        view_new.findViewById(R.id.callBtn).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(Intent.ACTION_DIAL);
                String phoneNumber = users.get(i).getTelephoneNumber();
                intent.setData(Uri.parse("tel:" + phoneNumber));
                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent);
                }
            }
        });

        return view_new;
    }
}