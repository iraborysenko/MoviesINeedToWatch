package com.example.aurora.moviesineedtowatch;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.example.aurora.moviesineedtowatch.dagger.blocks.app.AppComponent;
import com.example.aurora.moviesineedtowatch.dagger.blocks.app.AppModule;
import com.example.aurora.moviesineedtowatch.dagger.blocks.app.DaggerAppComponent;
import com.example.aurora.moviesineedtowatch.dagger.module.SharedPreferencesModule;
import com.example.aurora.moviesineedtowatch.tools.LocaleHelper;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

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
    public static final List<String> LANG_VALUES = Arrays.asList("en", "ru", "uk");

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
        String defaultLang;
        if (LANG_VALUES.contains(Locale.getDefault().getLanguage())) {
            defaultLang = Locale.getDefault().getLanguage();
        } else {
            defaultLang = "en";
        }
        super.attachBaseContext(LocaleHelper.onAttach(newBase, defaultLang));
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