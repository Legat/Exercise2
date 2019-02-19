package com.example.peter.exercise2.di;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static android.content.Context.MODE_PRIVATE;

@Module
public class PreferenceModule {
    private SharedPreferences preference;
    private static final String SPLASH_SETTING = "splash";

    @Provides
    @NonNull
    @Singleton
    SharedPreferences getPreference(@NonNull Context context){
        preference = context.getSharedPreferences(SPLASH_SETTING, MODE_PRIVATE);
        return preference;
     }

}
