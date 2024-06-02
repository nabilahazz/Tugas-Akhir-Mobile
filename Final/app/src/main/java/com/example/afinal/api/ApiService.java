package com.example.afinal.api;

import com.example.afinal.Product;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
public interface ApiService {
    @GET("products.json?brand=maybelline&_x_tr_sch=http&_x_tr_sl=en&_x_tr_tl=id&_x_tr_hl=id&_x_tr_pto=tc1")
    Call<List<Product>> getProduct();
}
