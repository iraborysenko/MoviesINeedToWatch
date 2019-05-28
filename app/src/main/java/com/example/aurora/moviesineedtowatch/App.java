package com.example.aurora.moviesineedtowatch;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.example.aurora.moviesineedtowatch.dagger.blocks.app.AppComponent;
import com.example.aurora.moviesineedtowatch.dagger.blocks.app.AppModule;
import com.example.aurora.moviesineedtowatch.dagger.blocks.app.DaggerAppComponent;
import com.example.aurora.moviesineedtowatch.dagger.module.SharedPreferencesModule;
import com.example.aurora.moviesineedtowatch.tools.LocaleHelper;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 18/06/18
 * Time: 09:33
 */
public class App extends Application {

    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        initRealm();

        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .sharedPreferencesModule(new SharedPreferencesModule(getApplicationContext()))
                .build();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase, "en"));
    }

    protected void initRealm() {
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }

    public AppComponent getApplicationComponent() {
        return mAppComponent;
    }
}