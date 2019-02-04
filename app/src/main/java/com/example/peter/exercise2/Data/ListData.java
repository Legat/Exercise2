package com.example.peter.exercise2.Data;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

import com.example.peter.exercise2.App;
import com.example.peter.exercise2.AppDataBase;
import com.example.peter.exercise2.BaseConverter;
import com.example.peter.exercise2.DAO;
import com.example.peter.exercise2.ListFragment;
import com.example.peter.exercise2.Models.News;
import com.example.peter.exercise2.Models.Result;
import com.example.peter.exercise2.NewsEntity;
import com.example.peter.exercise2.Providers.ApiService;
import com.example.peter.exercise2.Providers.InternetConnection;
import com.example.peter.exercise2.Providers.RetrofitClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import retrofit2.Call;

public class ListData implements IListData {
    private static final String API_KEY = "d94b328606074836a7618073303334da";
    private static final String DATABASE_EXIST = "exist";
    private static final String ARG_KEY = "arg_key";
    private List<Result> listresult;
    private News newsItem;
    private SharedPreferences prefer;
    private List<NewsEntity> listEnt;
    private AppDataBase db;

    @Override
    public List<NewsEntity> loadList(int marker) {
        prefer = App.getInstanceApp().getPreference();
        String arg = prefer.getString(ARG_KEY, "home.json");
        ExecutorService executor = Executors.newSingleThreadExecutor();
     //   Callable<List<NewsEntity>> callable = new NewsThread(arg,1);
     //   FutureTask task = new FutureTask(callable);
        NewsThread newsThread = new NewsThread(arg,marker);
        Future<List<NewsEntity>> future = executor.submit(newsThread);
        try {
            listEnt = future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        //   Thread newsThread = new Thread(task);
      //  newsThread.start();
//        try {
//            listEnt = (List<NewsEntity>) task.get();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
        return listEnt;
    }

    @Override
    public List<NewsEntity> updateList() {
        return null;
    }

    @Override
    public List<NewsEntity> filterList(String arg) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        NewsThread newsThread = new NewsThread(arg,3);
        Future<List<NewsEntity>> future = executor.submit(newsThread);
        try {
            listEnt = future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return listEnt;
    }

    @Override
    public boolean dataBaseExist() {
        prefer = App.getInstanceApp().getPreference();
        if (!prefer.getBoolean(DATABASE_EXIST,false))
        return false;
        else
        return true;
    }

    @Override
    public boolean internetConnection(Context context) {
        if(InternetConnection.checkConnection(context))
        return true;
        else
        return false;
    }

    @Override
    public int screenSize(Context context) {
        DisplayMetrics metrix = context.getResources().getDisplayMetrics();
        int width = metrix.widthPixels/2;
        return width;
    }

    @Override
    public String screenType(Context context) {
//        Activity mainActivity = (AppCompatActivity) context;
//        if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
//            mainActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        } else if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
//            mainActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        }
         return null;
    }

    public static ListFragment newInstance(){
        ListFragment fragment = new ListFragment();
        return fragment;
    }

    private class NewsThread implements Callable<List<NewsEntity>>{
        private String arg;
        private int marker;
        NewsThread(String arg, int marker){
            this.arg = arg;
            this.marker = marker;
        }
        @Override
        public synchronized List<NewsEntity> call() throws Exception {

            db = App.getInstanceApp().getDatabase();
            prefer = App.getInstanceApp().getPreference();
            DAO newsDao = db.NewsDao();
            BaseConverter baseConverter = new BaseConverter();
            if(marker == 1){ /// load
                if(newsDao.getAll().isEmpty()){

                    addShared();
                } else{
                    listEnt = newsDao.getAll();
                }

            } else if(marker ==2){ /// update
                newsDao.deleteAll();



            } else if(marker==3){ // filter
                newsDao.deleteAll();

                addShared();
            }
                ApiService api = RetrofitClient.getApiService();
                Call<News> news = api.getItemNews(arg, API_KEY);
                newsItem = news.execute().body();
                listresult = newsItem.getResults();
                listEnt = baseConverter.toDataBase(listresult);
                newsDao.insert(listEnt);




             return listEnt;
        }



        private void addShared(){
            SharedPreferences.Editor editor = prefer.edit();
            editor.putBoolean(DATABASE_EXIST, true);
            editor.putString(ARG_KEY,arg);
            editor.apply();
        }
    }
}
