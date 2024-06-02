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
import com.example.afinal.DBHelper;
import com.example.afinal.Product;
import com.example.afinal.R;
import com.example.afinal.activity.DetailActivity;
import com.example.afinal.activity.MainActivity;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;
    private Context context;
    private DBHelper dbHelper;
    public ProductAdapter(List<Product> productList,Context context) {
        this.productList = productList;
        this.context = context;
        dbHelper = new DBHelper(context);
    }

    public void setProducts(List<Product> productList) {
        this.productList = productList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        if (productList != null && !productList.isEmpty()) {
            Product product = productList.get(position);
            holder.bind(product);
            final boolean[] isFavorite = {dbHelper.isFavorite(dbHelper.getLoggedInUser(), String.valueOf(product.getId()))};
            holder.favoriteImageView.setImageResource(isFavorite[0] ? R.drawable.ic_favorite : R.drawable.ic_favorite_border);


            holder.favoriteImageView.setOnClickListener(v -> {
                if (isFavorite[0]) {
                    dbHelper.removeFavorite(dbHelper.getLoggedInUser(),String.valueOf(product.getId()));
                    holder.favoriteImageView.setImageResource(R.drawable.ic_favorite_border);
                } else {
                    dbHelper.addFavorite(dbHelper.getLoggedInUser(),String.valueOf(product.getId()));
                    holder.favoriteImageView.setImageResource(R.drawable.ic_favorite);
                }

                isFavorite[0] = !isFavorite[0];
                notifyItemChanged(position);
                // Notify the favorite fragment to update its data
                if (context instanceof MainActivity) {
                    ((MainActivity) context).updateFavoriteFragment();
                }
            });
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("product", product);
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return productList != null ? productList.size() : 0;
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageViewProduct,favoriteImageView;
        public TextView textViewProductName;
        public TextView textViewProductType;
        public TextView textViewProductPrice;


        ProductViewHolder(View itemView) {
            super(itemView);
            imageViewProduct = itemView.findViewById(R.id.IV_product);
            textViewProductName = itemView.findViewById(R.id.TV_name);
            textViewProductType = itemView.findViewById(R.id.TV_type);
            textViewProductPrice = itemView.findViewById(R.id.price);
            favoriteImageView = itemView.findViewById(R.id.imageViewFavorite);

        }

        void bind(final Product product) {
            textViewProductName.setText(product.getName());
            textViewProductType.setText(product.getProduct_type());
            textViewProductPrice.setText("$" + product.getPrice());


            // Load image using Glide
            Glide.with(itemView)
                    .load(product.getImage_link())
                    .placeholder(R.drawable.placeholder)
                    .into(imageViewProduct);

        }
    }
}
