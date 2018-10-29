package com.example.peter.exercise2.Providers;

import com.example.peter.exercise2.Models.News;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("v2/{filter}")
    Call<News> getItemNews(@Path("filter") String filter, @Query("api-key")String api_key);
}
