package com.example.aurora.moviesineedtowatch.dagger.blocks.app;

import android.content.Context;

import com.example.aurora.moviesineedtowatch.App;
import com.example.aurora.moviesineedtowatch.dagger.module.WishListModule;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 18/06/18
 * Time: 09:41
 */
@Module(includes = WishListModule.class)
public class AppModule {

    private final App mApp;

    public AppModule(App mApp) {
        this.mApp = mApp;
    }

    @Provides
    @Singleton
    public App app() {
        return mApp;
    }

    @Provides
    @Singleton
    Context applicationContext() {
        return mApp.getApplicationContext();
    }

    @Provides
    Realm provideRealm() {
        return Realm.getDefaultInstance();
    }
}