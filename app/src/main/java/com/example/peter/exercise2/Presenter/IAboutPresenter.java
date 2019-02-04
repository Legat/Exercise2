package com.example.peter.exercise2.Presenter;

import android.content.Context;

public interface IAboutPresenter {
    void network(int idView);
    void addView(Context context);
    void sendMessage(String message, Context context);
}
