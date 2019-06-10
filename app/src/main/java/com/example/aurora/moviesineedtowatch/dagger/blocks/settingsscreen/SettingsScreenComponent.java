package com.example.aurora.moviesineedtowatch.dagger.blocks.settingsscreen;

import com.example.aurora.moviesineedtowatch.dagger.module.SharedPreferencesModule;
import com.example.aurora.moviesineedtowatch.ui.settings.SettingsActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 19/07/18
 * Time: 14:57
 */
@Singleton
@Component(modules = {SettingsScreenModule.class, SharedPreferencesModule.class})
public interface SettingsScreenComponent {
    void inject(SettingsActivity settingsActivity);
}