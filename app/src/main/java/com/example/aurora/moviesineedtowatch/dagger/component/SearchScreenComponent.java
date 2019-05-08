package com.example.aurora.moviesineedtowatch.dagger.component;

import com.example.aurora.moviesineedtowatch.dagger.module.ContextModule;
import com.example.aurora.moviesineedtowatch.dagger.module.SharedPreferencesModule;
import com.example.aurora.moviesineedtowatch.dagger.module.screens.SearchScreenModule;
import com.example.aurora.moviesineedtowatch.ui.search.SearchActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 19/07/18
 * Time: 15:53
 */
@Singleton
@Component(modules = {ContextModule.class, SearchScreenModule.class, SharedPreferencesModule.class})
public interface SearchScreenComponent {
    void inject(SearchActivity searchActivity);
}
