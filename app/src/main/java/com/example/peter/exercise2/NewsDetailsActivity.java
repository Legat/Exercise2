package com.example.peter.exercise2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;


import com.example.peter.exercise2.Models.Result;

public class NewsDetailsActivity extends AppCompatActivity {
//    private TextView title, datetext, fulltext;
    private ImageView imageNews;
    private WebView page;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("New York Times");
        setContentView(R.layout.activity_news_details);
        String url = getIntent().getExtras().getString(Result.class.getSimpleName());
        String imageurl = getIntent().getExtras().getString(Result.class.getName());
        imageNews = findViewById(R.id.image_new);
        page = findViewById(R.id.web_page);
      //  page.getSettings().setJavaScriptEnabled(true);
        page.loadUrl(url);
        page.setHorizontalScrollBarEnabled(false);
//        title = findViewById(R.id.main_title);
//        datetext = findViewById(R.id.date_text);
//        fulltext = findViewById(R.id.full_text);


      //  NewsItem news = (NewsItem) getIntent().getSerializableExtra(NewsItem.class.getSimpleName());


       // setTitle(news.getCategory().getName());
//        title.setText(news.getTitle());
//        datetext.setText(news.getPublishDate().toString());
//        fulltext.setText(news.getFullText());
//
        GlideApp.with(this)
                 .load(imageurl)
                 .centerCrop()
                 .placeholder(R.drawable.ic_action_holder)
                 .into(imageNews);



    }


}
