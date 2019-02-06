package com.example.peter.exercise2.View;

import android.content.Intent;
import android.widget.TextView;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface IAboutView extends MvpView {
    void sendMessage(Intent intent);
    void networks(Intent intent);
    void getView(final TextView disclaimer);
    void sendMessageError(int message);
}
