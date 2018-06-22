package com.example.aurora.moviesineedtowatch.dagger.component;

import com.example.aurora.moviesineedtowatch.CustomApplication;
import com.example.aurora.moviesineedtowatch.dagger.module.ApplicationContextModule;
import com.example.aurora.moviesineedtowatch.dagger.wishlist.DatabaseRealm;
import com.example.aurora.moviesineedtowatch.dagger.wishlist.WishListImpl;
import com.example.aurora.moviesineedtowatch.dagger.module.NetModule;
import com.example.aurora.moviesineedtowatch.dagger.module.WishListModule;
import com.example.aurora.moviesineedtowatch.gson.MovieDeserializer;
import com.example.aurora.moviesineedtowatch.ui.MainActivity;
import com.example.aurora.moviesineedtowatch.ui.MovieActivity;
import com.example.aurora.moviesineedtowatch.ui.SearchActivity;

import javax.inject.Singleton;

import dagger.Component;
/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 18/06/18
 * Time: 09:39
 */
@Singleton
@Component(modules = {ApplicationContextModule.class, WishListModule.class, NetModule.class})
public interface ApplicationComponent {

    void inject(CustomApplication application);
    void inject(MainActivity mainActivity);
    void inject(SearchActivity searchActivity);
    void inject(MovieActivity movieActivity);

    void inject(DatabaseRealm databaseRealm);
    void inject(WishListImpl wishList);
    void inject(MovieDeserializer movieDeserializer);

}

