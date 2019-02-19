package com.example.peter.exercise2;

import android.content.Context;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;

import com.example.peter.exercise2.Presenter.NewsInformPresenter;
import com.example.peter.exercise2.View.INewsDetailsInfoView;



public class MewsDetailsFragment extends MvpAppCompatFragment implements INewsDetailsInfoView{
    @InjectPresenter
    NewsInformPresenter newsInformPresenter;
    public final static String Redact_Id = "redact_id";
    public final static String TITLE = "title_n";
    public final static String FUL_NEWS = "full_info";
    public static final String PUBLISH_NEWS = "publish_date";
    private TextView title, datetext, fulltext;
    private EditText edTitle, edDate, edFulltext;
    private ImageView imageNews;
    // private WebView page;
 //   private Handler handler;
    private MenuItem delete, edit, save;
    private ProgressBar progress;
    private LinearLayout detailLay;
    private LinearLayout editLay;
    private NewsEntity news;
  //  private DetailThread detaiThread;
    private Thread EditThread;
    // private ArrayList<EditText> editList;
    private AppDataBase db;
    private int id;
    private Toolbar toolbar;
    private CallBackChange callBacker;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = App.getInstanceApp().getDatabase();
      //  newsInformPresenter = new NewsInformPresenter(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            callBacker = (CallBackChange) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "must implement interface CallBackChange");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         View view = inflater.inflate(R.layout.activity_news_details,container,false);

      //  getActivity().setTitle("New York Times");

        imageNews = view.findViewById(R.id.image_new);
        title = view.findViewById(R.id.main_title);
        datetext = view.findViewById(R.id.date_text);
        fulltext = view.findViewById(R.id.full_text);
        progress = view.findViewById(R.id.progress_det);
        detailLay = view.findViewById(R.id.detail_lay);
        editLay = view.findViewById(R.id.edit_lay);
        toolbar = view.findViewById(R.id.detail_toolbar);
        toolbar.setTitle("New York Times");
        ((MainActivity)getActivity()).setSupportActionBar(toolbar);
       // ((MainActivity)getActivity()).getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);


