package com.example.peter.exercise2.di;

import android.content.Context;
import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ContextModule {
    private Context appContext;

    public ContextModule(@NonNull Context context) {
        appContext = context;
    }

    @Provides
    @Singleton
    Context getContextModule(){
      return appContext;
    }
}
