package com.example.afinal.activity;// DetailActivity.java
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.afinal.Product;
import com.example.afinal.R;

public class DetailActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private ProgressBar progressBar;
    private TextView productName;
    private TextView productType;
    private TextView productPrice;
    private ImageView productImage;
    private TextView productRating;
    private TextView productDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Inisialisasi view
        btnBack = findViewById(R.id.btnBack);
        progressBar = findViewById(R.id.progressBar);
        productName = findViewById(R.id.product_name);
        productType = findViewById(R.id.product_type);
        productPrice = findViewById(R.id.product_price);
        productImage = findViewById(R.id.product_image);
        productRating = findViewById(R.id.product_rating);
        productDescription = findViewById(R.id.product_description);

        Intent intent = getIntent();
        if (intent != null) {
            Product product = intent.getParcelableExtra("product");
            if (product != null) {
                productName.setText(product.getName());
                productType.setText(product.getProduct_type());
                productPrice.setText(String.valueOf(product.getPrice()));
                productDescription.setText(product.getDescription());
                productRating.setText(product.getRating());

                Glide.with(this).load(product.getImage_link()).into(productImage);
            } else {
                Toast.makeText(this, "No Product data found", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Intent is null", Toast.LENGTH_SHORT).show();
        }


        // Set listener untuk tombol kembali
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mengarahkan ke MainActivity saat tombol kembali diklik
                Intent intent = new Intent(DetailActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Opsional, jika Anda ingin menutup DetailActivity setelah pindah ke MainActivity
            }
        });

    }
}
