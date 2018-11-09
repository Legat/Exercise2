package com.example.peter.exercise2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.peter.exercise2.Models.Result;

import java.util.ArrayList;

public class NewsDetailsActivity extends AppCompatActivity implements Handler.Callback {
    public final static String Redact_Id = "redact_id";
    public final static String TITLE = "title_n";
    public final static String FUL_NEWS = "full_info";
    public static final String PUBLISH_NEWS = "publish_date";
    private TextView title, datetext, fulltext;
    private EditText edTitle, edDate, edFulltext;
    private ImageView imageNews;
   // private WebView page;
    private Handler handler;
    private MenuItem delete, edit, save;
    private ProgressBar progress;
    private LinearLayout detailLay;
    private LinearLayout editLay;
    private NewsEntity news;
    private DetailThread detaiThread;
    private Thread EditThread;
   // private ArrayList<EditText> editList;
    private AppDataBase db;
    private int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("New York Times");
        setContentView(R.layout.activity_news_details);
        imageNews = findViewById(R.id.image_new);
        title = findViewById(R.id.main_title);
        datetext = findViewById(R.id.date_text);
        fulltext = findViewById(R.id.full_text);
        progress = findViewById(R.id.progress_det);
        detailLay = findViewById(R.id.detail_lay);
        editLay = findViewById(R.id.edit_lay);

       // setResult(RESULT_CANCELED);
        db = App.getInstanceApp().getDatabase();
        handler = new Handler(this);
        id = getIntent().getExtras().getInt(NewsEntity.class.getSimpleName());
        new DetailThread(id,handler).start();

//        new Thread(new Runnable() {
//            volatile boolean isRunning = true;
//            @Override
//            public void run() {
//                if(isRunning){
//
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//
//                        }
//                    });
//                }
//            }
//
//        }).start();


        //    String url = getIntent().getExtras().getString(Result.class.getSimpleName());
    //    String imageurl = getIntent().getExtras().getString(Result.class.getName());

   //     page = findViewById(R.id.web_page);
      //  page.getSettings().setJavaScriptEnabled(true);
    //    page.loadUrl(url);
    //    page.setHorizontalScrollBarEnabled(false);





//          if(news != null) {
//              title.setText(news.getTitle());
//              datetext.setText(news.getPublishDate().toString());
//              fulltext.setText(news.getFulltext());
//          } else{
//              Toast.makeText(this,"null", Toast.LENGTH_SHORT);
//          }



//          title.setEnabled(false);
//          title.setCursorVisible(false);
//          title.setBackgroundColor(Color.TRANSPARENT);
//          title.setKeyListener(null);




      //  NewsItem news = (NewsItem) getIntent().getSerializableExtra(NewsItem.class.getSimpleName());


       // setTitle(news.getCategory().getName());






    }

    @Override
    protected void onStop() {
        super.onStop();
        Thread.currentThread().isInterrupted();
    }

    @Override
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



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        delete = menu.findItem(R.id.delete);
        save = menu.findItem(R.id.save);
        edit = menu.findItem(R.id.edit);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case R.id.edit:
                save.setVisible(true);
                edit.setVisible(false);
//                EditThread = new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                     handler.post(new Runnable() {
//                         @Override
//                         public void run() {
                          detailLay.setVisibility(View.GONE);
                          editLay.setVisibility(View.VISIBLE);
                          edTitle = findViewById(R.id.title_edit);
                          edDate = findViewById(R.id.date_edit);
                          edFulltext = findViewById(R.id.full_edit);
                          edTitle.setText(news.getTitle());
                          edDate.setText(news.getPublishDate());
                          edFulltext.setText(news.getFulltext());
//                         }
//                     });
//                    }
//                });

                break;
            case R.id.delete:
                EditThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        DAO editDao = db.NewsDao();
                        editDao.deleteItem(news);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                            Intent intent = new Intent();

                            int id = news.getId();
                            intent.putExtra(Redact_Id, id);
                            setResult(RESULT_OK,intent);
                            finish();
                            }
                        });
                    }
                });
                EditThread.start();
                break;
            case R.id.save:
                save.setVisible(false);
                edit.setVisible(true);
                EditThread = new Thread(new Runnable() {
                //    volatile boolean isRunning = true;
                    @Override
                    public void run() {

                            handler.post(new Runnable() {

                                @Override
                                public void run() {

                                    news.setTitle(edTitle.getText().toString());
                                    news.setFulltext(edFulltext.getText().toString());
                                    news.setPublishDate(edDate.getText().toString());
                                }
                            });
                            DAO editDao = db.NewsDao();

                            editDao.update(news);

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                detailLay.setVisibility(View.VISIBLE);
                                editLay.setVisibility(View.GONE);
                                title.setText(news.getTitle());
                                datetext.setText(news.getPublishDate());
                                fulltext.setText(news.getFulltext());

//                                String title = news.getTitle();
//                                String publishDate = news.getPublishDate();
//                                String fullTex = news.getFulltext();
                                Intent intent = new Intent();
                                intent.putExtra(TITLE, news.getTitle());
                                intent.putExtra(FUL_NEWS, news.getFulltext());
                                intent.putExtra(PUBLISH_NEWS, news.getPublishDate());
                                intent.putExtra(Redact_Id, news.getId());
                                setResult(RESULT_FIRST_USER,intent);
                                  //  finish();
                                }
                            });


                    }

                 //   private void stopRun(){
                  //      isRunning = false;
                //    }
                });
                 EditThread.start();
                break;


        }

        return super.onOptionsItemSelected(item);

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
