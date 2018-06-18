package com.example.aurora.moviesineedtowatch.dagger;

import com.example.aurora.moviesineedtowatch.tmdb.Movie;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 18/06/18
 * Time: 09:34
 */
public class WishListImpl implements WishList {

    @Inject
    DatabaseRealm databaseRealm;

    WishListImpl() {
        Injector.getApplicationComponent().inject(this);
    }

    @Override
    public List<Movie> findAll() {
        return databaseRealm.findAll(Movie.class);
    }
}
