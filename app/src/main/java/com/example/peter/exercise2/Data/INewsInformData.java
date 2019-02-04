package com.example.peter.exercise2.Data;

import android.support.v4.app.Fragment;

import com.example.peter.exercise2.NewsEntity;

public interface INewsInformData {
          NewsEntity getNews(int id, int marker);
          void saveNews(int id, int marker, NewsEntity news);
          void deleteNews(int id, int marker);

}
