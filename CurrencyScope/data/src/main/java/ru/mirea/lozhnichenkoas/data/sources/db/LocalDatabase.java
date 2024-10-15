package ru.mirea.lozhnichenkoas.data.sources.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {FavoriteCurrencyEntity.class}, version = 1)

public abstract class LocalDatabase extends RoomDatabase {
    public abstract FavoriteCurrencyDao favoriteCurrencyDao();
}
