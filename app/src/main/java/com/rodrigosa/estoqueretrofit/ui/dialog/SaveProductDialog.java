package com.rodrigosa.estoqueretrofit.ui.dialog;

import android.content.Context;

public class SaveProductDialog extends ProductFormDialog {

    private static final String TITLE = "Saving product";
    private static final String POSITIVE_BUTTON_TITLE = "Save";

    public SaveProductDialog(Context context, ConfirmListener listener) {
        super(context, TITLE, POSITIVE_BUTTON_TITLE, listener);
    }

}
