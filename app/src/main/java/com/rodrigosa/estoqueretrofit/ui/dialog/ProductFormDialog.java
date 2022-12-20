package com.rodrigosa.estoqueretrofit.ui.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.textfield.TextInputLayout;
import com.rodrigosa.estoqueretrofit.R;
import com.rodrigosa.estoqueretrofit.model.Product;

import java.math.BigDecimal;

abstract public class ProductFormDialog {

    private final String title;
    private final String positiveButtonTitle;
    private final ConfirmListener listener;
    private final Context context;
    private static final String NEGATIVE_BUTTON_TITLE = "Cancel";
    private Product product;

    ProductFormDialog(Context context, String title, String positiveButtonTitle, ConfirmListener listener) {
        this.title = title;
        this.positiveButtonTitle = positiveButtonTitle;
        this.listener = listener;
        this.context = context;
    }

    ProductFormDialog(Context context, String title, String positiveButtonTitle, ConfirmListener listener, Product product) {
        this(context, title, positiveButtonTitle, listener);
        this.product = product;
    }

    public void popsUp() {
        @SuppressLint("InflateParams") View viewCreated = LayoutInflater.from(context)
                .inflate(R.layout.product_form, null);
        fillForm(viewCreated);
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setView(viewCreated)
                .setPositiveButton(positiveButtonTitle, (dialog, which) -> {
                    EditText nameField = getEditText(viewCreated, R.id.form_product_name);
                    EditText priceField = getEditText(viewCreated, R.id.form_product_price);
                    EditText quantityField = getEditText(viewCreated, R.id.form_product_quantity);
                    createProduct(nameField, priceField, quantityField);
                }).setNegativeButton(NEGATIVE_BUTTON_TITLE, null).show();
    }

    @SuppressLint("SetTextI18n")
    private void fillForm(View ViewCreated) {
        if (product != null) {
            TextView idField = ViewCreated.findViewById(R.id.form_product_id);
            idField.setText(String.valueOf(product.getId()));
            idField.setVisibility(View.VISIBLE);
            EditText nameField = getEditText(ViewCreated, R.id.form_product_name);
            nameField.setText(product.getName());
            EditText priceField = getEditText(ViewCreated, R.id.form_product_price);
            priceField.setText(product.getPrice().toString());
            EditText quantityField = getEditText(ViewCreated, R.id.form_product_quantity);
            quantityField.setText(String.valueOf(product.getQuantity()));
        }
    }

    private void createProduct(EditText nameField, EditText priceField, EditText quantityField) {
        String name = nameField.getText().toString();
        BigDecimal price = convertPrice(priceField);
        int quantity = convertQuantity(quantityField);
        long id = fillId();
        Product product = new Product(id, name, price, quantity);
        listener.whenConfirmed(product);
    }

    private long fillId() {
        if (product != null) {
            return product.getId();
        }
        return 0;
    }

    private BigDecimal convertPrice(EditText campoPreco) {
        try {
            return new BigDecimal(campoPreco.getText().toString());
        } catch (NumberFormatException ignored) {
            return BigDecimal.ZERO;
        }
    }

    private int convertQuantity(EditText amountField) {
        try {
            return Integer.parseInt(amountField.getText().toString());
        } catch (NumberFormatException ignored) {
            return 0;
        }
    }

    private EditText getEditText(View viewCreated, int idTextInputLayout) {
        TextInputLayout textInputLayout = viewCreated.findViewById(idTextInputLayout);
        return textInputLayout.getEditText();
    }

    public interface ConfirmListener {
        void whenConfirmed(Product product);
    }

}
