package com.rodrigosa.estoqueretrofit.database.converter;

import androidx.room.TypeConverter;

import java.math.BigDecimal;

public class BigDecimalConverter {

    @TypeConverter
    public Double forDouble(BigDecimal value) {
        return value.doubleValue();
    }

    @TypeConverter
    public BigDecimal forBigDecimal(Double value) {
        if (value != null) {
            return new BigDecimal(value);
        }
        return BigDecimal.ZERO;
    }

}