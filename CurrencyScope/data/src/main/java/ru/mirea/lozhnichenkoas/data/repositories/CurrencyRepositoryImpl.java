package ru.mirea.lozhnichenkoas.data.repositories;

import java.util.List;

import ru.mirea.lozhnichenkoas.data.sources.NetApi.NetworkApi;
import ru.mirea.lozhnichenkoas.data.sources.db.FavoriteCurrencyDao;
import ru.mirea.lozhnichenkoas.data.sources.db.FavoriteCurrencyEntity;
import ru.mirea.lozhnichenkoas.domain.models.Currency;
import ru.mirea.lozhnichenkoas.domain.repositories.CurrencyRepository;

public class CurrencyRepositoryImpl implements CurrencyRepository {
    private NetworkApi networkApi;
    private FavoriteCurrencyDao favoriteCurrencyDao;
    private List<Currency> currencyList;

    public CurrencyRepositoryImpl(NetworkApi networkApi, FavoriteCurrencyDao favoriteCurrencyDao){
        this.networkApi = networkApi;
        this.favoriteCurrencyDao = favoriteCurrencyDao;
    }

    @Override
    public List<Currency> getAllCurrencies() {
        if (currencyList == null || currencyList.isEmpty()) {
            currencyList = networkApi.getAllCurrencies();
        }
        return currencyList;
    }

    @Override
    public void addFavoriteCurrency(String currencyCode) {
        List<String> favoriteCurrencies = getFavoriteCurrencies();

        if (!favoriteCurrencies.contains(currencyCode)) {
            favoriteCurrencyDao.insertFavoriteCurrency(new FavoriteCurrencyEntity(currencyCode));
        }
    }

    @Override
    public void removeFavoriteCurrency(String currencyCode) {
        favoriteCurrencyDao.deleteFavoriteCurrency(currencyCode);
    }

    @Override
    public List<String> getFavoriteCurrencies() {
        return favoriteCurrencyDao.getAllFavoriteCurrencies();
    }
}
