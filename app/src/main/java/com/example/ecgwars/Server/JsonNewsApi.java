package com.example.ecgwars.Server;

import com.example.ecgwars.model.ArtcileInfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface JsonNewsApi {

    @GET("v2/top-headlines")
    Call<ArtcileInfo> getNews(
            @Query("apiKey")String apiKey ,
            @Query("category") String category,
            @Query("country") String countyCode,
            @Query("pageSize") String pageSize,
            @Query ("page") String page);

}
