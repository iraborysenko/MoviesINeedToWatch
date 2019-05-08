package com.example.aurora.moviesineedtowatch.dagger.blocks.mainsreen.watchedfragment;

import com.example.aurora.moviesineedtowatch.dagger.module.WishListModule;
import com.example.aurora.moviesineedtowatch.ui.main.watchedtab.WatchedFragmentScreen;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 08/05/19
 * Time: 21:15
 */
@Module(includes = WishListModule.class)
public class WatchedFragmentScreenModule {
    private final WatchedFragmentScreen.View mView;

    public WatchedFragmentScreenModule(WatchedFragmentScreen.View mView) {
        this.mView = mView;
    }

    @Provides
    WatchedFragmentScreen.View providesWatchedFragmentScreenView() {
        return mView;
    }
}
