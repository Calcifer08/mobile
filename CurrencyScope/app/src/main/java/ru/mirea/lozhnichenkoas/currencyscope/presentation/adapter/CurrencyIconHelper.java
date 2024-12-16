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
            case "AUD":
                return R.drawable.aud;
            case "AZN":
                return R.drawable.azn;
            case "GBP":
                return R.drawable.gbp;
            case "AMD":
                return R.drawable.amd;
            case "BYN":
                return R.drawable.byn;
            case "BGN":
                return R.drawable.bgn;
            case "BRL":
                return R.drawable.brl;
            case "HUF":
                return R.drawable.huf;
            case "VND":
                return R.drawable.vnd;
            case "HKD":
                return R.drawable.hkd;
            case "GEL":
                return R.drawable.gel;
            case "DKK":
                return R.drawable.dkk;
            case "AED":
                return R.drawable.aed;
            case "USD":
                return R.drawable.usd;
            case "EUR":
                return R.drawable.eur;
            case "EGP":
                return R.drawable.egp;
            case "INR":
                return R.drawable.inr;
            case "IDR":
                return R.drawable.idr;
            case "KZT":
                return R.drawable.kzt;
            case "CAD":
                return R.drawable.cad;
            case "QAR":
                return R.drawable.qar;
            case "KGS":
                return R.drawable.kgs;
            case "CNY":
                return R.drawable.cny;
            case "MDL":
                return R.drawable.mdl;
            case "NZD":
                return R.drawable.nzd;
            case "NOK":
                return R.drawable.nok;
            case "PLN":
                return R.drawable.pln;
            case "RON":
                return R.drawable.ron;
            case "SGD":
                return R.drawable.sgd;
            case "TJS":
                return R.drawable.tjs;
            case "THB":
                return R.drawable.thb;
            case "TRY":
                return R.drawable.resource_try;
            case "TMT":
                return R.drawable.tmt;
            case "UZS":
                return R.drawable.uzs;
            case "UAH":
                return R.drawable.uah;
            case "CZK":
                return R.drawable.czk;
            case "SEK":
                return R.drawable.sek;
            case "CHF":
                return R.drawable.chf;
            case "RSD":
                return R.drawable.rsd;
            case "ZAR":
                return R.drawable.zar;
            case "KRW":
                return R.drawable.krw;
            case "JPY":
                return R.drawable.jpy;
            default:
                return R.drawable.default_currency;
        }
    }
}
