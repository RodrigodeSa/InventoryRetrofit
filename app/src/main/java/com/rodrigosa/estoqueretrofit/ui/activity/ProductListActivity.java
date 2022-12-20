package com.rodrigosa.estoqueretrofit.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rodrigosa.estoqueretrofit.R;
import com.rodrigosa.estoqueretrofit.model.Product;
import com.rodrigosa.estoqueretrofit.repository.ProductRepository;
import com.rodrigosa.estoqueretrofit.ui.adapter.ListProductsAdapter;
import com.rodrigosa.estoqueretrofit.ui.dialog.EditProductDialog;
import com.rodrigosa.estoqueretrofit.ui.dialog.SaveProductDialog;

import java.util.List;

public class ProductListActivity extends AppCompatActivity {

    private static final String TITlE_APPBAR = "Product list";
    private static final String MENSSAGE_ERROR_SEARCH_PRODUCTS = "Unable to load new products";
    private static final String MENSSAGE_ERROR_REMOVAL = "Unable to remove product";
    private static final String MENSSAGE_ERROR_SAVE = "Unable to save product";
    private static final String MENSSAGE_ERROR_EDITION = "Unable to edit product";
    private ListProductsAdapter adapter;
    private ProductRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        setTitle(TITlE_APPBAR);

        configureProductsList();
        configureFabSaveProduct();

        repository = new ProductRepository(this);

        searchProducts();
    }

    private void searchProducts() {
        repository.searchProducts(new ProductRepository.LoadedData<List<Product>>() {
            @Override
            public void whenSuccess(List<Product> newProducts) {
                adapter.update(newProducts);
            }

            @Override
            public void whenFail(String error) {
                showError(MENSSAGE_ERROR_SEARCH_PRODUCTS);
            }
        });
    }

    private void showError(String menssage) {
        Toast.makeText(this, menssage, Toast.LENGTH_LONG).show();
    }

    private void configureProductsList() {
        RecyclerView productsList = findViewById(R.id.activity_products_list);
        adapter = new ListProductsAdapter(this, this::openFormEditProduct);
        productsList.setAdapter(adapter);
        adapter.setOnItemClickRemoveContext(this::remove);
    }

    private void remove(int position, Product chosenProduct) {
        repository.remove(chosenProduct,
                new ProductRepository.LoadedData<Void>() {
                    @Override
                    public void whenSuccess(Void result) {
                        adapter.remove(position);
                    }

                    public void whenFail(String error) {
                        showError(MENSSAGE_ERROR_REMOVAL);
                    }
                });
    }

    private void configureFabSaveProduct() {
        FloatingActionButton fabAddProduct = findViewById(R.id.activity_list_products_fab_add_product);
        fabAddProduct.setOnClickListener(v -> openFormSaveProduct());
    }

    private void openFormSaveProduct() {
        new SaveProductDialog(this, this::save).popsUp();
    }

    private void save(Product createdProduct) {
        repository.save(createdProduct, new ProductRepository.LoadedData<Product>() {
            @Override
            public void whenSuccess(Product savedProduct) {
                adapter.adds(savedProduct);
            }

            public void whenFail(String error) {
                showError(MENSSAGE_ERROR_SAVE);
            }
        });
    }


    private void openFormEditProduct(int position, Product product) {
        new EditProductDialog(this, product, createdProduct ->
                edit(position, createdProduct)).popsUp();
    }

    private void edit(int position, Product createdProduct) {
        repository.edit(createdProduct,
                new ProductRepository.LoadedData<Product>() {
                    public void whenSuccess(Product editedProduct) {
                        adapter.edit(position, editedProduct);
                    }

                    public void whenFail(String error) {
                        showError(MENSSAGE_ERROR_EDITION);
                    }
                });
    }

}