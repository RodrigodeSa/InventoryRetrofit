package com.rodrigosa.estoqueretrofit.retrofit;

import com.rodrigosa.estoqueretrofit.retrofit.service.ProductService;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InventoryRetrofit {

    private static final String URL_BASE = "http://192.168.20.249:8080/";
    private final ProductService productService;

    public InventoryRetrofit() {
        OkHttpClient client = configureClient();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL_BASE)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        productService = retrofit.create(ProductService.class);
    }

    private OkHttpClient configureClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();
    }

    public ProductService getProdutoService() {
        return productService;
    }

}
