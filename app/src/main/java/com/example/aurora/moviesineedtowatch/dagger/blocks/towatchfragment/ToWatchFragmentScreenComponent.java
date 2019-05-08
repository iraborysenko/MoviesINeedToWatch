package com.example.aurora.moviesineedtowatch.dagger.blocks.towatchfragment;

import com.example.aurora.moviesineedtowatch.ui.main.towatchtab.ToWatchFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 08/05/19
 * Time: 21:13
 */
@Singleton
@Component(modules = ToWatchFragmentScreenModule.class)
public interface ToWatchFragmentScreenComponent {
    void inject(ToWatchFragment fragment);
}
