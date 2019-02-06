package com.example.peter.exercise2.Presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.peter.exercise2.View.IAboutView;


public interface IAboutPresenter  {

    void network(int idView);
    void addView(Context context);
    void sendMessage(String message, Context context);
}
