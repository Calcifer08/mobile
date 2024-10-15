package ru.mirea.lozhnichenkoas.domain.usecases;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.lozhnichenkoas.domain.models.Currency;
import ru.mirea.lozhnichenkoas.domain.repositories.CurrencyRepository;

public class SearchCurrencyByCodeUseCase {
    private CurrencyRepository currencyRepository;

    public SearchCurrencyByCodeUseCase(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    public List<Currency> execute(String charCode) {
        List<Currency> currencies = currencyRepository.getAllCurrencies();
        List<Currency> results = new ArrayList<>();

        String lowerCharCode = charCode.toLowerCase().trim();
        for (Currency currency : currencies) {
            if (currency.getCharCode().toLowerCase().contains(lowerCharCode)) {
                results.add(currency);
            }
        }

        return results;
    }
}
