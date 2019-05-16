package com.example.aurora.moviesineedtowatch.ui.manage;

import com.example.aurora.moviesineedtowatch.dagger.wishlist.WishList;
import com.example.aurora.moviesineedtowatch.tmdb.Movie;

import javax.inject.Inject;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 16/05/19
 * Time: 19:13
 */
public class ManagePresenter implements ManageScreen.Presenter {
    private ManageScreen.View mView;

    @Inject
    WishList wishList;

    @Inject
    ManagePresenter(ManageScreen.View mView) {
        this.mView = mView;
    }

    @Override
    public void loadWatchedMovie(String movieId, String dataLang) {
        Movie curMovie = wishList.chooseSelectedMovie(movieId, dataLang);
        mView.setMovieInfo(curMovie);
    }
}
