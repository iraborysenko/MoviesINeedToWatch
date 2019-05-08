package com.example.aurora.moviesineedtowatch.ui.main.towatchtab;

import com.example.aurora.moviesineedtowatch.adaprers.ToWatchRecyclerAdapter;
import com.example.aurora.moviesineedtowatch.tmdb.Movie;

import java.util.List;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 08/05/19
 * Time: 20:16
 */
public interface ToWatchFragmentScreen {
    interface View {

        ToWatchRecyclerAdapter initRecyclerView(List<Movie> movies);

        void movieTMDB(String movieId, String dataLang);

    }

    interface Presenter {

        void loadMovies();

    }
}
