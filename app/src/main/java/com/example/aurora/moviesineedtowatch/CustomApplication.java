package com.example.aurora.moviesineedtowatch;

import android.app.Application;

import com.example.aurora.moviesineedtowatch.dagger.DatabaseRealm;
import com.example.aurora.moviesineedtowatch.dagger.Injector;

import javax.inject.Inject;
/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 18/06/18
 * Time: 09:33
 */
public class CustomApplication extends Application {

    @Inject
    DatabaseRealm databaseRealm;

    @Override
    public void onCreate() {
        super.onCreate();
        initDagger();
        initRealm();
    }

    protected void initDagger() {
        Injector.initializeApplicationComponent(this);
        Injector.getApplicationComponent().inject(this);
    }

    protected void initRealm() {
        databaseRealm.setup();
    }

}