package ru.mirea.lozhnichenkoas.domain.usecases;

import ru.mirea.lozhnichenkoas.domain.repositories.CurrencyRepository;

public class AddFavoriteCurrencyUseCase {
    private CurrencyRepository currencyRepository;

    public AddFavoriteCurrencyUseCase(CurrencyRepository currencyRepository){
        this.currencyRepository = currencyRepository;
    }

    public void execute(String currencyCode){
        currencyRepository.addFavoriteCurrency(currencyCode);
    }
}
