package com.aryan.virtualtrading.api;

import com.aryan.virtualtrading.models.BalanceModel;
import com.aryan.virtualtrading.models.PortfolioModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface BalanceAPI {

    @POST("balance")
    Call<Void> createBalance(@Header("Authorization")String token, @Body BalanceModel balanceModel);

    @GET("balance/{uid}")
    Call<BalanceModel> getBalanceDetail(@Header("Authorization")String token, @Path("uid") String id);

    @PUT("balance/{uid}")
    Call<Void> updateBalance(@Header("Authorization")String token, @Path("uid") String uid, @Body BalanceModel balance);
}
