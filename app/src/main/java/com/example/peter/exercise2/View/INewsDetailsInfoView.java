package com.example.peter.exercise2.View;

import com.example.peter.exercise2.NewsEntity;

public interface INewsDetailsInfoView {
    void getNews(NewsEntity news);
    void getEditNews(NewsEntity news);
    void saveNews(NewsEntity news);
    void deleteNews(int id);
}
