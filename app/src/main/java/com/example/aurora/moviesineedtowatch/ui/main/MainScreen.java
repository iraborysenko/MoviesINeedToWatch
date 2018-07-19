package com.example.aurora.moviesineedtowatch.ui.main;

import com.example.aurora.moviesineedtowatch.adaprer.MainRecyclerAdapter;
import com.example.aurora.moviesineedtowatch.tmdb.Movie;

import java.util.List;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 16/07/18
 * Time: 20:05
 */
public interface MainScreen {
    interface View {
        MainRecyclerAdapter initRecyclerView(List<Movie> movies);
        void movieTMDB(String movieId, String dataLang);
        void searchTMDB();
    }

    interface Presenter {
        void loadMovies();
        void searchButton();
    }
}
