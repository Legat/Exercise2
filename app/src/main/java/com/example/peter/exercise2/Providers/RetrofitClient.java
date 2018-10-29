package com.example.peter.exercise2.Providers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "https://api.nytimes.com/svc/topstories/";
    private static Gson gson = new GsonBuilder().setLenient().create();
   /// https://api.nytimes.com/svc/topstories/v2/home.json
    private static Retrofit getRetrofitInstance(){
        return new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
    }

    public static ApiService getApiService(){
        return getRetrofitInstance().create(ApiService.class);
    }
}
