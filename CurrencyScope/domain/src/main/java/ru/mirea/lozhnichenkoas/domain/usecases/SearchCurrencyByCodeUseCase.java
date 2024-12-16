package ru.mirea.lozhnichenkoas.domain.usecases;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.lozhnichenkoas.domain.models.Currency;
import ru.mirea.lozhnichenkoas.domain.repositories.CurrencyRepository;
import ru.mirea.lozhnichenkoas.domain.repositories.callback.NetworkCallback;

public class SearchCurrencyByCodeUseCase {
    private CurrencyRepository currencyRepository;

    public SearchCurrencyByCodeUseCase(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    public void execute(final String charCode, final NetworkCallback<List<Currency>> callback) {
        currencyRepository.getAllCurrencies(new NetworkCallback<List<Currency>>() {
            @Override
            public void onResponse(List<Currency> result) {
                List<Currency> currencyList = new ArrayList<>();

                String lowerCharCode = charCode.toLowerCase().trim();
                for (Currency currency : result) {
                    if (currency.getCharCode().toLowerCase().contains(lowerCharCode)) {
                        currencyList.add(currency);
                    }
                }

                callback.onResponse(currencyList);
            }

            @Override
            public void onFailure(Throwable throwable) {
                callback.onFailure(throwable);
            }
        });

    }
}
