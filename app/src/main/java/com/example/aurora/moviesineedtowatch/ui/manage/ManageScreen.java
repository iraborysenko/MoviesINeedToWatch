package com.example.aurora.moviesineedtowatch.ui.manage;

import com.example.aurora.moviesineedtowatch.tmdb.Movie;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 16/05/19
 * Time: 19:13
 */
public interface ManageScreen {
    interface View {

        void setMovieInfo(Movie curMovie);

    }

    interface Presenter {

        void loadWatchedMovie(String movieId);

    }
}
