package ru.mirea.lozhnichenkoas.data.sources.db.providers;

import ru.mirea.lozhnichenkoas.data.sources.db.FavoriteCurrencyDao;
import ru.mirea.lozhnichenkoas.data.sources.db.LocalDatabase;

public interface DatabaseProvider {
    LocalDatabase getDatabaseInstance();
    FavoriteCurrencyDao getFavoriteCurrencyDao();
}
