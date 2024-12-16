package ru.mirea.lozhnichenkoas.data.repositories;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.mirea.lozhnichenkoas.data.sources.NetApi.NetworkApi;
import ru.mirea.lozhnichenkoas.data.sources.NetApi.RetrofitInstance;
import ru.mirea.lozhnichenkoas.data.sources.NetApi.models.CurrencyResponse;
import ru.mirea.lozhnichenkoas.data.sources.db.FavoriteCurrencyDao;
import ru.mirea.lozhnichenkoas.data.sources.db.FavoriteCurrencyEntity;
import ru.mirea.lozhnichenkoas.domain.models.Currency;
import ru.mirea.lozhnichenkoas.domain.repositories.CurrencyRepository;
import ru.mirea.lozhnichenkoas.domain.repositories.callback.NetworkCallback;

public class CurrencyRepositoryImpl implements CurrencyRepository {
    private NetworkApi networkApi;
    private FavoriteCurrencyDao favoriteCurrencyDao;
    private List<Currency> currencyList = new ArrayList<>();

    public CurrencyRepositoryImpl(FavoriteCurrencyDao favoriteCurrencyDao){
        this.favoriteCurrencyDao = favoriteCurrencyDao;
        this.networkApi = RetrofitInstance.getNetworkApi();
    }

    @Override
    public void getAllCurrencies(NetworkCallback<List<Currency>> callback) {
        if (currencyList == null || currencyList.isEmpty()) {
            Call<CurrencyResponse> call = networkApi.getAllCurrencies();
            call.enqueue(new Callback<CurrencyResponse>() {
                @Override
                public void onResponse(Call<CurrencyResponse> call, Response<CurrencyResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        currencyList = response.body().mapToCurrency();
                        callback.onResponse(currencyList);
                    } else {
                        callback.onFailure(new Throwable("Ошибка при получении данных"));
                    }
                }

                @Override
                public void onFailure(Call<CurrencyResponse> call, Throwable t) {
                    callback.onFailure(t);
                }
            });

        } else {
            callback.onResponse(currencyList);
        }
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
