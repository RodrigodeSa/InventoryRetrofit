package com.rodrigosa.estoqueretrofit.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.rodrigosa.estoqueretrofit.database.converter.BigDecimalConverter;
import com.rodrigosa.estoqueretrofit.database.dao.ProductDAO;
import com.rodrigosa.estoqueretrofit.model.Product;

@Database(entities = {Product.class}, version = 1, exportSchema = false)
@TypeConverters(value = {BigDecimalConverter.class})
public abstract class InventoryDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "inventory.db";

    public abstract ProductDAO getProductDAO();

    public static InventoryDatabase getInstance(Context context) {
        return Room.databaseBuilder(context, InventoryDatabase.class, DATABASE_NAME).build();
    }
}
