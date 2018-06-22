package com.example.aurora.moviesineedtowatch.dagger;

import com.example.aurora.moviesineedtowatch.CustomApplication;
import com.example.aurora.moviesineedtowatch.dagger.component.ApplicationComponent;
import com.example.aurora.moviesineedtowatch.dagger.component.DaggerApplicationComponent;
import com.example.aurora.moviesineedtowatch.dagger.module.ApplicationContextModule;
import com.example.aurora.moviesineedtowatch.dagger.module.NetModule;
import com.example.aurora.moviesineedtowatch.dagger.module.WishListModule;

import java.util.Objects;
/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 18/06/18
 * Time: 09:38
 */
public class Injector {

    private static ApplicationComponent applicationComponent;

    private Injector() {}

    public static void initializeApplicationComponent(CustomApplication customApplication) {
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationContextModule(new ApplicationContextModule(customApplication))
                .wishListModule(new WishListModule())
                .netModule(new NetModule())
                .build();
    }

    public static ApplicationComponent getApplicationComponent() {
        Objects.requireNonNull(applicationComponent, "applicationComponent is null");
        return applicationComponent;
    }

}