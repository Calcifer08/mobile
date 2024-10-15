package ru.mirea.lozhnichenkoas.domain.usecases;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ru.mirea.lozhnichenkoas.domain.models.Currency;
import ru.mirea.lozhnichenkoas.domain.repositories.CurrencyRepository;

public class GetFavoriteCurrenciesUseCase {
    private CurrencyRepository currencyRepository;

    public GetFavoriteCurrenciesUseCase(CurrencyRepository currencyRepository){
        this.currencyRepository = currencyRepository;
    }

    public List<Currency> execute(){
        List<Currency> favoriteCurrencyList = new ArrayList<>();
        List<Currency> currencyList = currencyRepository.getAllCurrencies();
        List<String> favoriteNameCurrencies = currencyRepository.getFavoriteCurrencies();

        Set<String> favoriteNameSet = new HashSet<>(favoriteNameCurrencies);

        for (Currency currency : currencyList){
            if (favoriteNameSet.contains(currency.getCharCode())) {
                favoriteCurrencyList.add(currency);
            }
        }

        return favoriteCurrencyList;
    }
}
