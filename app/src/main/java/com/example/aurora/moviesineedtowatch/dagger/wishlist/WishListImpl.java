package com.example.aurora.moviesineedtowatch.dagger.wishlist;

import com.example.aurora.moviesineedtowatch.App;
import com.example.aurora.moviesineedtowatch.tmdb.Movie;

import java.util.List;

import javax.inject.Inject;

import static io.realm.internal.SyncObjectServerFacade.getApplicationContext;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 18/06/18
 * Time: 09:34
 */
public class WishListImpl implements WishList {

    @Inject
    RealmImpl realmImpl;

    public WishListImpl() {
        ((App) getApplicationContext()).getApplicationComponent().inject(this);
    }

    @Override
    public List<Movie> findAllToWatch() {
        return realmImpl.findAllToWatch();
    }

    @Override
    public List<Movie> findAllWatched() {
        return realmImpl.findAllWatched();
    }

    @Override
    public void addSelectedMovie(Movie movie) {
        realmImpl.add(movie);
    }

    @Override
    public void deleteSelectedMovie(String movieId) {
        realmImpl.delete(movieId);
    }

    @Override
    public Movie chooseSelectedMovie(String movieId) {
        return realmImpl.choose(movieId);
    }

    @Override
    public void moveToOtherTab(String movieId, Boolean watchedFlag) {
        realmImpl.move(movieId, watchedFlag);
    }

    @Override
    public void saveUserData(String movieId, String commentStr, String myRatingStr) {
        realmImpl.moveWithData(movieId, commentStr, myRatingStr);
    }
}
