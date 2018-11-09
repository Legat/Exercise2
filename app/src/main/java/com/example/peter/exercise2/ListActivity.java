package com.example.peter.exercise2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.view.View;
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
import com.example.peter.exercise2.Providers.InternetConnection;
import com.example.peter.exercise2.Providers.RetrofitClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class ListActivity extends AppCompatActivity implements Handler.Callback {
    private static final int VERTICAL_STATE_ITEM = 4;
    private static final String API_KEY = "d94b328606074836a7618073303334da";
    private static final String SPLASH_SETTING = "splash";
    private static final String DATABASE_EXIST = "exist";
    public static final String SPLASH_BOOL_VAL = "value";
    private final static String Redact_Id = "redact_id";
    private GridLayoutManager gridLayoutManager;
    private NewsAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private CoordinatorLayout coordinate;
    private List<Result> listresult;
    private News newsItem;
    private RecyclerView recyclerView;
    private FloatingActionButton fabButton;
    private FrameLayout frame;
    private Handler handler;
    private NewsThread newsTread;
    private Thread filterThread;
    private String param;
    private RadioButton radioButton;
    private AlertDialog dialog;
    private SharedPreferences prefer;
    private ArrayList<NewsEntity> listEnt;
    private AppDataBase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefer = App.getInstanceApp().getPreference();
        db = App.getInstanceApp().getDatabase();
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
                fabButton = findViewById(R.id.update);
                fabButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences.Editor editor = prefer.edit();
                        editor.putBoolean(DATABASE_EXIST, false);
                        editor.apply();
                        newsTread = new NewsThread(handler, param);
                        newsTread.start();
                    }
                });
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                    gridManager();
                } else{
                    linearManager();
                }
                adapter = new NewsAdapter(this, listEnt, screenSize());
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

                Toast.makeText(this,"size convert" + listEnt.size(), Toast.LENGTH_SHORT).show();
                break;
            case 3:
                coordinate = findViewById(R.id.coordinate_lay);
                Snackbar.make(frame,R.string.internet, Snackbar.LENGTH_LONG).show();
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

    public static void startDetailsActiv(Context context, Intent intent){
     //  context.startActivityForResult();

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

                        SharedPreferences.Editor editor = prefer.edit();
                        editor.putBoolean(DATABASE_EXIST, false);
                        editor.apply();
//                          FilterRun filterRun = new FilterRun(title);
//                          new Thread(filterRun).start();

                        param = compoundButton.getText().toString() + ".json";
                        dialog.dismiss();
                        invalidateOptionsMenu();
                        newsTread = new NewsThread(handler, param);
                        newsTread.start();


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
        frame.addView(progressBar);
        if(prefer.getBoolean(SPLASH_BOOL_VAL,false)){
            progressBar.setVisibility(View.INVISIBLE);
            frame.setBackgroundResource(R.drawable.splash_ny);

            SharedPreferences.Editor editor = prefer.edit();
            editor.putBoolean(SPLASH_BOOL_VAL,true);
            editor.commit();
        }

//        progresstxt = new TextView(this);
//        progresstxt.setText(String.valueOf(3));
//        progresstxt.setLayoutParams(params);

 //       frame.addView(progresstxt);
        return frame;
    }

    @Override
    protected void onStop() {
        super.onStop();
      //  newsTread.stoping();
        newsTread.isInterrupted();
     //   secondThread.isInterrupted();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
      //  Toast.makeText(this,"OnDestroy", Toast.LENGTH_LONG).show();
      //  prefer.edit().clear().commit();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SharedPreferences.Editor editor = prefer.edit();
        editor.putBoolean(SPLASH_BOOL_VAL,true);
        editor.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
   //   adapter.onAdapterResult(requestCode,resultCode,data);
       //  super.onActivityResult(requestCode, resultCode, data);
     //   Toast.makeText(this, "Contact0", Toast.LENGTH_LONG).show();
        if(requestCode == 0){
            int id =  data.getIntExtra(NewsDetailsActivity.Redact_Id, 1);
            Toast.makeText(this, "Contact1", Toast.LENGTH_LONG).show();
            if(resultCode == RESULT_OK){
                Toast.makeText(this, "Contact2", Toast.LENGTH_LONG).show();
                adapter.delete(id);
            } else if(requestCode == RESULT_CANCELED) {
                Toast.makeText(this,"Change",Toast.LENGTH_LONG ).show();
                String title = data.getStringExtra(NewsDetailsActivity.TITLE);
                String publishDate = data.getStringExtra(NewsDetailsActivity.PUBLISH_NEWS);
                String fullText = data.getStringExtra(NewsDetailsActivity.FUL_NEWS);

                adapter.edit(id,title,fullText,publishDate);
            } else{
                return;
            }

        } else{
            Toast.makeText(this, "Contact0", Toast.LENGTH_LONG).show();
        }

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
            DAO newsDao = db.NewsDao();
          if(!prefer.getBoolean(DATABASE_EXIST,false)){
            //   if(InternetConnection.checkConnection(getApplicationContext())){

            ApiService api = RetrofitClient.getApiService();
            Call<News> news = api.getItemNews(arg, API_KEY);
            try {
                newsItem = news.execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            listresult = newsItem.getResults();
            //  ArrayList<NewsEntity> listEnt = new ArrayList<NewsEntity>();
            BaseConverter baseConverter = new BaseConverter();
            listEnt = baseConverter.toDataBase(listresult);
     //      handler.sendEmptyMessage(3);
            if(newsDao.getAll().isEmpty()){
              newsDao.insert(listEnt);
              } else if(!arg.equals("home.json")) {

                listEnt = (ArrayList<NewsEntity>) newsDao.getAll();
                newsDao.delete(listEnt);
                BaseConverter baseConverterFil = new BaseConverter();
                List<NewsEntity> filtList = baseConverterFil.toDataBase(listresult); ////
                newsDao.insert(filtList); ////


            } else {
                listEnt = (ArrayList<NewsEntity>) newsDao.getAll();
                newsDao.delete(listEnt);
                newsDao.insert(listEnt);
            }

            SharedPreferences.Editor editor = prefer.edit();
            editor.putBoolean(DATABASE_EXIST, true);
            editor.apply();
          }  // end Preference Data Base
        //

            listEnt = (ArrayList<NewsEntity>) newsDao.getAll();


              //  db.beginTransaction();


             //   db.endTransaction();
      //      }  end empty

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
        }  /// end Iterrupt
        }  /// end run
    }



    private int screenSize(){
        DisplayMetrics metrix = this.getResources().getDisplayMetrics();
        int width = metrix.widthPixels/2;
        return width;
    }

  private class FilterRun implements Runnable{
     volatile boolean isRun = true;
     private String filterVal;
     private ArrayList<NewsEntity> filList;

      public FilterRun(String filterVal) {
          this.filterVal = filterVal;
          filList = new ArrayList<NewsEntity>();
      }

      @Override
      public void run() {
        if(isRun){

            String filterArg = Character.toUpperCase(filterVal.charAt(0)) + filterVal.substring(1).toLowerCase();
            DAO newsDao = db.NewsDao();
            filList = (ArrayList<NewsEntity>) newsDao.getByFilter(filterArg);

            handler.post(new Runnable() {
                @Override
                public void run() {
               //   setContentView(R.layout.activity_list);
                  adapter.filter(filList);
                }
            });
        }
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

