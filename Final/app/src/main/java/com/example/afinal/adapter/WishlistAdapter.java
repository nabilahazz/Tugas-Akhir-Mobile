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
import com.example.afinal.R;
import com.example.afinal.activity.DetailActivity;

import java.util.List;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ViewHolder> {

    private List<Product> favoriteProducts;
    private Context context;

    public WishlistAdapter(Context context, List<Product> favoriteProducts) {
        this.context = context;
        this.favoriteProducts = favoriteProducts;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wishlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product destination = favoriteProducts.get(position);
        holder.nameTextView.setText(destination.getName());
        holder.descTextView.setText(destination.getDescription());
        Glide.with(context).load(destination.getImage_link()).into(holder.imageView);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("destination", destination);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return favoriteProducts.size();
    }

    public void updateData(List<Product> newFavorites) {
        this.favoriteProducts = newFavorites;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView nameTextView,descTextView,price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.IV_product);
            nameTextView = itemView.findViewById(R.id.TV_name);
            descTextView= itemView.findViewById(R.id.TV_type);
            price= itemView.findViewById(R.id.price);
        }
    }
}

