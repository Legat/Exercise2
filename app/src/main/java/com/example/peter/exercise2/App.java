package com.example.peter.exercise2;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;

import java.util.concurrent.TimeUnit;

import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

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

        Constraints constrain = new Constraints.Builder()
                          .setRequiresCharging(true)
                          .setRequiredNetworkType(NetworkType.UNMETERED)
                          .build();

        PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(ServiceWorker.class,3, TimeUnit.MINUTES,2,TimeUnit.MINUTES)
                                                 .setConstraints(constrain)
                                                 .build();
       // OneTimeWorkRequest workRequest1 = new OneTimeWorkRequest.Builder(ServiceWorker.class).build();
       // WorkManager.getInstance().enqueue(workRequest1);
        WorkManager.getInstance().enqueue(workRequest);
        registerReceiver(new NetworUtils.NetworkReceiver(),new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));





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

//    public RequestManager getGlide(Context context){
//        RequestOptions requestOptions = new RequestOptions();
//        requestOptions.placeholder(R.drawable.ic_action_holder);
//        requestOptions.centerCrop();
//        return  Glide.with(context)
//                      .setDefaultRequestOptions(requestOptions);
//
//    }

}
