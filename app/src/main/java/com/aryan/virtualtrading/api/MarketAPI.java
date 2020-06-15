package com.aryan.virtualtrading.api;

import com.aryan.virtualtrading.models.MarketModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MarketAPI {

    @GET("companies")
    Call<List<MarketModel>> getMarket();

    @GET("companies/{id}")
    Call<MarketModel> getCompanyDetail(@Path("id") String _id);
}
