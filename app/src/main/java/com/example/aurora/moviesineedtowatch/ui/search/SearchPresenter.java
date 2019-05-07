package com.example.aurora.moviesineedtowatch.ui.search;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.example.aurora.moviesineedtowatch.adaprer.SearchRecyclerAdapter;
import com.example.aurora.moviesineedtowatch.dagger.wishlist.WishList;
import com.example.aurora.moviesineedtowatch.retrofit.ApiInterface;
import com.example.aurora.moviesineedtowatch.tools.Constants;
import com.example.aurora.moviesineedtowatch.tmdb.FoundMovie;
import com.example.aurora.moviesineedtowatch.tmdb.Movie;
import com.example.aurora.moviesineedtowatch.tmdb.SearchResult;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.aurora.moviesineedtowatch.tools.Constants.EN;
import static com.example.aurora.moviesineedtowatch.tools.Constants.RU;
import static com.example.aurora.moviesineedtowatch.tools.Constants.TMDB_KEY;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 19/07/18
 * Time: 16:00
 */
public class SearchPresenter implements SearchScreen.Presenter {
    private SearchScreen.View mView;

    @Inject
    WishList wishList;

    @Inject
    ApiInterface apiInterface;

    @Inject
    SearchPresenter(SearchScreen.View mView) {
        this.mView = mView;
    }

    @Override
    public void recyclerViewListener(SearchRecyclerAdapter mAdapter) {
        mAdapter.setOnItemClickListener(new SearchRecyclerAdapter.ClickListener() {
            @Override
            public void onItemClick(View v, String movieId) {
                Log.d(Constants.DEBUG, "It's onclick");
            }

            @Override
            public void onItemLongClick(View v, String movieId) {
                loadMovieInfo(movieId);
            }
        });
    }

    @Override
    public void editSearchField(String searchQuery) {
        mView.setProgressBarVisible();
        Call<SearchResult> call = apiInterface.getSearchResult((mView.getSwitchValue()?EN:RU),
                TMDB_KEY, searchQuery);
        call.enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(@NonNull Call<SearchResult>call, @NonNull Response<SearchResult> response) {
                SearchResult result = response.body();
                assert result != null;
                mView.setNotificationField(result.getTotalResults());
                FoundMovie[] search = result.getResults();
                mView.initRecyclerView(search);
                mView.setProgressBarInvisible();
            }

            @Override
            public void onFailure(@NonNull Call<SearchResult>call, @NonNull Throwable t) {
                Log.e(Constants.SEE, t.toString());
            }
        });
    }

    private void loadMovieInfo(String movieId) {
        mView.setProgressBarVisible();

        Call<Movie> call =
                apiInterface.getMovie(Integer.parseInt(movieId),(mView.getSwitchValue()?EN:RU), TMDB_KEY);
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(@NonNull Call<Movie>call, @NonNull Response<Movie> response) {
                Movie movie = response.body();
                assert movie != null;
                wishList.addSelectedMovie(movie);
                mView.setProgressBarInvisible();
                mView.showAddedMovieToast(movie.getTitle());
            }

            @Override
            public void onFailure(@NonNull Call<Movie>call, @NonNull Throwable t) {
                Log.e(Constants.SEE, t.toString());
            }
        });
    }
}
