package com.example.peter.exercise2;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class TokenInterceptor implements Interceptor {
  private static final String API_KEY = "d94b328606074836a7618073303334da";
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request().newBuilder()
                .header("key",API_KEY)
                .build();
        return chain.proceed(request);
    }
}
