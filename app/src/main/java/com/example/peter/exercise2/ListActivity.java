package com.example.peter.exercise2;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.example.peter.exercise2.Adapters.NewsAdapter;
import com.example.peter.exercise2.Adapters.VerticalSpaceItemDecoration;
import com.example.peter.exercise2.Models.NewsItem;

import java.util.List;

public class ListActivity extends AppCompatActivity implements Handler.Callback {
    public static final int VERTICAL_STATE_ITEM = 4;
    private GridLayoutManager gridLayoutManager;
    private NewsAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private List<NewsItem> listnews;
    private RecyclerView recyclerView;
    private FrameLayout frame;
    private Handler handler;
    private NewsThread newsTread;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler(this);
        newsTread = new NewsThread(handler);
      //  if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT || getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            if (!newsTread.isAlive()){
                newsTread.start();
            }
     //   }

    }

    @Override
    public boolean handleMessage(Message message) {
        switch (message.what){
            case 1:
                if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                } else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
                frame = ProgressLay();
                setContentView(frame);

                break;
            case 2:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                setContentView(R.layout.activity_list);
                recyclerView = findViewById(R.id.list_news);
                adapter = new NewsAdapter(getApplicationContext(), listnews);
                if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                    gridLayoutManager = new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);
                    recyclerView.setLayoutManager(gridLayoutManager);
                } else{
                    linearLayoutManager = new LinearLayoutManager(this);
                    recyclerView.setLayoutManager(linearLayoutManager);

                }
                recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(VERTICAL_STATE_ITEM ));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(adapter);
                break;
        }

        return false;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            gridLayoutManager = new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);
            recyclerView.setLayoutManager(gridLayoutManager);
        } else if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            linearLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(linearLayoutManager);
        }
    }

    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.news_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != R.id.about) {
            return super.onOptionsItemSelected(item);
        }
        startActivity(new Intent(this, AboutActivity.class));
        return true;
    }

    private FrameLayout ProgressLay(){
        FrameLayout frame = new FrameLayout(this);
        frame.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ProgressBar progressBar = new ProgressBar(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        progressBar.setLayoutParams(params);
        frame.addView(progressBar);
        return frame;
    }

    @Override
    protected void onStop() {
        super.onStop();
        newsTread.stoping();
        handler.removeCallbacksAndMessages(null);
    }



    private class NewsThread extends Thread{
        private Handler handler;
        private volatile boolean isRuning = true;
        private NewsThread(Handler handler){
            this.handler = handler;
        }

    @Override
    public void run() {
        super.run();
        if(isRuning) {
            handler.sendEmptyMessage(1);

            try {
                sleep(2000);
                listnews = DataUtils.generateNews();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            handler.sendEmptyMessage(2);
        }
    }
    private void stoping(){
            isRuning = false;
    }

}

//    private float size(){
//       // float density = getResources().getDisplayMetrics().density;
//        DisplayMetrics metrics = getResources().getDisplayMetrics();
//
//        //int width = metrics.widthPixels;
//        int heigth = metrics.heightPixels;
//         float dp =  heigth/metrics.scaledDensity;
//         return dp;
//
//    }
}
