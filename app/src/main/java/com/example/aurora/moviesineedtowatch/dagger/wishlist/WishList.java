package com.example.aurora.moviesineedtowatch.dagger.wishlist;

import com.example.aurora.moviesineedtowatch.tmdb.Movie;

import java.util.List;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 18/06/18
 * Time: 09:37
 */
public interface WishList {

    List<Movie> findAll();

    void addSelectedMovie(Movie movie);

    void deleteSelectedMovie(String movieId, String dataLang);

    Movie chooseSelectedMovie(String movieId, String dataLang);
}
