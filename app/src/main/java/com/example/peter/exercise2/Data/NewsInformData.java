package com.example.peter.exercise2.Data;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;


import com.example.peter.exercise2.App;
import com.example.peter.exercise2.AppDataBase;
import com.example.peter.exercise2.DAO;
import com.example.peter.exercise2.MewsDetailsFragment;
import com.example.peter.exercise2.NewsEntity;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;


public class NewsInformData implements INewsInformData {
    private AppDataBase db;
    private NewsEntity news;
    private DetailThread detailThread;

    @Override
    public NewsEntity getNews(int id, int marker) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        DetailThread detailThread = new DetailThread(id,marker);

        Future<NewsEntity> future = executor.submit(detailThread);
        try {
            news = future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        // detailThread = new DetailThread(id, marker);
  //   detailThread.start();


     if(news != null){
        return news;
      }
     return null;
    }

    @Override
    public void saveNews(int id, int marker, NewsEntity newss) {
     this.news = newss;
     DetailThread detailThread = new DetailThread(id,5);
     FutureTask task = new FutureTask(detailThread);
     Thread detThread = new Thread(task);
     detThread.start();
        try {
            task.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    //    new DetailThread(id, 5);
   //  sleep();
    }


    @Override
    public void deleteNews(int id, int marker) {
     DetailThread detailThread = new DetailThread(id,marker);
     FutureTask task2 = new FutureTask(detailThread);
     Thread deleteThtread = new Thread(task2);
     deleteThtread.start();
        try {
            task2.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
//     new DetailThread(id,marker).start();
//     sleep();
    }

    public static Fragment newInstance(int id){
        Bundle args = new Bundle();
        args.putInt(NewsEntity.class.getSimpleName(),id);
        Fragment fragment = new MewsDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

//    private void sleep()  {
//        try {
//            Thread.sleep(200);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }




    private class DetailThread implements Callable<NewsEntity> {
        private int id;
        private int marker;


        private DetailThread(int id, int marker){
            this.id = id;
            this.marker = marker;

        }

//        @Override
//        public synchronized void run() {
//            super.run();
//            if(!isInterrupted()){
//            //    handler.sendEmptyMessage(1);
//                db = App.getInstanceApp().getDatabase();
//                DAO editDao = db.NewsDao();
//                if(marker == 1) {
//
//                   news = editDao.getById(id);
//                //   handler.sendMessage(handler.obtainMessage());
//
//                } else if(marker==2){
//                    checkNews();
//                    editDao.deleteItem(news);
//                }else{
//                    checkNews();
//                    try {
//                        editDao.update(news);
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//
//
//            }
//        }

        private void checkNews(){
            if(news == null){
                DAO editDao = db.NewsDao();
                news = editDao.getById(id);
            }
        }

        @Override
        public synchronized NewsEntity call() throws Exception {
            //    handler.sendEmptyMessage(1);
            db = App.getInstanceApp().getDatabase();
            DAO editDao = db.NewsDao();
            if(marker == 1) {  /// get data

                news = editDao.getById(id);
           //     return news;

            } else if(marker==2){  // delete
                checkNews();
                editDao.deleteItem(news);
            }else{  /// update
                checkNews();
                try {
                    editDao.update(news);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
              //   return news;

            }
            return news;
        }
    }


}
