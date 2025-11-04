package com.example.myapplication.data.remote;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import com.example.myapplication.data.model.ProductResponse;

public interface BarcodeApiService {
    @GET("api/v0/product/{barcode}.json")
    Call<ProductResponse> getProduct(@Path("barcode") String barcode);
}
