package com.example.peter.exercise2.Presenter;



import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.example.peter.exercise2.NewsEntity;

public interface INewsInformPresenter {
    void getNews(int id);
    void editNews(NewsEntity news);
    void saveNews(int id, NewsEntity snews);
    void deleteNews(int id, Context context, Fragment fragment);
    void loadImg(ImageView img, String reference, Fragment fragment);

}
