package com.example.afinal.fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.afinal.DBHelper;
import com.example.afinal.Product;
import com.example.afinal.adapter.ProductAdapter;
import com.example.afinal.R;
import com.example.afinal.api.ApiService;
import com.example.afinal.api.ProductClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ProductAdapter productAdapter;
    private String loggedInUser;
    private DBHelper dbHelper;
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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        progressBar = view.findViewById(R.id.ProgressBar);
        textViewConnectionLost = view.findViewById(R.id.connectionlost);
        btn_retry = view.findViewById(R.id.btn_retry);

        productAdapter = new ProductAdapter(new ArrayList<>(), getContext());
        recyclerView.setAdapter(productAdapter);

        dbHelper = new DBHelper(getContext());
        loggedInUser = dbHelper.getLoggedInUser();

        progressBar.setVisibility(View.VISIBLE);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (isNetworkAvailable(getContext())) {
                fetchProducts();
            } else {
                showConnectionLost();
            }
        }, 2000);

        btn_retry.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            btn_retry.setVisibility(View.GONE);
            textViewConnectionLost.setVisibility(View.GONE);

            executorService.execute(() -> {
                if (isNetworkAvailable(getContext())) {
                    getActivity().runOnUiThread(this::fetchProducts);
                } else {
                    getActivity().runOnUiThread(this::handleRetryFailure);
                }
            });
        });

        return view;
    }

    private void fetchProducts() {
        ApiService apiService = ProductClient.getClient().create(ApiService.class);
        Call<List<Product>> call = apiService.getProduct();
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                progressBar.setVisibility(View.GONE); // Hide progress bar

                if (response.isSuccessful()) {
                    List<Product> productList = response.body();
                    if (productList != null && !productList.isEmpty()) {
                        productAdapter.setProducts(productList); // Set products to adapter
                        recyclerView.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(getContext(), "No products found", Toast.LENGTH_SHORT).show();
                        recyclerView.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(getContext(), "Failed to fetch products", Toast.LENGTH_SHORT).show();
                    recyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                progressBar.setVisibility(View.GONE); // Hide progress bar
                Toast.makeText(getContext(), "Network error! Please try again later.", Toast.LENGTH_SHORT).show();
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
        }, 300);
    }
}
