package com.example.peter.exercise2;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.peter.exercise2.Models.News;
import com.example.peter.exercise2.Models.Result;
import com.example.peter.exercise2.Providers.ApiService;
import com.example.peter.exercise2.Providers.InternetConnection;
import com.example.peter.exercise2.Providers.RetrofitClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class SupportService extends Service  {
    SharedPreferences preference;
    AppDataBase bd;
    private static final String ARG_KEY = "arg_key";
    private static final String API_KEY = "d94b328606074836a7618073303334da";
    private News newsItem;
    private List<Result> listresult;
    private ArrayList<NewsEntity> listEnt;
    private UpdateThread updateThread;

    Timer timer;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        preference = App.getInstanceApp().getPreference();
        listresult = new ArrayList<>();
        bd = App.instanceApp.getDatabase();
        timer = new Timer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {



//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
//        Intent intentPen = new Intent(Intent.ACTION_VIEW, Uri.parse("http://techsupportnep.blogspot.com"));
//        PendingIntent pendingIntent = PendingIntent.getActivity(this,01,intentPen,0);
//        builder.setContentIntent(pendingIntent);
//        builder.setDefaults(Notification.DEFAULT_ALL);
//        builder.setContentTitle("Notification Title");
//        builder.setSmallIcon(R.mipmap.ic_launcher);
//        builder.setContentText("notification example");
//
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        notificationManager.notify(001, builder.build());
        createNotification();
///  old realization ////////////////////////////////////////////////////////////////////////////////////////////////////////////
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//
//
//                Intent notificationIntent = new Intent(SupportService.this,MainActivity.class);
//                PendingIntent pendInt = PendingIntent.getActivity(SupportService.this,0,notificationIntent,0);
//                Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.the_new_york);
//                NotificationCompat.Builder builder = new NotificationCompat.Builder(SupportService.this,"CHANNEL")
//                        .setLargeIcon(bitmap);
//                if(InternetConnection.checkConnection(getApplicationContext())) {
//                    builder.setSmallIcon(R.drawable.ic_done);
//                    String arg = getArg();
//                    Thread updateThread = new UpdateThread(arg);
//                    updateThread.start();
//                } else {
//                    builder.setSmallIcon(R.drawable.ic_error);
//                }
//
//                builder.setDefaults(Notification.DEFAULT_VIBRATE)
//                        .setContentTitle("NY Times")
//                        .setContentText("News updating...")
//                        .setAutoCancel(true)
//                        .setContentIntent(pendInt);
//
//                Notification notification = builder.build();
//
//                NotificationManager  notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//                notificationManager.notify(1,notification);
//
//                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
//                    startForeground(1,notification);
//                    }
//            }
//        },10000);
////////////////// old realization //////////////////////////////////////////////////////////////////////////////////////////

         Observable<News> observableApi = RetrofitClient.getObservableApi().getObservNews(getArg(),API_KEY);
                          observableApi.subscribeOn(Schedulers.io())
                                  .observeOn(AndroidSchedulers.mainThread());
         Subscription obser = observableApi
                                  .map(new Func1<News, List<Result>>() {
             @Override
             public List<Result> call(News news) {
                 return news.getResults();
             }
         })
                 .map(new Func1<List<Result>, List<NewsEntity>>() {
             @Override
             public List<NewsEntity> call(List<Result> results) {
                 BaseConverter baseConverter = new BaseConverter();
                 return listEnt = baseConverter.toDataBase(listresult);
             }
         }).subscribe(new Subscriber<List<NewsEntity>>() {
                     @Override
                     public void onCompleted() {

                     }

                     @Override
                     public void onError(Throwable e) {
                     Log.d("MY_TAG","Error " + e);
                     }

                     @Override
                     public void onNext(List<NewsEntity> newsEntities) {
                         final DAO updateDao = bd.NewsDao();
                         updateDao.deleteAll();
                         updateDao.insert(newsEntities);
                         Intent notificationIntent = new Intent(SupportService.this,MainActivity.class);
                         PendingIntent pendInt = PendingIntent.getActivity(SupportService.this,0,notificationIntent,0);
                         Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.the_new_york);
                         NotificationCompat.Builder builder = new NotificationCompat.Builder(SupportService.this,"CHANNEL")
                                 .setLargeIcon(bitmap)
                                 .setDefaults(Notification.DEFAULT_VIBRATE)
                                 .setSmallIcon(R.drawable.ic_done)
                                 .setContentTitle("NY Times")
                                 .setContentText("News updating...")
                                 .setAutoCancel(true)
                                 .setContentIntent(pendInt);

                         Notification notification = builder.build();

                         NotificationManager  notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                         notificationManager.notify(1,notification);

                         if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
                             startForeground(1,notification);
                         }
                     }
                 });
           obser.unsubscribe();



         NewsObservabale(getArg()).timeout(100,TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<List<NewsEntity>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                createErrorNotification(e);
                            }

                            @Override
                            public void onNext(List<NewsEntity> newsEntities) {
                                Intent notificationIntent = new Intent(SupportService.this,MainActivity.class);
                                PendingIntent pendInt = PendingIntent.getActivity(SupportService.this,0,notificationIntent,0);
                                Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.the_new_york);
                                NotificationCompat.Builder builder = new NotificationCompat.Builder(SupportService.this,"CHANNEL")
                                        .setLargeIcon(bitmap)
                                        .setDefaults(Notification.DEFAULT_VIBRATE)
                                        .setSmallIcon(R.drawable.ic_done)
                                        .setContentTitle("NY Times")
                                        .setContentText("News updating...")
                                        .setAutoCancel(true)
                                        .setContentIntent(pendInt);

                                Notification notification = builder.build();

                                NotificationManager  notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                notificationManager.notify(1,notification);

                                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
                                    startForeground(1,notification);
                                }
                            }
                        });

