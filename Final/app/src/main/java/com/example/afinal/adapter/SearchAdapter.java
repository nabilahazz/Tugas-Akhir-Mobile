package com.example.afinal.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.afinal.Product;
import com.example.afinal.SearchResult;
import com.example.afinal.R;
import com.example.afinal.activity.DetailActivity;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private Context context;
    private List<SearchResult> searchResults;

    public SearchAdapter(Context context, List<SearchResult> searchResults) {
        this.context = context;
        this.searchResults = searchResults;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SearchResult searchResult = searchResults.get(position);
        holder.bind(searchResult);
    }

    @Override
    public int getItemCount() {
        return searchResults.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageViewThumbnail;
        private TextView textViewName;
        private TextView textViewtype,price,rating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewThumbnail = itemView.findViewById(R.id.IV_product);
            textViewName = itemView.findViewById(R.id.TV_name);
            textViewtype = itemView.findViewById(R.id.TV_type);
            price = itemView.findViewById(R.id.price);
            rating = itemView.findViewById(R.id.tv_rating);
        }

        public void bind(SearchResult searchResult) {
            textViewName.setText(searchResult.getName());
            textViewtype.setText(searchResult.getProduct_type());
            rating.setText(searchResult.getRating());
            price.setText(String.valueOf(searchResult.getPrice()));

            Glide.with(itemView.getContext())
                    .load(searchResult.getImage_link())
                    .placeholder(R.drawable.placeholder)
                    .into(imageViewThumbnail);

            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, DetailActivity.class);
                Product product = new Product(
                        searchResult.getId(),
                        searchResult.getName(),
                        searchResult.getImage_link(),
                        searchResult.getPrice(),
                        searchResult.getRating(),
                        searchResult.getDescription(),
                        searchResult.getProduct_type()

                );
                intent.putExtra("product", product); // Kirim objek Destination
                context.startActivity(intent);
            });
        }
    }
}
