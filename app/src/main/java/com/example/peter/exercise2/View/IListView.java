package com.example.peter.exercise2.View;

import com.example.peter.exercise2.Adapters.NewsAdapter;

public interface IListView {
    void loadList(NewsAdapter adapter);
    void errorMessage(String message);
    void showProgress();
    void hideProgress();

}
