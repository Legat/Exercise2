package com.example.peter.exercise2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;
import com.example.peter.exercise2.Models.NewsItem;

public class NewsDetailsActivity extends AppCompatActivity {
    private TextView title, datetext, fulltext;
    private ImageView imageNews;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        title = findViewById(R.id.main_title);
        datetext = findViewById(R.id.date_text);
        fulltext = findViewById(R.id.full_text);
        imageNews = findViewById(R.id.image_new);

        NewsItem news = (NewsItem) getIntent().getSerializableExtra(NewsItem.class.getSimpleName());
       // setTitle(news.getCategory().getName());
        title.setText(news.getTitle());
        datetext.setText(news.getPublishDate().toString());
        fulltext.setText(news.getFullText());

        GlideApp.with(this)
                 .load(news.getImageUrl())
                 .centerCrop()
                 .placeholder(R.drawable.ic_action_holder)
                 .into(imageNews);



    }


}
