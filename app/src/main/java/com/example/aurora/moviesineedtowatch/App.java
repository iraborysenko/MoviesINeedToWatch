package com.example.aurora.moviesineedtowatch;

import android.app.Application;

import com.example.aurora.moviesineedtowatch.dagger.component.AppComponent;
import com.example.aurora.moviesineedtowatch.dagger.component.DaggerAppComponent;
import com.example.aurora.moviesineedtowatch.dagger.module.AppModule;
import com.example.aurora.moviesineedtowatch.dagger.module.NetModule;
import com.example.aurora.moviesineedtowatch.dagger.module.WishListModule;

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

        initRealm();

        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .wishListModule(new WishListModule())
                .netModule(new NetModule())
                .build();
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