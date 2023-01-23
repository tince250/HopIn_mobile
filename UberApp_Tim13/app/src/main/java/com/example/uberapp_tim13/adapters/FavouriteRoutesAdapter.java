package com.example.uberapp_tim13.adapters;

import android.content.res.ColorStateList;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.dtos.RouteDTO;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Route;

public class FavouriteRoutesAdapter extends RecyclerView.Adapter<FavouriteRoutesAdapter.FavouritesViewHolder> {

    private List<RouteDTO> favouriteRoutes;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onHeartClick(int position, ImageView heartIcon);
    }

    public OnItemClickListener getListener() {
        return listener;
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public FavouriteRoutesAdapter(List<RouteDTO> favouriteRoutes) {
        this.favouriteRoutes = favouriteRoutes;
    }

    @NonNull
    @Override
    public FavouritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.favourite_route_card, parent, false);
        FavouritesViewHolder fvh = new FavouritesViewHolder(v, listener);
        return fvh;
    }

    @Override
    public void onBindViewHolder(@NonNull FavouritesViewHolder holder, int position) {
        RouteDTO currentItem = this.favouriteRoutes.get(position);

        holder.locationsTV.setText(currentItem.getDeparture().getAddress() + " -> " + currentItem.getDestination().getAddress());
        holder.distanceTV.append(" " + currentItem.getDistance() + " km");
    }

    @Override
    public int getItemCount() {
        return this.favouriteRoutes.size();
    }

    public static class FavouritesViewHolder extends RecyclerView.ViewHolder {

        public TextView locationsTV;
        public TextView distanceTV;
        public ImageView heartIcon;

        public FavouritesViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);

            locationsTV = itemView.findViewById(R.id.routeTV);
            distanceTV = itemView.findViewById(R.id.distanceTV);
            heartIcon = itemView.findViewById(R.id.heartImg);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });

            heartIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onHeartClick(position, heartIcon);
                        }
                    }
                }
            });
        }
    }
}
