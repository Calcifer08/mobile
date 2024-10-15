package ru.mirea.lozhnichenkoas.currencyscope.presentation.adapter;

import java.util.List;

import ru.mirea.lozhnichenkoas.currencyscope.R;
import ru.mirea.lozhnichenkoas.domain.models.Currency;

public class CurrencyIconHelper {
    public static void setIconsCurrencies(List<Currency> currencyList) {
        for (Currency currency : currencyList) {
            currency.setIconId(getIconCurrency(currency.getCharCode()));
        }
    }

    private static int getIconCurrency(String currencyCode){
        switch (currencyCode) {
            case "USD":
                return R.drawable.usd;
            case "GBR":
                return R.drawable.gbr;
            default:
                return R.drawable.default_currency;
        }
    }
}
