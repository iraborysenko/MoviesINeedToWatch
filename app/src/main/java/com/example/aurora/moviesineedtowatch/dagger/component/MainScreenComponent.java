package com.example.aurora.moviesineedtowatch.dagger.component;

import com.example.aurora.moviesineedtowatch.dagger.module.MainScreenModule;
import com.example.aurora.moviesineedtowatch.ui.main.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 16/07/18
 * Time: 21:08
 */
@Singleton
@Component(modules = MainScreenModule.class)
public interface MainScreenComponent {
    void inject(MainActivity mainActivity);
}