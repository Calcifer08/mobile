package ru.mirea.lozhnichenkoas.domain.repositories;

import java.util.List;

import ru.mirea.lozhnichenkoas.domain.models.Currency;
import ru.mirea.lozhnichenkoas.domain.repositories.callback.NetworkCallback;

public interface CurrencyRepository {
    void getAllCurrencies(NetworkCallback<List<Currency>> callback);
    void addFavoriteCurrency(String currencyCode);
    void removeFavoriteCurrency(String currencyCode);
    List<String> getFavoriteCurrencies();
}
