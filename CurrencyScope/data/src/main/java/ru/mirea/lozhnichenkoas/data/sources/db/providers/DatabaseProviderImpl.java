package ru.mirea.lozhnichenkoas.data.sources.db.providers;

import android.content.Context;

import androidx.room.Room;

import ru.mirea.lozhnichenkoas.data.sources.db.FavoriteCurrencyDao;
import ru.mirea.lozhnichenkoas.data.sources.db.LocalDatabase;

public class DatabaseProviderImpl implements DatabaseProvider{
    private LocalDatabase databaseInstance;

    public DatabaseProviderImpl(Context context){
            databaseInstance = Room.databaseBuilder(context, LocalDatabase.class, "database")
                    .allowMainThreadQueries()
                    .build();
    }
    @Override
    public LocalDatabase getDatabaseInstance() {
        return databaseInstance;
    }

    @Override
    public FavoriteCurrencyDao getFavoriteCurrencyDao() {
        return databaseInstance.favoriteCurrencyDao();
    }
}
