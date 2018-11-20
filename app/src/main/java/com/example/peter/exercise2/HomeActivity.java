package com.example.peter.exercise2;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.example.peter.exercise2.Adapters.ViewPagerAdapter;

import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class HomeActivity extends AppCompatActivity {
    ViewPager viewPager;
    PagerAdapter adapter;
    int[] img;
    private static int currentpage = 0;
    private static int numpage = 0;
    private TextView welcome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        img = new int[]{R.drawable.news1, R.drawable.details2, R.drawable.about3};
        viewPager = findViewById(R.id.pager);
        welcome = findViewById(R.id.welcome);
        adapter = new ViewPagerAdapter(this, img);
        viewPager.setAdapter(adapter);

         welcome.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 finish();
                 Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                 startActivity(intent);
             }
         });
        CircleIndicator indicator = findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
             currentpage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
              if(state == ViewPager.SCROLL_STATE_IDLE){
                  int pagecount = img.length;
                  if(currentpage == 0){
                      viewPager.setCurrentItem(pagecount-1,false);
                  }else if(currentpage == pagecount-1){
                      viewPager.setCurrentItem(0,false);
                  }
              }
            }
        });

        final Handler handler= new Handler();
        final Runnable update = new Runnable() {
            @Override
            public void run() {
                if(currentpage == numpage){
                    currentpage=0;
                }
                viewPager.setCurrentItem(currentpage++,true);
            }
        };
        Timer swipe = new Timer();
        swipe.schedule(new TimerTask() {
            @Override
            public void run() {
            handler.post(update);
            }
        },1000,1000);
    }
}
