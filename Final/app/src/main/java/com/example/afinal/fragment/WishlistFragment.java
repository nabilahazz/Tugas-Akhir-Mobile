package com.example.afinal.fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.afinal.DBHelper;
import com.example.afinal.Product;
import com.example.afinal.R;
import com.example.afinal.adapter.WishlistAdapter;
import com.example.afinal.api.ApiService;
import com.example.afinal.api.ProductClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WishlistFragment extends Fragment {

    private RecyclerView recyclerView;
    private WishlistAdapter wishlistAdapter;
    private DBHelper dbHelper;
    private String loggedInUser;
    private List<Product> allDestinations = new ArrayList<>();
    private ProgressBar progressBar;
    private TextView textViewConnectionLost;
    private Button btn_retry;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wishlist, container, false);

        dbHelper = new DBHelper(getContext());
        loggedInUser = dbHelper.getLoggedInUser();

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        wishlistAdapter = new WishlistAdapter(getContext(), new ArrayList<>());
        recyclerView.setAdapter(wishlistAdapter);

        progressBar = view.findViewById(R.id.ProgressBar);
        textViewConnectionLost = view.findViewById(R.id.connectionlost);
        btn_retry = view.findViewById(R.id.btn_retry);

        loadDestinationsWithRetry();

        btn_retry.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            btn_retry.setVisibility(View.GONE);
            textViewConnectionLost.setVisibility(View.GONE);

            executorService.execute(() -> {
                if (isNetworkAvailable(getContext())) {
                    getActivity().runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        loadDestinations();
                    });
                } else {
                    getActivity().runOnUiThread(this::handleRetryFailure);
                }
            });
        });

        return view;
    }

    private void loadDestinationsWithRetry() {
        progressBar.setVisibility(View.VISIBLE);
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (isNetworkAvailable(getContext())) {
                loadDestinations();
            } else {
                showConnectionLost();
            }
        }, 2000);
    }

    private void loadDestinations() {
        ApiService apiService = ProductClient.getClient().create(ApiService.class);
        Call<List<Product>> call = apiService.getProduct();
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    allDestinations = response.body();
                    filterFavoriteDestinations();
                } else {
                    Toast.makeText(getContext(), "Failed to fetch destinations", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showConnectionLost() {
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        textViewConnectionLost.setVisibility(View.VISIBLE);
        btn_retry.setVisibility(View.VISIBLE);
    }

    private void handleRetryFailure() {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            progressBar.setVisibility(View.GONE);
            btn_retry.setVisibility(View.VISIBLE);
            textViewConnectionLost.setVisibility(View.VISIBLE);
        }, 300); // Display progress bar for a brief moment
    }

    private void filterFavoriteDestinations() {
        List<Product> favoriteDestinations = new ArrayList<>();
        for (Product product : allDestinations) {
            if (dbHelper.isFavorite(loggedInUser, String.valueOf(product.getId()))) {
                favoriteDestinations.add(product);
            }
        }
        if (!favoriteDestinations.isEmpty()) {
            wishlistAdapter.updateData(favoriteDestinations);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(getContext(), "No favorite destinations found", Toast.LENGTH_SHORT).show();
        }
    }
}
