package com.example.peter.exercise2.Presenter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.peter.exercise2.CallBackChange;
import com.example.peter.exercise2.Data.INewsInformData;
import com.example.peter.exercise2.Data.NewsInformData;
import com.example.peter.exercise2.NewsEntity;
import com.example.peter.exercise2.R;
import com.example.peter.exercise2.View.INewsDetailsInfoView;

@InjectViewState
public class NewsInformPresenter extends MvpPresenter<INewsDetailsInfoView> implements INewsInformPresenter {
  //  private INewsDetailsInfoView newsInfoView;
    private CallBackChange callBacker;
    private NewsEntity news;
    public NewsInformPresenter(/*INewsDetailsInfoView newsInfoView*/) {
      //  this.newsInfoView = newsInfoView;
    }

    @Override
    public void getNews(int id) {
    INewsInformData data = new NewsInformData();
    news = data.getNews(id,1);
    getViewState().getNews(news);
  //  newsInfoView.getNews(news);
    }

    @Override
    public void editNews(NewsEntity news) {
       // newsInfoView.getEditNews(news);
        getViewState().getEditNews(news);
    }

    @Override
    public void saveNews(int id, NewsEntity snews) {
        INewsInformData data = new NewsInformData();
        news.setTitle(snews.getTitle());
        news.setPublishDate(snews.getPublishDate());
        news.setFulltext(snews.getFulltext());
        data.saveNews(id,3,news);
       // newsInfoView.saveNews(news);
        getViewState().saveNews(news);
    }

    @Override
    public void deleteNews(int id, Context context, Fragment fragment) {
        callBacker = (CallBackChange) context;
        INewsInformData data = new NewsInformData();
        data.deleteNews(id, 2);
        FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
        manager.beginTransaction()
                .remove(fragment)
                .commit();
        callBacker.onItemDelete(id);
    }

    @Override
    public void loadImg(ImageView imgView, String reference, Fragment fragment) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.ic_action_holder);
        requestOptions.centerCrop();
        Glide.with(fragment)
                .setDefaultRequestOptions(requestOptions)
                .load(reference)
                .into(imgView);

    }


}
