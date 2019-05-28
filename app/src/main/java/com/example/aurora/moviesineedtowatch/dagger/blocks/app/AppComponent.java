package com.example.aurora.moviesineedtowatch.dagger.blocks.app;

import com.example.aurora.moviesineedtowatch.App;
import com.example.aurora.moviesineedtowatch.adaprers.ToWatchRecyclerAdapter;
import com.example.aurora.moviesineedtowatch.dagger.module.SharedPreferencesModule;
import com.example.aurora.moviesineedtowatch.dagger.wishlist.WishListImpl;
import com.example.aurora.moviesineedtowatch.gson.MovieDeserializer;
import com.example.aurora.moviesineedtowatch.ui.main.MainActivity;
import com.example.aurora.moviesineedtowatch.ui.settings.SettingsActivity;

import javax.inject.Singleton;

import dagger.Component;
/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 18/06/18
 * Time: 09:39
 */
@Singleton
@Component(modules = {AppModule.class, SharedPreferencesModule.class})
public interface AppComponent {
    void inject(App app);
    void inject(WishListImpl wishList);
    void inject(MovieDeserializer movieDeserializer);
    void inject(MainActivity mainActivity);
    void inject(ToWatchRecyclerAdapter toWatchRecyclerAdapter);
    void inject(SettingsActivity settingsActivity);
}

