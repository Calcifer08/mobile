package ru.mirea.lozhnichenkoas.domain.repositories;

import java.util.List;

import ru.mirea.lozhnichenkoas.domain.models.Currency;

public interface CurrencyRepository {
    List<Currency> getAllCurrencies();
    void addFavoriteCurrency(String currencyCode);
    void removeFavoriteCurrency(String currencyCode);
    List<String> getFavoriteCurrencies();
}
