package com.example.uberapp_tim13.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.uberapp_tim13.R;

public class FavoriteRouteFragment extends Fragment {
    ImageView iconHeart;
    ImageView iconRepeat;

    public static FavoriteRouteFragment newInstance() {
        return new FavoriteRouteFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_route, container, false);
        iconHeart = view.findViewById(R.id.heartImg);
        iconRepeat = view.findViewById(R.id.repeatImg);

        iconHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iconHeart.setColorFilter(ContextCompat.getColor(getContext(), R.color.dark_gray));
            }
        });

        iconRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "You chose to repeat ride!", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
