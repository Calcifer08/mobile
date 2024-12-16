package ru.mirea.lozhnichenkoas.domain.usecases;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ru.mirea.lozhnichenkoas.domain.models.Currency;
import ru.mirea.lozhnichenkoas.domain.repositories.CurrencyRepository;
import ru.mirea.lozhnichenkoas.domain.repositories.callback.NetworkCallback;

public class GetFavoriteCurrenciesUseCase {
    private CurrencyRepository currencyRepository;

    public GetFavoriteCurrenciesUseCase(CurrencyRepository currencyRepository){
        this.currencyRepository = currencyRepository;
    }

    public void execute(final NetworkCallback<List<Currency>> callback){
        final List<Currency> favoriteCurrencyList = new ArrayList<>();
        currencyRepository.getAllCurrencies(new NetworkCallback<List<Currency>>() {
            @Override
            public void onResponse(List<Currency> result) {
                List<String> favoriteNameCurrencies = currencyRepository.getFavoriteCurrencies();

                Set<String> favoriteNameSet = new HashSet<>(favoriteNameCurrencies);

                for (Currency currency : result){
                    if (favoriteNameSet.contains(currency.getCharCode())) {
                        favoriteCurrencyList.add(currency);
                    }
                }

                callback.onResponse(favoriteCurrencyList);
            }

            @Override
            public void onFailure(Throwable throwable) {
                callback.onFailure(throwable);
            }
        });
    }
}
