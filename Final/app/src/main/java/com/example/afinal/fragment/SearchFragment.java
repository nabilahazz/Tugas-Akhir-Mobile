package com.example.afinal.fragment;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.afinal.Product;
import com.example.afinal.SearchResult;
import com.example.afinal.R;
import com.example.afinal.adapter.SearchAdapter;
import com.example.afinal.api.ApiService;
import com.example.afinal.api.ProductClient;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {

    private RecyclerView recyclerViewSearchResults;
    private SearchAdapter searchAdapter;
    private List<SearchResult> searchResults;
    private ApiService apiService;
    private ProgressBar progressBar;
    private TextView textViewConnectionLost;

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        progressBar = view.findViewById(R.id.ProgressBar);
        recyclerViewSearchResults = view.findViewById(R.id.recyclerViewSearchResults);
        textViewConnectionLost = view.findViewById(R.id.connectionlost);

        searchResults = new ArrayList<>();
        searchAdapter = new SearchAdapter(getContext(), searchResults);

        recyclerViewSearchResults.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewSearchResults.setAdapter(searchAdapter);

        apiService = ProductClient.getClient().create(ApiService.class);

        SearchView searchView = view.findViewById(R.id.Search);
        searchView.setQueryHint("search product");
        searchView.setIconifiedByDefault(false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                progressBar.setVisibility(View.VISIBLE);
                recyclerViewSearchResults.setVisibility(View.GONE);
                textViewConnectionLost.setVisibility(View.GONE);

                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.execute(() -> {
                    if (!TextUtils.isEmpty(newText)) {
                        performSearch(newText);
                    } else {
                        clearSearchResults();
                    }
                });
                return true;
            }
        });
        return view;
    }

    private void performSearch(String query) {
        if (!isNetworkAvailable(getContext())) {
            showNoInternetMessage();
        } else {
            Call<List<Product>> call = apiService.getProduct();
            call.enqueue(new Callback<List<Product>>() {
                @Override
                public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<Product> products = response.body();
                        searchResults.clear();
                        for (Product product : products) {
                            if (product.getName().toLowerCase().contains(query.toLowerCase())) {
                                SearchResult searchResult = new SearchResult(
                                        product.getId(),
                                        product.getName(),
                                        product.getPrice(),
                                        product.getImage_link(),
                                        product.getProduct_type(),
                                        product.getRating(),
                                        product.getDescription()


                                );
                                searchResults.add(searchResult);
                            }
                        }
                        updateSearchResults();
                    } else {
                        showToast("Failed to search data");
                    }
                }

                @Override
                public void onFailure(Call<List<Product>> call, Throwable t) {
                    showToast("Error: " + t.getMessage());
                }
            });
        }
    }

    private void clearSearchResults() {
        getActivity().runOnUiThread(() -> {
            searchResults.clear();
            searchAdapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);
            recyclerViewSearchResults.setVisibility(View.VISIBLE);
        });
    }

    private void updateSearchResults() {
        getActivity().runOnUiThread(() -> {
            searchAdapter.notifyDataSetChanged();
            if (searchResults.isEmpty()) {
                recyclerViewSearchResults.setVisibility(View.GONE);
            } else {
                recyclerViewSearchResults.setVisibility(View.VISIBLE);
            }
            progressBar.setVisibility(View.GONE);
        });
    }

    private void showNoInternetMessage() {
        getActivity().runOnUiThread(() -> {
            textViewConnectionLost.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        });
    }

    private void showToast(String message) {
        getActivity().runOnUiThread(() -> {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            
        });
    }
}




