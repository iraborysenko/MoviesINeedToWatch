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

    List<Movie> findAllToWatch();

    List<Movie> findAllWatched();

    void addSelectedMovie(Movie movie);

    void deleteSelectedMovie(String movieId);

    Movie chooseSelectedMovie(String movieId);

    void moveToOtherTab(String movieId, Boolean watchedFlag);

    void saveUserData(String movieId, String commentStr, String myRatingStr);
}