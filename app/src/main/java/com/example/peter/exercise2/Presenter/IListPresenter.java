package com.example.peter.exercise2.Presenter;

import android.content.Context;

public interface IListPresenter {

    void loadList(Context context, int marker);
    void updateList();
    void setResult();
    void filterList(Context context, String arg);
    void setErorr();
}
