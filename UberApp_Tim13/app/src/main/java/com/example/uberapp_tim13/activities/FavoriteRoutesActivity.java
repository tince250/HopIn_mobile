package com.example.uberapp_tim13.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.adapters.FavouriteRoutesAdapter;
import com.example.uberapp_tim13.dialogs.OrderAgainDialog;
import com.example.uberapp_tim13.dtos.RouteDTO;
import com.example.uberapp_tim13.fragments.FavoriteRouteFragment;
import com.example.uberapp_tim13.rest.RestUtils;
import com.example.uberapp_tim13.services.AuthService;
import com.example.uberapp_tim13.tools.FragmentTransition;
import com.example.uberapp_tim13.tools.Globals;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteRoutesActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FavouriteRoutesAdapter adapterRV;
    private RecyclerView.LayoutManager layoutManagerRV;

    private OrderAgainDialog orderAgainDialog;

    private List<RouteDTO> routes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Favorite routes");
        setContentView(R.layout.activity_favorite_routes);

        recyclerView = findViewById(R.id.favouriteRoutesRV);
        layoutManagerRV = new LinearLayoutManager(getParent());
        recyclerView.setLayoutManager(layoutManagerRV);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Call<List<RouteDTO>> call = RestUtils.passengerAPI.getFavouriteRoutes(AuthService.tokenDTO.getAccessToken(), Globals.user.getId());
        call.enqueue(new Callback<List<RouteDTO>>() {
            @Override
            public void onResponse(Call<List<RouteDTO>> call, Response<List<RouteDTO>> response) {
                if (response.isSuccessful()) {
                    Log.d("OMILJENE", response.body().toString());
                    routes = response.body();

                    adapterRV = new FavouriteRoutesAdapter(routes);
                    recyclerView.setAdapter(adapterRV);
                    adapterRV.setListener(new FavouriteRoutesAdapter.OnItemClickListener() {
                        @Override
                        public void onOrderAgainClick(int position) {
                            orderAgainDialog = new OrderAgainDialog(FavoriteRoutesActivity.this, routes.get(position));
                            orderAgainDialog.show();
                        }

                        @Override
                        public void onHeartClick(int position, ImageView heartIcon) {
                            displayAreYouSureDialog(position, heartIcon);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<RouteDTO>> call, Throwable t) {
                Toast.makeText(FavoriteRoutesActivity.this, "Oops! An error occured while trying to fetch fav routes :(", Toast.LENGTH_LONG);
            }
        });

    }

    private void removeFromFavourites(int position) {
        routes.remove(position);
        adapterRV.notifyItemRemoved(position);
    }

    private void displayAreYouSureDialog(int position, ImageView heartIcon) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        removeRouteFromFavourites(position, heartIcon);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(FavoriteRoutesActivity.this,  R.style.AlertDialogTheme);
        builder.setMessage("Are you sure that you want to remove route from favourites?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    private void removeRouteFromFavourites(int position, ImageView heartIcon) {
        ImageViewCompat.setImageTintList(heartIcon, ColorStateList.valueOf(ContextCompat.getColor(FavoriteRoutesActivity.this, R.color.dark_gray)));
        Call<Void> call = RestUtils.passengerAPI.removeFavouriteRoute(AuthService.tokenDTO.getAccessToken(), Globals.user.getId(), routes.get(position).getId());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    removeFromFavourites(position);
                    Toast.makeText(FavoriteRoutesActivity.this, "Route removed from favourites", Toast.LENGTH_SHORT);
                } else {
                    Toast.makeText(FavoriteRoutesActivity.this, "Error! Try again.", Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(FavoriteRoutesActivity.this, "Error! Try again.", Toast.LENGTH_SHORT);
            }
        });
    }
}