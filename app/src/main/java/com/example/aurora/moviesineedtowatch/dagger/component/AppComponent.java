package com.example.aurora.moviesineedtowatch.dagger.component;

import com.example.aurora.moviesineedtowatch.App;
import com.example.aurora.moviesineedtowatch.dagger.module.AppModule;
import com.example.aurora.moviesineedtowatch.dagger.module.SharedPreferencesModule;
import com.example.aurora.moviesineedtowatch.dagger.wishlist.WishListImpl;
import com.example.aurora.moviesineedtowatch.dagger.module.NetModule;
import com.example.aurora.moviesineedtowatch.dagger.module.WishListModule;
import com.example.aurora.moviesineedtowatch.gson.MovieDeserializer;
import com.example.aurora.moviesineedtowatch.ui.main.MainPresenter;
import com.example.aurora.moviesineedtowatch.ui.movie.MovieActivity;
import com.example.aurora.moviesineedtowatch.ui.search.SearchActivity;

import javax.inject.Singleton;

import dagger.Component;
/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 18/06/18
 * Time: 09:39
 */
@Singleton
@Component(modules = {AppModule.class, WishListModule.class, NetModule.class,
        SharedPreferencesModule.class})
public interface AppComponent {

    void inject(App app);
    void inject(SearchActivity searchActivity);
    void inject(MovieActivity movieActivity);
    void inject(WishListImpl wishList);
    void inject(MovieDeserializer movieDeserializer);
    void inject(MainPresenter mainPresenter);
}

