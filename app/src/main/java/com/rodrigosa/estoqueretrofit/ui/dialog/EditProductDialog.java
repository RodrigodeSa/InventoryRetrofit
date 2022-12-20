package com.rodrigosa.estoqueretrofit.ui.dialog;

import android.content.Context;

import com.rodrigosa.estoqueretrofit.model.Product;

public class EditProductDialog extends ProductFormDialog {

    private static final String TITLE = "Editing product";
    private static final String POSITIVE_BOTTON_TITLE = "Edit";

    public EditProductDialog(Context context, Product product, ConfirmListener listener) {
        super(context, TITLE, POSITIVE_BOTTON_TITLE, listener, product);
    }
}
