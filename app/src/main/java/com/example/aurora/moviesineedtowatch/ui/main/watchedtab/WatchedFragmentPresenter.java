package com.example.aurora.moviesineedtowatch.ui.main.watchedtab;

import android.view.View;

import com.example.aurora.moviesineedtowatch.adaprers.WatchedRecyclerAdapter;
import com.example.aurora.moviesineedtowatch.dagger.wishlist.RealmImpl;
import com.example.aurora.moviesineedtowatch.dagger.wishlist.WishList;
import com.example.aurora.moviesineedtowatch.tmdb.Movie;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 08/05/19
 * Time: 20:18
 */
public class WatchedFragmentPresenter implements WatchedFragmentScreen.Presenter {

    @Inject
    RealmImpl realmImpl;

    @Inject
    WishList wishList;

    private WatchedFragmentScreen.View mView;

    @Inject
    WatchedFragmentPresenter(WatchedFragmentScreen.View mView) {
        this.mView = mView;
    }

    @Override
    public void loadWatchedMovies() {
        List<Movie> movies = getWishList();
        realmImpl.addRealmDataChangeListener(movies, recyclerViewListener(movies));
    }

    private List<Movie> getWishList() {
        return wishList.findAllWatched();
    }

    private WatchedRecyclerAdapter recyclerViewListener(List<Movie> movies) {

        WatchedRecyclerAdapter mAdapter = mView.initRecyclerView(movies);

        mAdapter.setOnItemClickListener(new WatchedRecyclerAdapter.ClickListener() {
            @Override
            public void onItemClick(View v, String movieId, String dataLang) {

            }

            @Override
            public void onItemLongClick(View v, String movieId, String dataLang) {
                wishList.deleteSelectedMovie(movieId, dataLang);
            }
        });
        return mAdapter;
    }
}
