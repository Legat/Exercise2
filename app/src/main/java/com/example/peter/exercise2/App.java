package com.example.peter.exercise2;

import android.app.Application;
import android.arch.persistence.room.Room;

import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;


import com.example.peter.exercise2.Presenter.AboutPresenter;
import com.example.peter.exercise2.di.AboutDataComponent;

import com.example.peter.exercise2.di.AboutDataModule;
import com.example.peter.exercise2.di.DaggerPersistancyComponent;
import com.example.peter.exercise2.di.PersistancyComponent;
import com.example.peter.exercise2.di.PersistancyModule;


import java.util.concurrent.TimeUnit;

import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

public class App extends Application {
    public static App instanceApp;
    private SharedPreferences preference;
    private AppDataBase database;
 //   private static AboutDataComponent aboutDataComponent;
    private static PersistancyComponent persistancyComponent;
    private static AboutDataComponent aboutDataComponent;

//    public static AboutDataComponent getAboutDataComponent(){
//        return aboutDataComponent;
//    }
    public static PersistancyComponent getPersistancyComponent(){
        return persistancyComponent;
    }

    public AboutDataComponent getAboutComponent(){
        if(aboutDataComponent == null){
            persistancyComponent.createAboutComponent(new AboutDataModule());
        }
        return aboutDataComponent;
    }

    public void clearAboutComponent(){
        aboutDataComponent = null;
    }

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

        //// dagger
     //   persistancyComponent = build();

        persistancyComponent = DaggerPersistancyComponent.builder()
                               .persistancyModule(new PersistancyModule(instanceApp))
                               .build();
    }

//    private AboutDataComponent build(){
//        return DaggerAboutDataComponent.builder()
//                .aboutDataModule(new AboutDataModule())
//                .build();
//    }

//      private PersistancyComponent build(){
//        return DaggerPersistancyComponent.builder()
//                .persistancyModule(new PersistancyModule(instanceApp))
//                .build();
//      }


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
