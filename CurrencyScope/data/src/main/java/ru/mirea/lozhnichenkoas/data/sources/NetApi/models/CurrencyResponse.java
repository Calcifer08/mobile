package ru.mirea.lozhnichenkoas.data.sources.NetApi.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ru.mirea.lozhnichenkoas.domain.models.Currency;

public class CurrencyResponse {
    @SerializedName("Valute")
    private Map<String, CurrencyDetails> valute;

    public List<Currency> mapToCurrency(){
        List<Currency> currencyList = new ArrayList<>();
        for (Map.Entry<String, CurrencyDetails> entry : valute.entrySet()) {
            CurrencyDetails currencyDetails = entry.getValue();
            currencyList.add(new Currency(
                    currencyDetails.CharCode,
                    currencyDetails.Name,
                    currencyDetails.Nominal,
                    currencyDetails.Value,
                    currencyDetails.Previous));
        }
        return currencyList;
    }

    public static class CurrencyDetails {
        @SerializedName("CharCode")
        String CharCode;

        @SerializedName("Nominal")
        int Nominal;

        @SerializedName("Name")
        String Name;

        @SerializedName("Value")
        double Value;

        @SerializedName("Previous")
        double Previous;
    }
}
