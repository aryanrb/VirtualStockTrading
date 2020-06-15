package com.aryan.virtualtrading.api;

import com.aryan.virtualtrading.models.TradeModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface TradeAPI {
    @POST("trades")
    Call<Void> addTrade(@Header("Authorization")String token, @Body TradeModel trade);

    @GET("trades/{uid}")
    Call<List<TradeModel>> getTradeHistory(@Header("Authorization")String token, @Path("uid") String id);

}
