package ru.mirea.lozhnichenkoas.domain.usecases;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.lozhnichenkoas.domain.models.Currency;
import ru.mirea.lozhnichenkoas.domain.repositories.CurrencyRepository;

public class SearchCurrencyByVoiceUseCase {
    private CurrencyRepository currencyRepository;

    public SearchCurrencyByVoiceUseCase(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    public List<Currency> execute(String currencyName) {
        List<Currency> currencies = currencyRepository.getAllCurrencies();
        List<Currency> result = new ArrayList<>();

        String lowerCaseQuery = currencyName.toLowerCase();
        for (Currency currency : currencies) {
            if (currency.getName().toLowerCase().contains(lowerCaseQuery)) {
                result.add(currency);
            }
        }

        return result;
    }
}
