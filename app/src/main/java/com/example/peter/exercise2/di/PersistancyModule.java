package com.example.peter.exercise2.di;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.example.peter.exercise2.App;
import com.example.peter.exercise2.AppDataBase;
import com.example.peter.exercise2.DAO;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;



@Module
public class PersistancyModule {
   private Context appContext;

    public PersistancyModule(@NonNull Context context) {
        appContext = context;
    }

    @Provides
    @NonNull
    @Singleton
    DAO getDataBase(Context context){
        AppDataBase database = Room.databaseBuilder(context, AppDataBase.class, "database").build();
        return database.NewsDao();
    }
}
