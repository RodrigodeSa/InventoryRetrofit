package com.rodrigosa.estoqueretrofit.repository;

import android.content.Context;

import com.rodrigosa.estoqueretrofit.asynctask.BaseAsyncTask;
import com.rodrigosa.estoqueretrofit.database.InventoryDatabase;
import com.rodrigosa.estoqueretrofit.database.dao.ProductDAO;
import com.rodrigosa.estoqueretrofit.model.Product;
import com.rodrigosa.estoqueretrofit.retrofit.InventoryRetrofit;
import com.rodrigosa.estoqueretrofit.retrofit.callback.CallbackNoReturn;
import com.rodrigosa.estoqueretrofit.retrofit.callback.CallbackReturn;
import com.rodrigosa.estoqueretrofit.retrofit.service.ProductService;

import java.util.List;

import retrofit2.Call;

public class ProductRepository {

    private final ProductDAO dao;
    private final ProductService service;

    public ProductRepository(Context context) {
        InventoryDatabase db = InventoryDatabase.getInstance(context);
        dao = db.getProductDAO();
        service = new InventoryRetrofit().getProdutoService();
    }

    public void searchProducts(LoadedData<List<Product>> callback) {
        searchInternalProducts(callback);
    }

    private void searchInternalProducts(LoadedData<List<Product>> callback) {
        new BaseAsyncTask<>(dao::searchAll, result -> {
            callback.whenSuccess(result);
            searchProductsInApi(callback);
        }).execute();
    }

    private void searchProductsInApi(LoadedData<List<Product>> callback) {
        Call<List<Product>> call = service.searchAll();
        call.enqueue(new CallbackReturn<>(
                new CallbackReturn.ReturnCallback<List<Product>>() {
                    @Override
                    public void whenSuccess(List<Product> result) {
                        updateInternal(result, callback);
                    }

                    @Override
                    public void whenFail(String error) {
                        callback.whenFail(error);
                    }
                }));
    }

    private void updateInternal(List<Product> products, LoadedData<List<Product>> callback) {
        new BaseAsyncTask<>(() -> {
            dao.save(products);
            return dao.searchAll();
        }, callback::whenSuccess).execute();
    }

    public void save(Product product, LoadedData<Product> callback) {
        saveInApi(product, callback);
    }

    private void saveInApi(Product product, LoadedData<Product> callback) {
        Call<Product> call = service.save(product);
        call.enqueue(new CallbackReturn<>(
                new CallbackReturn.ReturnCallback<Product>() {
                    @Override
                    public void whenSuccess(Product result) {
                        internalSave(result, callback);
                    }

                    @Override
                    public void whenFail(String error) {
                        callback.whenFail(error);
                    }
                }));
    }

    private void internalSave(Product product, LoadedData<Product> callback) {
        new BaseAsyncTask<>(() -> {
            long id = dao.save(product);
            return dao.searchProduct(id);
        }, callback::whenSuccess).execute();
    }

    public void edit(Product product, LoadedData<Product> callback) {
        Call<Product> call = service.edit(product.getId(), product);
        call.enqueue(new CallbackReturn<>(
                new CallbackReturn.ReturnCallback<Product>() {
                    @Override
                    public void whenSuccess(Product result) {
                        new BaseAsyncTask<>(() -> {
                            dao.update(product);
                            return product;
                        }, callback::whenSuccess).execute();
                    }

                    @Override
                    public void whenFail(String error) {
                        callback.whenFail(error);
                    }
                }));
    }

    public void remove(Product product, LoadedData<Void> callback) {
        removeInApi(product, callback);
    }

    private void removeInApi(Product product, LoadedData<Void> callback) {
        Call<Void> call = service.remove(product.getId());
        call.enqueue(new CallbackNoReturn(
                new CallbackNoReturn.ReturnCallback() {
                    @Override
                    public void whenSuccess() {
                        removeInternal(product, callback);
                    }

                    @Override
                    public void whenFail(String error) {
                        callback.whenFail(error);
                    }
                }));
    }

    private void removeInternal(Product product, LoadedData<Void> callback) {
        new BaseAsyncTask<>(() -> {
            dao.remove(product);
            return null;
        }, callback::whenSuccess).execute();
    }

    public interface LoadedData<T> {
        void whenSuccess(T resultado);

        void whenFail(String erro);
    }

}
