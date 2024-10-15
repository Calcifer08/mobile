package ru.mirea.lozhnichenkoas.data.sources.NetApi.models;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

import ru.mirea.lozhnichenkoas.domain.models.Currency;

public class CurrencyResponse {
    @SerializedName("Valute")
    private Map<String, Currency> Valute;

    public Map<String, Currency> getValute() {
        return Valute;
    }
}
