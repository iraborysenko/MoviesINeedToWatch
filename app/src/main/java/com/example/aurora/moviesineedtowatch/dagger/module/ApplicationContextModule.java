package com.example.aurora.moviesineedtowatch.dagger.module;

import android.content.Context;

import com.example.aurora.moviesineedtowatch.CustomApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 18/06/18
 * Time: 09:41
 */
@Module
public class ApplicationContextModule {

    private final CustomApplication application;

    public ApplicationContextModule(CustomApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public CustomApplication application() {
        return application;
    }

    @Provides
    @Singleton
    public Context applicationContext() {
        return application.getApplicationContext();
    }


}