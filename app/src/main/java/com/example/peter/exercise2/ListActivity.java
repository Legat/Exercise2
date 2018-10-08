package com.example.peter.exercise2;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;

import com.example.peter.exercise2.Adapters.NewsAdapter;
import com.example.peter.exercise2.Adapters.VerticalSpaceItemDecoration;
import com.example.peter.exercise2.Models.NewsItem;

import java.util.List;

public class ListActivity extends AppCompatActivity {
    public static final int VERTICAL_STATE_ITEM = 4;
    private GridLayoutManager gridLayoutManager;
    private NewsAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private List<NewsItem> listnews;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        recyclerView = findViewById(R.id.list_news);
        linearLayoutManager = new LinearLayoutManager(this);
        listnews = DataUtils.generateNews();
        adapter = new NewsAdapter(getApplicationContext(), listnews);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(VERTICAL_STATE_ITEM ));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            gridLayoutManager = new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);
            recyclerView.setLayoutManager(gridLayoutManager);
        } else if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
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
