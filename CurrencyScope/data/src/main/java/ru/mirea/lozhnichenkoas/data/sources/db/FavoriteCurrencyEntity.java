package ru.mirea.lozhnichenkoas.data.sources.db;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class FavoriteCurrencyEntity {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    public String currencyCode;

    public FavoriteCurrencyEntity(@NonNull String currencyCode){
        this.currencyCode = currencyCode;
    }

}
