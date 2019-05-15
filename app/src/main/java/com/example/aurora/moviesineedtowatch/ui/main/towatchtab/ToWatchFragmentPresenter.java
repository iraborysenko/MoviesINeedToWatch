package com.example.aurora.moviesineedtowatch.ui.main.towatchtab;

import android.view.View;

import com.example.aurora.moviesineedtowatch.adaprers.ToWatchRecyclerAdapter;
import com.example.aurora.moviesineedtowatch.dagger.wishlist.RealmImpl;
import com.example.aurora.moviesineedtowatch.dagger.wishlist.WishList;
import com.example.aurora.moviesineedtowatch.tmdb.Movie;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 08/05/19
 * Time: 20:17
 */
public class ToWatchFragmentPresenter implements ToWatchFragmentScreen.Presenter {

    @Inject
    RealmImpl realmImpl;

    @Inject
    WishList wishList;

    private ToWatchFragmentScreen.View mView;

    @Inject
    ToWatchFragmentPresenter(ToWatchFragmentScreen.View mView) {
        this.mView = mView;
    }


    @Override
    public void loadMovies(){
        List<Movie> movies = getWishList();
        realmImpl.addRealmDataChangeListener(movies, recyclerViewListener(movies));
    }

    private List<Movie> getWishList() {
        return wishList.findAll();
    }

    private ToWatchRecyclerAdapter recyclerViewListener(List<Movie> movies) {

        ToWatchRecyclerAdapter mAdapter = mView.initRecyclerView(movies);

        mAdapter.setOnItemClickListener(new ToWatchRecyclerAdapter.ClickListener() {
            @Override
            public void onItemClick(View v, String movieId, String dataLang) {
                mView.movieTMDB(movieId, dataLang);
            }

            @Override
            public void onItemLongClick(View v, String movieId, String dataLang) {
                wishList.deleteSelectedMovie(movieId, dataLang);
            }

            @Override
            public void onAddEditButtonClick(String movieId) {
                wishList.moveToWatched(movieId);
            }
        });
        return mAdapter;
    }
}
