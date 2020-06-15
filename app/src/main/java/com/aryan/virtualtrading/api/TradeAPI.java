package com.aryan.virtualtrading.api;

import com.aryan.virtualtrading.models.TradeModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface TradeAPI {
    @POST("trades")
    Call<Void> addTrade(@Header("Authorization")String token, @Body TradeModel trade);

}
