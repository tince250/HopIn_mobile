package com.example.uberapp_tim13;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.uberapp_tim13.fragments.FavoriteRouteFragment;
import com.example.uberapp_tim13.tools.FragmentTransition;

public class FavoriteRoutesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Favorite routes");
        setContentView(R.layout.activity_favorite_routes);
        FragmentTransition.to(FavoriteRouteFragment.newInstance(), this, false, R.id.list);
    }
}