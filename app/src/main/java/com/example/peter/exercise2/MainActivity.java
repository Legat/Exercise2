package com.example.peter.exercise2;



import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.peter.exercise2.Data.ListData;

public class MainActivity extends AppCompatActivity implements CallBackChange{
    private FrameLayout container;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        container = findViewById(R.id.container);
       // Toast.makeText(this,"Tam tak", Toast.LENGTH_LONG).show();
        fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.container);

        if(fragment == null){
            fragmentManager.beginTransaction()
                    .add(R.id.container, ListData.newInstance())
                    .commit();
        }

        if(fragment instanceof MewsDetailsFragment && widthDp() >= 720
                  && getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
           fragmentManager.beginTransaction().replace(R.id.container, ListData.newInstance())
                                             .commit();
           if(savedInstanceState != null){
               int id = savedInstanceState.getInt("key");
               if(id != 0){
               fragmentManager.beginTransaction().add(R.id.frame_detail,MewsDetailsFragment.newInstance(id))
                                                 .commit();
               }
           }



        }

    }

    private void buildDialog(){

    }

    @Override
    public void onBackPressed() {
        int count = fragmentManager.getBackStackEntryCount();
        if(widthDp() >= 720
                && getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            finish();
        }

     //   if(count==0) {
        super.onBackPressed();
    //    }else{
          //  Toast.makeText(this,"Check",Toast.LENGTH_SHORT).show();
//          fragmentList = fragmentManager.findFragmentById(R.id.container);
//          if(fragmentList instanceof ListFragment){
//
//
//          }
        //   fragmentManager.popBackStack();
     //   }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
         MewsDetailsFragment detailFrag = (MewsDetailsFragment) fragmentManager.findFragmentByTag("INFO_TAG");
         if(detailFrag != null){
             int id = detailFrag.getObjectId();
             if(id != 0){
             outState.putInt("key",id);

             }

         }
         super.onSaveInstanceState(outState);
    }

    private float widthDp(){
        // float density = getResources().getDisplayMetrics().density;
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        //int heigth = metrics.heightPixels;
        float dp =  width/metrics.scaledDensity;
        return dp;
    }




    @Override
    public void onItemDelete(int id) {
        ListFragment fragment = ListData.newInstance();
        fragmentManager.beginTransaction().add(R.id.container,fragment).commit();
       // fragment.deleteItem(id);



    }
}
