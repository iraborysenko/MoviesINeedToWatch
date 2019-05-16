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
    public List<Movie> findAll() {
        return realmImpl.findAll();
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
    public void deleteSelectedMovie(String movieId, String dataLang) {
        realmImpl.delete(movieId, dataLang);
    }

    @Override
    public Movie chooseSelectedMovie(String movieId, String dataLang) {
        return realmImpl.choose(movieId, dataLang);
    }

    @Override
    public void moveToWatched(String movieId) {
        realmImpl.move(movieId);
    }
}
