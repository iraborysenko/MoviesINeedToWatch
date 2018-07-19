package com.example.aurora.moviesineedtowatch.ui.movie;

import com.example.aurora.moviesineedtowatch.tmdb.Movie;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 19/07/18
 * Time: 14:52
 */
public interface MovieScreen {
    interface View {
        void setMovieInfo(Movie curMovie);
    }

    interface Presenter {
        void loadSelectedMovie(String movieId, String dataLang);
    }
}