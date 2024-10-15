package ru.mirea.lozhnichenkoas.data.sources.NetApi;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.lozhnichenkoas.domain.models.Currency;

public class NetworkApi {
    public List<Currency> getAllCurrencies() {
        List<Currency> currencies = new ArrayList<>();
        currencies.add(new Currency("USD", "Австралийский доллар", 1, 64.7887, 65.5296));
        currencies.add(new Currency("GBR", "Азербайджанский манат", 1, 56.5109, 57.1996));
        return currencies;
    }
}