     //   handler = new Handler(this);
        id = getArguments().getInt(NewsEntity.class.getSimpleName());
        newsInformPresenter.getNews(id);
    //    new DetailThread(id,handler).start();
        return view;
    }

    public static Fragment newInstance(int id){
        Bundle args = new Bundle();
        args.putInt(NewsEntity.class.getSimpleName(),id);
        Fragment fragment = new MewsDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public int getObjectId(){
        return news.getId();
    }

    private void setToolbar(View view){
        toolbar = view.findViewById(R.id.detail_toolbar);
        toolbar.setTitle("New York Times");
        ((MainActivity)getActivity()).setSupportActionBar(toolbar);
        // ((MainActivity)getActivity()).getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);
    }


  //  @Override
    public boolean handleMessage(Message message) {
        switch (message.what){
            case 1:
                progress.setVisibility(View.VISIBLE);
                title.setVisibility(View.GONE);
                datetext.setVisibility(View.GONE);
                fulltext.setVisibility(View.GONE);
                break;
            case 2:
                progress.setVisibility(View.GONE);
                title.setVisibility(View.VISIBLE);
                datetext.setVisibility(View.VISIBLE);
                fulltext.setVisibility(View.VISIBLE);


                title.setText(news.getTitle());
                datetext.setText(news.getPublishDate().toString());
                fulltext.setText(news.getFulltext());
                GlideApp.with(this)
                        .load(news.getMultimediqUrl())
                        .centerCrop()
                        .placeholder(R.drawable.ic_action_holder)
                        .into(imageNews);
                break;

        }
        return false;

    }

    ///@Override    посмотреть как замутить меню.
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getActivity().getMenuInflater().inflate(R.menu.edit_menu, menu);
//        delete = menu.findItem(R.id.delete);
//        save = menu.findItem(R.id.save);
//        edit = menu.findItem(R.id.edit);
//        return true;
//    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
         inflater.inflate(R.menu.edit_menu,menu);
         super.onCreateOptionsMenu(menu, inflater);
         delete = menu.findItem(R.id.delete);
         save = menu.findItem(R.id.save);
         edit = menu.findItem(R.id.edit);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case R.id.edit:
                save.setVisible(true);
                edit.setVisible(false);

                detailLay.setVisibility(View.GONE);
                editLay.setVisibility(View.VISIBLE);
               newsInformPresenter.editNews(new NewsEntity(title.getText().toString(), fulltext.getText().toString() ,datetext.getText().toString()));


                break;
            case R.id.delete:
                newsInformPresenter.deleteNews(id,getActivity(),this);
//                EditThread = new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        DAO editDao = db.NewsDao();
//                        editDao.deleteItem(news);
//                        handler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                Intent intent = new Intent();
//
//                                int id = news.getId();
//                                intent.putExtra(Redact_Id, id);
//
//                               getActivity().getSupportFragmentManager().beginTransaction().remove(MewsDetailsFragment.this).commit();
//
//                               callBacker.onItemDelete(id);
//                              //  getActivity().setResult(RESULT_OK,intent);
//                             //   finish();
//                            }
//                        });
//                    }
//                });
//                EditThread.start();
                break;
            case R.id.save:
                save.setVisible(false);
                edit.setVisible(true);
                newsInformPresenter.saveNews(id, new NewsEntity(edTitle.getText().toString(), edFulltext.getText().toString(), edDate.getText().toString()));
//                EditThread = new Thread(new Runnable() {
//                    //    volatile boolean isRunning = true;
//                    @Override
//                    public void run() {
//
//                        handler.post(new Runnable() {
//
//                            @Override
//                            public void run() {
//
//                                news.setTitle(edTitle.getText().toString());
//                                news.setFulltext(edFulltext.getText().toString());
//                                news.setPublishDate(edDate.getText().toString());
//                            }
//                        });
//                        try {
//                            Thread.sleep(1000);
//                            DAO editDao = db.NewsDao();
//                            editDao.update(news);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//
//
//                        handler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                detailLay.setVisibility(View.VISIBLE);
//                                editLay.setVisibility(View.GONE);
//                                title.setText(news.getTitle());
//                                datetext.setText(news.getPublishDate());
//                                fulltext.setText(news.getFulltext());
//
////                                String title = news.getTitle();
////                                String publishDate = news.getPublishDate();
////                                String fullTex = news.getFulltext();
//                                Intent intent = new Intent();
//                                intent.putExtra(TITLE, news.getTitle());
//                                intent.putExtra(FUL_NEWS, news.getFulltext());
//                                intent.putExtra(PUBLISH_NEWS, news.getPublishDate());
//                                intent.putExtra(Redact_Id, news.getId());
//                               // getActivity().setResult(RESULT_FIRST_USER,intent);
//
//                            }
//                        });
//
//
//
//                    }
//
//                    //   private void stopRun(){
//                    //      isRunning = false;
//                    //    }
//                });
//                EditThread.start();
                break;


        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void getNews(NewsEntity news) {
      title.setText(news.getTitle());
      datetext.setText(news.getPublishDate().toString());
      fulltext.setText(news.getFulltext());
      newsInformPresenter.loadImg(imageNews,news.getMultimediqUrl(),this);
    }

    @Override
    public void getEditNews(NewsEntity news) {
        edTitle = getView().findViewById(R.id.title_edit);
        edDate = getView().findViewById(R.id.date_edit);
        edFulltext = getView().findViewById(R.id.full_edit);
        edTitle.setText(news.getTitle());
        edDate.setText(news.getPublishDate());
        edFulltext.setText(news.getFulltext());

    }

    @Override
    public void saveNews(NewsEntity news) {
        detailLay.setVisibility(View.VISIBLE);
        editLay.setVisibility(View.GONE);
        title.setText(news.getTitle());
        datetext.setText(news.getPublishDate());
        fulltext.setText(news.getFulltext());
    }

    @Override
    public void deleteNews(int id) {

    }


    private class DetailThread extends Thread {
        private int id;
        private Handler handler;

        private DetailThread(int id, Handler handler){
            this.id = id;
            this.handler = handler;
        }


        @Override
        public void run() {
            super.run();
            if(!isInterrupted()){
                handler.sendEmptyMessage(1);
                DAO editDao = db.NewsDao();
                news = editDao.getById(id);
                Message mes = handler.obtainMessage(2);
                handler.sendEmptyMessage(2);

            }
        }
    }




}
