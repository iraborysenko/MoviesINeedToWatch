package com.example.aurora.moviesineedtowatch.dagger;

import com.example.aurora.moviesineedtowatch.CustomApplication;
import com.example.aurora.moviesineedtowatch.ui.MainActivity;

import javax.inject.Singleton;

import dagger.Component;
/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 18/06/18
 * Time: 09:39
 */
@Singleton
@Component(modules = {ApplicationContextModule.class, WishListModule.class})
public interface ApplicationComponent {

    void inject(CustomApplication application);
    void inject(MainActivity mainActivity);

    void inject(DatabaseRealm databaseRealm);
    void inject(WishListImpl wishList);

}

