package ru.mirea.lozhnichenkoas.domain.usecases;

import ru.mirea.lozhnichenkoas.domain.repositories.CurrencyRepository;

public class RemoveFavoriteCurrencyUseCase {
    private CurrencyRepository currencyRepository;

    public RemoveFavoriteCurrencyUseCase(CurrencyRepository currencyRepository){
        this.currencyRepository = currencyRepository;
    }

    public void execute(String currencyCode){
        currencyRepository.removeFavoriteCurrency(currencyCode);
    }
}
