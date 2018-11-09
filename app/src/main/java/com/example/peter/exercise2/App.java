package com.example.peter.exercise2;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.SharedPreferences;

public class App extends Application {
    public static App instanceApp;
    private SharedPreferences preference;
    private AppDataBase database;
    private static final String SPLASH_SETTING = "splash";
 //   private static  final String SPLASH_BOOL_VAL = "value";
    @Override
    public void onCreate() {
        super.onCreate();
        instanceApp = this;
        database = Room.databaseBuilder(this, AppDataBase.class, "database").build();
        preference = getSharedPreferences(SPLASH_SETTING, MODE_PRIVATE);
    }


    public static App getInstanceApp(){
        return  instanceApp;
    }

    public AppDataBase getDatabase() {
        return database;
    }

    public SharedPreferences getPreference() {
        return preference;
    }


}
