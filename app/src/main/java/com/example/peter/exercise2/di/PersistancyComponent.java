package com.example.peter.exercise2.di;



import dagger.Component;

@Component(modules = {ContextModule.class, PersistancyModule.class})
public interface PersistancyComponent {
    PrefDataComponent createPrefdataComponent(PreferenceModule preferenceModule);
    AboutDataComponent createAboutComponent(AboutDataModule aboutDataModule);
}
