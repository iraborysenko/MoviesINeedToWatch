package com.example.aurora.moviesineedtowatch.dagger.blocks.mainsreen;

import com.example.aurora.moviesineedtowatch.dagger.module.WishListModule;
import com.example.aurora.moviesineedtowatch.ui.main.MainScreen;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 16/07/18
 * Time: 20:10
 */
@Module(includes = WishListModule.class)
public class MainScreenModule {
    private final MainScreen.View mView;

    public MainScreenModule(MainScreen.View mView) {
        this.mView = mView;
    }

    @Provides
    MainScreen.View providesMainScreenView() {
        return mView;
    }
}