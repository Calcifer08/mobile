package ru.mirea.lozhnichenkoas.data.sources.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FavoriteCurrencyDao {
    @Query("SELECT currencyCode FROM favoritecurrencyentity")
    List<String> getAllFavoriteCurrencies();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFavoriteCurrency(FavoriteCurrencyEntity favoriteCurrency);

    @Query("DELETE FROM FavoriteCurrencyEntity WHERE currencyCode = :currencyCode")
    void deleteFavoriteCurrency(String currencyCode);
}
