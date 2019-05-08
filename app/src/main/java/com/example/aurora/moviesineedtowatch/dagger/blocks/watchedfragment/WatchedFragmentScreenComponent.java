package com.example.aurora.moviesineedtowatch.dagger.blocks.watchedfragment;

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
@Component(modules = WatchedFragmentScreenModule.class)
public interface WatchedFragmentScreenComponent {
    void inject(WatchedFragment fragment);
}
