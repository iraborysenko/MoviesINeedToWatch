package com.example.aurora.moviesineedtowatch.ui.main.watchedtab;

import androidx.recyclerview.widget.RecyclerView;

import com.example.aurora.moviesineedtowatch.adaprers.WatchedRecyclerAdapter;
import com.example.aurora.moviesineedtowatch.tmdb.Movie;

import java.util.List;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 08/05/19
 * Time: 20:17
 */
public interface WatchedFragmentScreen {
    interface View {

        WatchedRecyclerAdapter initRecyclerView(List<Movie> movies);

        void movieWatchedDetails(String movieId);

    }

    interface Presenter {

        void loadWatchedMovies();

        void updateList(RecyclerView mRecyclerView);

    }
}
