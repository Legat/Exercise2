package com.example.peter.exercise2.Data;

import android.content.Context;

import com.example.peter.exercise2.NewsEntity;

import java.util.List;

public interface IListData {
    List<NewsEntity> loadList(int marker);
    List<NewsEntity> updateList();
    List<NewsEntity> filterList(String arg);
    boolean dataBaseExist();
    boolean internetConnection(Context context);
    int screenSize(Context context);
    String screenType(Context context);
}
