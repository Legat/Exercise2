package com.example.peter.exercise2.di;

import com.example.peter.exercise2.Data.AboutData;



import dagger.Module;
import dagger.Provides;

@Module
public class AboutDataModule {

    @Provides
    @AboutScope
    AboutData getAboutData(){
        return new AboutData();
    }
}
