package ru.mirea.lozhnichenkoas.domain.usecases;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.lozhnichenkoas.domain.models.Currency;
import ru.mirea.lozhnichenkoas.domain.repositories.CurrencyRepository;
import ru.mirea.lozhnichenkoas.domain.repositories.callback.NetworkCallback;

public class GetCurrencyDetailsUseCase {
    private CurrencyRepository currencyRepository;

    public GetCurrencyDetailsUseCase(CurrencyRepository currencyRepository){
        this.currencyRepository = currencyRepository;
    }

    public void execute(final String currencyCode, final NetworkCallback<List<Currency>> callback){
        currencyRepository.getAllCurrencies(new NetworkCallback<List<Currency>>() {
            @Override
            public void onResponse(List<Currency> result) {
                for (Currency currency : result) {
                    if (currency.getCharCode().equals(currencyCode)) {
                        List<Currency> currencyList = new ArrayList<>();
                        currencyList.add(currency);
                        callback.onResponse(currencyList);
                        return;
                    }
                }
                callback.onResponse(null);
            }

            @Override
            public void onFailure(Throwable throwable) {
                callback.onFailure(throwable);
            }
        });
    }
}
