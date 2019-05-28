package com.example.aurora.moviesineedtowatch.dagger.blocks.mainsreen.watchedfragment;

import com.example.aurora.moviesineedtowatch.dagger.module.SharedPreferencesModule;
import com.example.aurora.moviesineedtowatch.ui.main.watchedtab.WatchedFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 08/05/19
 * Time: 21:14
 */
@Singleton
@Component(modules = {WatchedFragmentScreenModule.class, SharedPreferencesModule.class})
public interface WatchedFragmentScreenComponent {
    void inject(WatchedFragment fragment);
}
