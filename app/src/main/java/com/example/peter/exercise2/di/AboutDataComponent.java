package com.example.peter.exercise2.di;

import com.example.peter.exercise2.Presenter.AboutPresenter;



import dagger.Component;
import dagger.Subcomponent;

@AboutScope
@Subcomponent(modules = {AboutDataModule.class})
public interface AboutDataComponent {

void inject(AboutPresenter aboutPresenter);
}
