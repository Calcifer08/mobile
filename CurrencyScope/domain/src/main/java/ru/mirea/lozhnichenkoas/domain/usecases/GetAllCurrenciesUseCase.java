package ru.mirea.lozhnichenkoas.domain.usecases;

import java.util.List;

import ru.mirea.lozhnichenkoas.domain.models.Currency;
import ru.mirea.lozhnichenkoas.domain.repositories.CurrencyRepository;

public class GetAllCurrenciesUseCase {
    private CurrencyRepository currencyRepository;

    public GetAllCurrenciesUseCase(CurrencyRepository currencyRepository){
        this.currencyRepository = currencyRepository;
    }

    public List<Currency> execute(){
        return currencyRepository.getAllCurrencies();
    }
}
