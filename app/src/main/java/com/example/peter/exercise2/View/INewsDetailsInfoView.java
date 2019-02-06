package com.example.peter.exercise2.View;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.example.peter.exercise2.NewsEntity;

public interface INewsDetailsInfoView extends MvpView {
    @StateStrategyType(SingleStateStrategy.class)
    void getNews(NewsEntity news);
    @StateStrategyType(OneExecutionStateStrategy.class)
    void getEditNews(NewsEntity news);
    @StateStrategyType(SingleStateStrategy.class)
    void saveNews(NewsEntity news);
    void deleteNews(int id);
}
