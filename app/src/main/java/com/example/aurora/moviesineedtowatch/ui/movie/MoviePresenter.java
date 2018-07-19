package com.example.aurora.moviesineedtowatch.ui.movie;

import com.example.aurora.moviesineedtowatch.dagger.wishlist.WishList;
import com.example.aurora.moviesineedtowatch.tmdb.Movie;

import javax.inject.Inject;


/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 19/07/18
 * Time: 14:52
 */
public class MoviePresenter implements MovieScreen.Presenter {
    private MovieScreen.View mView;

    @Inject
    WishList wishList;

    @Inject
    MoviePresenter(MovieScreen.View mView) {
        this.mView = mView;
    }

    @Override
    public void loadSelectedMovie(String movieId, String dataLang) {
        Movie curMovie = wishList.chooseSelectedMovie(movieId, dataLang);
        mView.setMovieInfo(curMovie);
    }
}
