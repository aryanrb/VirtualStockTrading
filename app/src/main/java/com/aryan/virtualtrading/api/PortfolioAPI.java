package com.aryan.virtualtrading.api;


import com.aryan.virtualtrading.models.PortfolioModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PortfolioAPI {

    @GET("portfolio/{uid}")
    Call<List<PortfolioModel>> getMyPortfolio(@Header("Authorization")String token, @Path("uid") String id);

    @POST("portfolio")
    Call<Void> addPortfolio(@Header("Authorization")String token, @Body PortfolioModel portfolio);

    @GET("portfolio/{uid}/{cid}")
    Call<PortfolioModel> getMyPortfolioCo(@Header("Authorization")String token, @Path("uid") String uid, @Path("cid") String cid);

    @PUT("portfolio/{uid}/{cid}")
    Call<Void> updatePortfolio(@Header("Authorization")String token, @Path("uid") String uid, @Path("cid") String cid, @Body PortfolioModel portfolio);
}
