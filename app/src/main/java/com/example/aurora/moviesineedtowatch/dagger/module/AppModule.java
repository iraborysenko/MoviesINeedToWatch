package com.example.aurora.moviesineedtowatch.dagger.module;

import android.content.Context;

import com.example.aurora.moviesineedtowatch.App;

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
@Module
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
    public Context applicationContext() {
        return mApp.getApplicationContext();
    }

    @Provides
    Realm provideRealm() {
        return Realm.getDefaultInstance();
    }
}