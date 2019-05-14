package com.example.aurora.moviesineedtowatch.ui.search;

import android.support.v7.widget.SearchView;

import com.example.aurora.moviesineedtowatch.adaprers.SearchRecyclerAdapter;
import com.example.aurora.moviesineedtowatch.tmdb.FoundMovie;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 19/07/18
 * Time: 16:00
 */
public interface SearchScreen {
    interface View {
        void setNotificationField(String totalResults);
        void setProgressBarVisible();
        void setProgressBarInvisible();
        Boolean getSwitchValue();
        void showAddedMovieToast(String movieTitle);
        void initRecyclerView(FoundMovie[] search);
    }

    interface Presenter {
        void recyclerViewListener(SearchRecyclerAdapter mAdapter);
        void getResultsBasedOnQuery(SearchView searchView);
    }
}
