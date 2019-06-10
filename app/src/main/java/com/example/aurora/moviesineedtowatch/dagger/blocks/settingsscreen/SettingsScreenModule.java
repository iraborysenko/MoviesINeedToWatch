package com.example.aurora.moviesineedtowatch.dagger.blocks.settingsscreen;

import com.example.aurora.moviesineedtowatch.dagger.module.WishListModule;
import com.example.aurora.moviesineedtowatch.ui.settings.SettingsScreen;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 19/07/18
 * Time: 14:55
 */
@Module(includes = WishListModule.class)
public class SettingsScreenModule {
    private final SettingsScreen.View mView;

    public SettingsScreenModule(SettingsScreen.View mView) {
        this.mView = mView;
    }

    @Provides
    SettingsScreen.View providesSettingsScreenView() {
        return mView;
    }
}