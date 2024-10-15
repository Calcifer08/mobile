package ru.mirea.lozhnichenkoas.data.sources.NetApi;

import retrofit2.Call;
import retrofit2.http.GET;
import ru.mirea.lozhnichenkoas.data.sources.NetApi.models.CurrencyResponse;

public interface CurrencyApi {
    @GET("daily_json.js")
    Call<CurrencyResponse> getCurrencies();
}
