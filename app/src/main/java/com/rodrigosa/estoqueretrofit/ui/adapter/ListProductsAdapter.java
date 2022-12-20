package com.rodrigosa.estoqueretrofit.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rodrigosa.estoqueretrofit.R;
import com.rodrigosa.estoqueretrofit.model.Product;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListProductsAdapter extends RecyclerView.Adapter<ListProductsAdapter.ViewHolder> {

    private final OnItemClickListener onItemClickListener;
    private OnItemClickRemoveContextMenuListener onItemClickRemoveContext = (position, productRemoved) -> {
    };
    private final Context context;
    private final List<Product> products = new ArrayList<>();

    public ListProductsAdapter(Context context, OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        this.context = context;
    }

    public void setOnItemClickRemoveContext(OnItemClickRemoveContextMenuListener onItemClickRemoveContext) {
        this.onItemClickRemoveContext = onItemClickRemoveContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewCreated = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ViewHolder(viewCreated);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = products.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void update(List<Product> products) {
        notifyItemRangeRemoved(0, this.products.size());
        this.products.clear();
        this.products.addAll(products);
        this.notifyItemRangeInserted(0, this.products.size());
    }

    public void adds(Product... products) {
        int currentSize = this.products.size();
        Collections.addAll(this.products, products);
        int newSize = this.products.size();
        notifyItemRangeInserted(currentSize, newSize);
    }

    public void edit(int position, Product product) {
        products.set(position, product);
        notifyItemChanged(position);
    }

    public void remove(int position) {
        products.remove(position);
        notifyItemRemoved(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView idField;
        private final TextView nameField;
        private final TextView priceField;
        private final TextView quantityField;
        private Product product;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            idField = itemView.findViewById(R.id.product_item_id);
            nameField = itemView.findViewById(R.id.product_item_name);
            priceField = itemView.findViewById(R.id.product_item_price);
            quantityField = itemView.findViewById(R.id.product_item_quantity);
            configureItemClick(itemView);
            configureContextMenu(itemView);
        }

        private void configureContextMenu(@NonNull View itemView) {
            itemView.setOnCreateContextMenuListener((menu, v, menuInfo) -> {
                new MenuInflater(context).inflate(R.menu.product_list_menu, menu);
                menu.findItem(R.id.menu_list_products_remove).setOnMenuItemClickListener(
                        item -> {
                            int productPosition = getAdapterPosition();
                            onItemClickRemoveContext.onItemClick(productPosition, product);
                            return true;
                        });
            });
        }

        private void configureItemClick(@NonNull View itemView) {
            itemView.setOnClickListener(v -> onItemClickListener.onItemClick(getAdapterPosition(), product));
        }

        void bind(Product product) {
            this.product = product;
            idField.setText(String.valueOf(product.getId()));
            nameField.setText(product.getName());
            priceField.setText(formatForCurrency(product.getPrice()));
            quantityField.setText(String.valueOf(product.getQuantity()));
        }

        private String formatForCurrency(BigDecimal value) {
            NumberFormat formatter = NumberFormat.getCurrencyInstance();
            return formatter.format(value);
        }

    }

    public interface OnItemClickListener {
        void onItemClick(int position, Product product);
    }

    public interface OnItemClickRemoveContextMenuListener {
        void onItemClick(int position, Product productRemoved);
    }

}
