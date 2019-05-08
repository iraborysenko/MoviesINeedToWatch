package com.example.aurora.moviesineedtowatch.dagger.blocks.moviescreen;

import com.example.aurora.moviesineedtowatch.dagger.module.WishListModule;
import com.example.aurora.moviesineedtowatch.ui.movie.MovieScreen;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 19/07/18
 * Time: 14:55
 */
@Module(includes = WishListModule.class)
public class MovieScreenModule {
    private final MovieScreen.View mView;

    public MovieScreenModule(MovieScreen.View mView) {
        this.mView = mView;
    }

    @Provides
    MovieScreen.View providesMovieScreenView() {
        return mView;
    }
}