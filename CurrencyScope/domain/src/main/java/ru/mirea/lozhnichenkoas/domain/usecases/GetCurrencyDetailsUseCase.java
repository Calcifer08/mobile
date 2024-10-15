package ru.mirea.lozhnichenkoas.domain.usecases;

import java.util.List;

import ru.mirea.lozhnichenkoas.domain.models.Currency;
import ru.mirea.lozhnichenkoas.domain.repositories.CurrencyRepository;

public class GetCurrencyDetailsUseCase {
    private CurrencyRepository currencyRepository;

    public GetCurrencyDetailsUseCase(CurrencyRepository currencyRepository){
        this.currencyRepository = currencyRepository;
    }

    public Currency execute(String currencyCode){
        List<Currency> currencies =  currencyRepository.getAllCurrencies();
        for (Currency currency : currencies) {
            if (currency.getCharCode().equals(currencyCode)) {
                return currency;
            }
        }
        return null;
    }
}
