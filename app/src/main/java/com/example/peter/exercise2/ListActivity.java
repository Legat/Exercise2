package com.example.peter.exercise2;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
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
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.peter.exercise2.Adapters.NewsAdapter;
import com.example.peter.exercise2.Adapters.VerticalSpaceItemDecoration;
import com.example.peter.exercise2.Models.News;
import com.example.peter.exercise2.Models.Result;
import com.example.peter.exercise2.Providers.ApiService;
import com.example.peter.exercise2.Providers.RetrofitClient;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;

public class ListActivity extends AppCompatActivity implements Handler.Callback {
    private static final int VERTICAL_STATE_ITEM = 4;
    private static final String API_KEY = "d94b328606074836a7618073303334da";
    private GridLayoutManager gridLayoutManager;
    private NewsAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
  //  private List<NewsItem> listnews;
    private List<Result> listresult;
    private News newsItem;
    private RecyclerView recyclerView;
    private FrameLayout frame;
    private Handler handler;
    private NewsThread newsTread;
    private NewsThread secondThread;
    private String param;
    private RadioButton radioButton;
    private AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(param == null || param == ""){
            param = "home.json";
            setTitle("New York Times");
        }

        handler = new Handler(this);
        newsTread = new NewsThread(handler, param);

          //  if (!newsTread.isAlive()){
                newsTread.start();
         //   }



    //    linearManager();

      //  ApiService api = RetrofitClient.getApiService();
     //   Call<News> news = api.getItemNews(API_KEY);
//        try {
//            ApiService api = RetrofitClient.getApiService();
//            Call<News> news = api.getItemNews(API_KEY);
//            newsItem = news.execute().body();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        news.enqueue(new Callback<News>() {
//            @Override
//            public void onResponse(Call<News> call, Response<News> response) {
//                if(response.isSuccessful()){
//                    newsItem = response.body();
//                    Toast.makeText(ListActivity.this,"success" + newsItem.getResults().size(), Toast.LENGTH_LONG).show();
//                    listresult = newsItem.getResults();
//                } else{
//                    Log.d("MY_TAG", "response code" + response.errorBody().toString());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<News> call, Throwable t) {
//                Log.d("MY_TAG", "failure " + t);
//                Toast.makeText(ListActivity.this,"quantity "+ t.toString(), Toast.LENGTH_LONG).show();
//            }
//        });

//        listresult.size();
       //  newsItem.getResults();
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
                setContentView(R.layout.activity_list);
                recyclerView = findViewById(R.id.list_news);


                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                    gridManager();
                } else{
                    linearManager();
                }
                adapter = new NewsAdapter(getApplicationContext(), listresult, screenSize());
                recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(VERTICAL_STATE_ITEM ));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(adapter);
//                if(secondThread != null) {
//                    secondThread.isInterrupted();
//                    newsTread.isInterrupted();
////                    if(!secondThread.isAlive()){
////                        Toast.makeText(this, "Threads is not alive", Toast.LENGTH_LONG).show();
////                    }
//                }


                break;
//            case 3:
//                progresstxt.setText(String.valueOf(message.arg1));
        }

        return false;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            gridManager();
        } else if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            linearManager();
        }
    }

    private void linearManager(){
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void gridManager(){
        gridLayoutManager = new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.news_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.about) {
            startActivity(new Intent(this, AboutActivity.class));
            return true;
        } else if(item.getItemId() == R.id.filter){
         //   Toast.makeText(this, "Try",Toast.LENGTH_SHORT).show();

//            if(item.getIcon().getConstantState().equals(getResources().getDrawable(R.drawable.ic_filter).getConstantState())){
//             item.setIcon(R.drawable.ic_filter_remove);
//            } else {
//                item.setIcon(R.drawable.ic_filter);
//            }
            String[] selection = getResources().getStringArray(R.array.array_sections);
            float dp =  getResources().getDisplayMetrics().density;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            ScrollView scroll = new ScrollView(this);
            scroll.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            RadioGroup radioGroup = new RadioGroup(this);
            radioGroup.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams lineraParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.MATCH_PARENT);
            lineraParams.gravity = Gravity.CENTER_HORIZONTAL;
            radioGroup.setLayoutParams(lineraParams);
            scroll.addView(radioGroup);

            for(int i = 0; i< selection.length; i++){
                radioButton = new RadioButton(this);
                LinearLayout.LayoutParams checkParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                radioButton.setLayoutParams(checkParams);
                radioButton.setText(selection[i].toString());
                radioButton.setPadding((int) (10*dp),0,0,0);
                radioButton.setTextSize(6 * getResources().getDisplayMetrics().density);
                radioGroup.addView(radioButton);

                radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        String title = compoundButton.getText().toString();
                        ActionBar ab = getSupportActionBar();
                        ab.setSubtitle(title);
                        param = compoundButton.getText().toString() + ".json";
                        dialog.dismiss();
                        invalidateOptionsMenu();

                        secondThread = new NewsThread(handler, param);
                        secondThread.start();


                    }
                });
            }


            builder.setView(scroll);

            dialog = builder.create();
            dialog.show();





            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private FrameLayout ProgressLay(){
        FrameLayout frame = new FrameLayout(this);
        frame.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ProgressBar progressBar = new ProgressBar(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        progressBar.setLayoutParams(params);
//        progresstxt = new TextView(this);
//        progresstxt.setText(String.valueOf(3));
//        progresstxt.setLayoutParams(params);
        frame.addView(progressBar);
 //       frame.addView(progresstxt);
        return frame;
    }

    @Override
    protected void onStop() {
        super.onStop();
      //  newsTread.stoping();
        newsTread.isInterrupted();
        secondThread.isInterrupted();
        handler.removeCallbacksAndMessages(null);
    }



    private class NewsThread extends Thread{
        private Handler handler;
        private String arg;
      //  private volatile boolean isRuning = true;
        private NewsThread(Handler handler, String arg){
            this.handler = handler;
            this.arg = arg;
        }

    @Override
    public void run() {
        super.run();

        if(!isInterrupted()) {
            handler.sendEmptyMessage(1);


            ApiService api = RetrofitClient.getApiService();
            Call<News> news = api.getItemNews(arg, API_KEY);
            try {
                newsItem = news.execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            listresult = newsItem.getResults();
//            while (count >= 0 && !isInterrupted()) {
//              try {
//                  sleep( 500);
//                  Message msg = handler.obtainMessage(3);
//                  msg.arg1 = count;
//                  handler.sendMessage(msg);
//                  count--;
//              } catch (InterruptedException e) {
//                  e.printStackTrace();
//              }
     //     }
            handler.sendEmptyMessage(2);
        }
    }

}

    private int screenSize(){
        DisplayMetrics metrix = this.getResources().getDisplayMetrics();
        int width = metrix.widthPixels/2;
        return width;
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
