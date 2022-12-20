package com.rodrigosa.estoqueretrofit.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.math.BigDecimal;

@Entity
public class Product {

    @PrimaryKey(autoGenerate = true)
    private final long id;
    private final String name;
    private final BigDecimal price;
    private final int quantity;

    public Product(long id, String name, BigDecimal price, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price.setScale(2, BigDecimal.ROUND_HALF_EVEN);
    }

    public int getQuantity() {
        return quantity;
    }

}
