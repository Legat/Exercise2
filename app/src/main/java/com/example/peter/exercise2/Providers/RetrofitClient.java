package com.example.peter.exercise2.Providers;

import com.example.peter.exercise2.TokenInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "https://api.nytimes.com/svc/topstories/";
    private static Gson gson = new GsonBuilder().setLenient().create();
    OkHttpClient client = new OkHttpClient.Builder()
                 .addInterceptor(new TokenInterceptor())
                 .build();
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

    private static Retrofit getRetrofitObservable(){
        return new Retrofit.Builder()
                           .addConverterFactory(GsonConverterFactory.create(gson))
                           .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                           .baseUrl(BASE_URL)
                           .build();

    }

    public static ApiService getObservableApi(){
        return getRetrofitObservable().create(ApiService.class);
    }
}
