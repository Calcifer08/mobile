package ru.mirea.lozhnichenkoas.domain.usecases;

import java.util.List;

import ru.mirea.lozhnichenkoas.domain.models.Currency;
import ru.mirea.lozhnichenkoas.domain.repositories.CurrencyRepository;
import ru.mirea.lozhnichenkoas.domain.repositories.callback.NetworkCallback;

public class GetAllCurrenciesUseCase {
    private CurrencyRepository currencyRepository;

    public GetAllCurrenciesUseCase(CurrencyRepository currencyRepository){
        this.currencyRepository = currencyRepository;
    }

    public void execute(NetworkCallback<List<Currency>> callback){
        currencyRepository.getAllCurrencies(callback);
    }
}
