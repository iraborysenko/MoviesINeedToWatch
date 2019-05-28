package com.example.aurora.moviesineedtowatch.dagger.blocks.managescreen;

import com.example.aurora.moviesineedtowatch.dagger.module.SharedPreferencesModule;
import com.example.aurora.moviesineedtowatch.ui.manage.ManageActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 19/07/18
 * Time: 14:57
 */
@Singleton
@Component(modules = {ManageScreenModule.class, SharedPreferencesModule.class })
public interface ManageScreenComponent {
    void inject(ManageActivity manageActivity);
}