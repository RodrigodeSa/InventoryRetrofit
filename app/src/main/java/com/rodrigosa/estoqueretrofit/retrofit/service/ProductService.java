package com.rodrigosa.estoqueretrofit.retrofit.service;

import com.rodrigosa.estoqueretrofit.model.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ProductService {

    @GET("product")
    Call<List<Product>> searchAll();

    @POST("product")
    Call<Product> save(@Body Product product);

    @PUT("product/{id}")
    Call<Product> edit(@Path("id") long id, @Body Product product);

    @DELETE("product/{id}")
    Call<Void> remove(@Path("id") long id);

}
