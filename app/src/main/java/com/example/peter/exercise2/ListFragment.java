package com.example.peter.exercise2;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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

import com.arellomobile.mvp.MvpFragment;
import com.example.peter.exercise2.Adapters.NewsAdapter;
import com.example.peter.exercise2.Adapters.VerticalSpaceItemDecoration;
import com.example.peter.exercise2.Models.News;
import com.example.peter.exercise2.Models.Result;
import com.example.peter.exercise2.Presenter.IListPresenter;
import com.example.peter.exercise2.Presenter.ListPresenter;
import com.example.peter.exercise2.Providers.ApiService;
import com.example.peter.exercise2.Providers.RetrofitClient;
import com.example.peter.exercise2.View.IListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;

public class ListFragment extends Fragment implements IListView,Handler.Callback {
    private static final int VERTICAL_STATE_ITEM = 4;
    private static final String API_KEY = "d94b328606074836a7618073303334da";
    private static final String SPLASH_SETTING = "splash";
    private static final String DATABASE_EXIST = "exist";
    public static final String SPLASH_BOOL_VAL = "value";
    private final static String Redact_Id = "redact_id";
    private static final String ARG_KEY = "arg_key";
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
    private ArrayList<NewsEntity> listEnt;
    private AppDataBase db;
  //  private FrameLayout progressFrame;
    private ProgressBar progress;
    private Toolbar toolbar;
    private AppCompatActivity appCompatActivity;
    private IListPresenter listPresenter;

    private SharedPreferences prefer;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setHasOptionsMenu(true);
        setRetainInstance(true);
      //  App.getPersistancyComponent().inject(this);
        prefer = App.getInstanceApp().getPreference();
        db = App.getInstanceApp().getDatabase();
        listPresenter = new ListPresenter(this);
        if(param == null || param.equals("")){
            param = "home.json";
        }
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.activity_list,container,false);
        progress = view.findViewById(R.id.progress_bar);
        fabButton = view.findViewById(R.id.update);
        setToolBar(view);
        recyclerView = view.findViewById(R.id.list_news);


     //   handler = new Handler(this);
    //    newsTread = new NewsThread(handler, param);
    //    newsTread.start();

        return view;






    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                SharedPreferences.Editor editor = prefer.edit();
//                editor.putBoolean(DATABASE_EXIST, false);
//                editor.apply();
//                newsTread = new NewsThread(handler, param);
//                newsTread.start();
                listPresenter.loadList(getActivity(),2);
            }
        });
        listPresenter.loadList(getActivity(),1);

    }



    private void setToolBar(View view){
        toolbar = view.findViewById(R.id.main_toolbar);
        toolbar.setTitle("New York Times");
        ((MainActivity)getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
    }

    @Override
    public void loadList(NewsAdapter adapter) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            if(widthDp() >= 720){   /// for Portaint
                linearManager();
            } else{
                gridManager();
            }

        } else{
            linearManager();
        }
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(VERTICAL_STATE_ITEM ));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

    }

    @Override
    public boolean handleMessage(Message message) {
        switch (message.what){
            case 1:
                if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                   getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                } else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                   getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
              //  frame = ProgressLay();
                progress.setVisibility(View.VISIBLE);

                break;
            case 2:
                progress.setVisibility(View.GONE);

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
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                    if(widthDp() >= 720){   /// for Portaint
                        linearManager();
                    } else{
                        gridManager();
                    }

                } else{
                    linearManager();
                }
                adapter = new NewsAdapter(getContext(), listEnt, screenSize());
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

                 Toast.makeText(getContext(),"size convert" + listEnt.size(), Toast.LENGTH_SHORT).show();
                break;
            case 3:
                coordinate = getView().findViewById(R.id.coordinate_lay);
                Snackbar.make(frame,R.string.internet, Snackbar.LENGTH_LONG).show();
        }

        return false;
    }

//    public static ListFragment newInstance(){
//        ListFragment fragment = new ListFragment();
//        return fragment;
//    }

    private void linearManager(){
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void gridManager(){
        gridLayoutManager = new GridLayoutManager(getContext(),2,GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.news_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.about) {
            startActivity(new Intent(getContext(), AboutActivity.class));
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
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

            ScrollView scroll = new ScrollView(getContext());
            scroll.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            RadioGroup radioGroup = new RadioGroup(getContext());
            radioGroup.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams lineraParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.MATCH_PARENT);
            lineraParams.gravity = Gravity.CENTER_HORIZONTAL;
            radioGroup.setLayoutParams(lineraParams);
            scroll.addView(radioGroup);

            for (String aSelection : selection) {
                radioButton = new RadioButton(getContext());
                LinearLayout.LayoutParams checkParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                radioButton.setLayoutParams(checkParams);
                radioButton.setText(aSelection);
                radioButton.setPadding((int) (10 * dp), 0, 0, 0);
                radioButton.setTextSize(6 * getResources().getDisplayMetrics().density);
                radioGroup.addView(radioButton);

                radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        String title = compoundButton.getText().toString();
                        // ActionBar ab = getSupportActionBar();
                        toolbar.setSubtitle(title);

                        SharedPreferences.Editor editor = prefer.edit();
                        editor.putBoolean(DATABASE_EXIST, false);
                        editor.apply();


                        param = compoundButton.getText().toString() + ".json";
                        dialog.dismiss();
                        getActivity().invalidateOptionsMenu();
//                        newsTread = new NewsThread(handler, param);
//                        newsTread.start();

                        listPresenter.filterList(getActivity(), param);

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

    //    private FrameLayout ProgressLay(){
//        FrameLayout frame = new FrameLayout(getContext());
//        frame.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//
//        ProgressBar progressBar = new ProgressBar(getContext());
//        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT);
//        params.gravity = Gravity.CENTER;
//        progressBar.setLayoutParams(params);
//        frame.addView(progressBar);
//        if(prefer.getBoolean(SPLASH_BOOL_VAL,false)){
//            progressBar.setVisibility(View.INVISIBLE);
//            frame.setBackgroundResource(R.drawable.splash_ny);
//
//            SharedPreferences.Editor editor = prefer.edit();
//            editor.putBoolean(SPLASH_BOOL_VAL,true);
//            editor.commit();
//        }
//
////        progresstxt = new TextView(this);
////        progresstxt.setText(String.valueOf(3));
////        progresstxt.setLayoutParams(params);
//
//        //       frame.addView(progresstxt);
//        return frame;
//    }

    private int screenSize(){
        DisplayMetrics metrix = this.getResources().getDisplayMetrics();
        int width = metrix.widthPixels/2;
        return width;
    }



    public void deleteItem(int id){
        if(id != 0) {
            adapter.delete(id);
                    }
    }



    @Override
    public void errorMessage(String message) {
      Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();
    }

    @Override
    public void showProgress() {
      recyclerView.setVisibility(View.INVISIBLE);
      progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
      progress.setVisibility(View.GONE);
      recyclerView.setVisibility(View.VISIBLE);
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
                    editor.putString(ARG_KEY,arg);
                    editor.apply();
                }  // end Preference Data Base
                //

                listEnt = (ArrayList<NewsEntity>) newsDao.getAll();




                handler.sendEmptyMessage(2);
            }  /// end Iterrupt
        }  /// end run

    }

    private float widthDp(){
        // float density = getResources().getDisplayMetrics().density;
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        //int heigth = metrics.heightPixels;
        return width/metrics.scaledDensity;


    }


}
