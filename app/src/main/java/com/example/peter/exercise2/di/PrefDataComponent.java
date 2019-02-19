package com.example.peter.exercise2.di;

import com.example.peter.exercise2.ListFragment;

import dagger.Subcomponent;

@DataSharedScope
@Subcomponent(modules = {PreferenceModule.class, PersistancyModule.class})
public interface PrefDataComponent {
    void inject(ListFragment listFragment);
}
