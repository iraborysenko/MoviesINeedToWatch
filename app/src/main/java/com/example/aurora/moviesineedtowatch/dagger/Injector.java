package com.example.aurora.moviesineedtowatch.dagger;

import com.example.aurora.moviesineedtowatch.CustomApplication;

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
                .build();
    }

    public static ApplicationComponent getApplicationComponent() {
        Objects.requireNonNull(applicationComponent, "applicationComponent is null");
        return applicationComponent;
    }

}