//        @Override
//        public void onCompleted() {
//            Toast.makeText(MainActivity.this,"Complete", Toast.LENGTH_SHORT).show();
//        }
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//          try {
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        stopSelf();
//            }
//        }).start();

//        Intent notificationIntent = new Intent(this,MainActivity.class);
//        PendingIntent pendInt = PendingIntent.getActivity(this,0,notificationIntent,0);
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.the_new_york);
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"CHANNEL")
//                .setLargeIcon(bitmap);
//           if(InternetConnection.checkConnection(getApplicationContext())) {
//               builder.setSmallIcon(R.drawable.ic_done);
//           } else {
//               builder.setSmallIcon(R.drawable.ic_error);
//           }
////                            .addAction(R.drawable.ic_agree,"OK",pendInt)
////                            .addAction(R.drawable.ic_cancel,"Cancel",pendInt)
//        builder.setDefaults(Notification.DEFAULT_VIBRATE)
//               .setContentTitle("NY Times")
//               .setContentText("News updating...")
//               .setContentIntent(pendInt);
//
//        Notification notification = builder.build();
//
//      //  notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        notificationManager.notify(1,notification);

//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
//            startForeground(1,notification);
//        }



        return START_STICKY;

    }

    private void createErrorNotification(Throwable e){
        Intent notificationIntent = new Intent(SupportService.this,MainActivity.class);
        PendingIntent pendInt = PendingIntent.getActivity(SupportService.this,0,notificationIntent,0);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.the_new_york);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(SupportService.this,"CHANNEL")
                .setLargeIcon(bitmap)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setSmallIcon(R.drawable.ic_error)
                .setContentTitle("NY Error: " + e.getStackTrace())
                .setContentText("News can'be update!")
                .setAutoCancel(true)
                .setContentIntent(pendInt);

        Notification notification = builder.build();

        NotificationManager  notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1,notification);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            startForeground(1,notification);
        }
    }

    private void createNotification(){
        Intent activityIntent = new Intent(this,MainActivity.class);
        PendingIntent pendActivity = PendingIntent.getActivity(this,0,activityIntent,0);
        Intent broadcastintent = new Intent(this,StopReceiver.class);
        PendingIntent pendbroad = PendingIntent.getBroadcast(this,0,broadcastintent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder2 = new NotificationCompat.Builder(this,"CHANNEL")
                                  .setSmallIcon(R.drawable.ic_question)
                                  .setDefaults(Notification.DEFAULT_SOUND)
                                  .setContentTitle("NY Times")
                                  .setContentText("Do you wanna update?")
                                  .setContentIntent(pendActivity)
                                  .setAutoCancel(true)
                                  .addAction(R.drawable.ic_cancel,"Cancel",pendbroad);

        Notification notificationBonus = builder2.build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(2,notificationBonus);



    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    private String getArg(){

        String argument = preference.getString(ARG_KEY,"home.json");
        return argument;
    }




    class UpdateThread extends Thread {
       private String arg;
      // private CatchException exeption;

        public UpdateThread(String arg) {
            this.arg = arg;


        }

        @Override
        public void run()  {
            super.run();
            if(!InternetConnection.checkConnection(getApplicationContext())){
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            DAO updateDao = bd.NewsDao();
          //  listEnt = (ArrayList<NewsEntity>) updateDao.getAll();
         //   updateDao.delete(listEnt);

            ApiService api = RetrofitClient.getApiService();
            Call<News> news = api.getItemNews(arg, API_KEY);
            try {
                newsItem = news.execute().body();
            } catch (IOException e) {
                e.printStackTrace();
              //  exeption.getMistake(false);
              //  throw new RuntimeException("a wrapper exception", e);
            }
            try {
               // Thread.sleep(1000);
                 listresult = newsItem.getResults();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            BaseConverter baseConverter = new BaseConverter();
            listEnt = baseConverter.toDataBase(listresult);

            try {
                Thread.sleep(300);
               updateDao.deleteAll();

                updateDao.insert(listEnt);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

           //  stopSelf();




        }
    }

    Observable<List<NewsEntity>> NewsObservabale(final String arg){
       return Observable.create(new Observable.OnSubscribe<List<NewsEntity>>() {
           @Override
           public void call(Subscriber<? super List<NewsEntity>> subscriber) {
               DAO updateDao = bd.NewsDao();
               //  listEnt = (ArrayList<NewsEntity>) updateDao.getAll();
               //   updateDao.delete(listEnt);

               ApiService api = RetrofitClient.getApiService();
               Call<News> news = api.getItemNews(arg, API_KEY);
               try {
                   newsItem = news.execute().body();
               } catch (IOException e) {
                   e.printStackTrace();
                   //  exeption.getMistake(false);
                   //  throw new RuntimeException("a wrapper exception", e);
               }
               try {
                   // Thread.sleep(1000);
                   listresult = newsItem.getResults();
               } catch (NullPointerException e) {
                   e.printStackTrace();
               }

               BaseConverter baseConverter = new BaseConverter();
               listEnt = baseConverter.toDataBase(listresult);

               try {
                   Thread.sleep(300);
                   updateDao.deleteAll();

                   updateDao.insert(listEnt);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
               subscriber.onNext(listEnt);
               subscriber.onCompleted();
           }
       });
    }





}

