package com.example.peter.exercise2.Presenter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.example.peter.exercise2.Adapters.NewsAdapter;
import com.example.peter.exercise2.Data.ListData;
import com.example.peter.exercise2.NewsEntity;
import com.example.peter.exercise2.View.IListView;

import java.util.List;

public class ListPresenter implements IListPresenter {
    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    private IListView listView;
    private List<NewsEntity> news;

    public ListPresenter(IListView listView) {
        this.listView = listView;
    }

//    @Override
//    public void showProgress() {
//
//    }
//
//    @Override
//    public void hideProgress() {
//
//    }

    @Override
    public void loadList(Context context, int marker) {
        listView.showProgress();
        ListData data = new ListData();
        if (data.internetConnection(context)){
        news = data.loadList(marker);
        int size = data.screenSize(context);
        adapter = new NewsAdapter(context, news, size);
        listView.loadList(adapter);
        listView.hideProgress();
    } else{
        setErorr();
    }
    }

    @Override
    public void updateList() {
        listView.showProgress();

    }

    @Override
    public void setResult() {

    }

    @Override
    public void filterList(Context context, String arg) {
    listView.showProgress();
        ListData data = new ListData();
        if (data.internetConnection(context)) {
            news = data.filterList(arg);
            int size = data.screenSize(context);
            adapter = new NewsAdapter(context, news, size);
            listView.loadList(adapter);
            listView.hideProgress();
        }else {
            setErorr();
        }
    }

    @Override
    public void setErorr() {
        listView.errorMessage("Internet connection is not available");
    }
}
