package com.example.uberapp_tim13.adapters.invited_passengers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.dtos.UserReturnedDTO;
import com.example.uberapp_tim13.tools.Globals;

import java.util.List;

public class InvitedPassengersAdapter extends BaseAdapter {

    private Context context;

    private Activity activity;
    private List<UserReturnedDTO> users;
    private TextView pressedName;
    private List<Boolean> accepted;

    public InvitedPassengersAdapter(Activity activity, List<UserReturnedDTO> users, List<Boolean> accepted, Context context) {
        this.activity = activity;
        this.users = users;
        this.accepted = accepted;
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
        UserReturnedDTO user = this.users.get(i);
        Boolean accepted = this.accepted.get(i);
        View view_new = view;

        if(view == null) {
            view_new = activity.getLayoutInflater().inflate(R.layout.invited_passenger_item, null);
        }

        this.pressedName = (TextView)view_new.findViewById(R.id.passengerNameTV);
        this.pressedName.setText(user.getName() + " " + user.getSurname());


        if (accepted == null)
            view_new.findViewById(R.id.deleteItemBtn).setVisibility(View.VISIBLE);
        else if (accepted == true){
            view_new.findViewById(R.id.deleteItemBtn).setVisibility(View.GONE);
            view_new.findViewById(R.id.acceptedItemBtn).setVisibility(View.VISIBLE);
        }
        else if (accepted == false) {
            view_new.findViewById(R.id.deleteItemBtn).setVisibility(View.GONE);
            view_new.findViewById(R.id.declinedItemBtn).setVisibility(View.VISIBLE);
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
