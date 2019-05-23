package com.example.aurora.moviesineedtowatch.ui.main.towatchtab;

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
    public void loadToWatchMovies(){
        List<Movie> movies = getWishList();
        realmImpl.addRealmDataChangeListener(movies, recyclerViewListener(movies));
    }

    private List<Movie> getWishList() {
        return wishList.findAllToWatch();
    }

    private ToWatchRecyclerAdapter recyclerViewListener(List<Movie> movies) {

        ToWatchRecyclerAdapter mAdapter = mView.initRecyclerView(movies);

        mAdapter.setOnItemClickListener(new ToWatchRecyclerAdapter.ClickListener() {
            @Override
            public void onItemClick(String movieId) {
                mView.movieToWatchDetails(movieId);
            }

            @Override
            public void onAddEditButtonClick(String movieId) {
                mView.movieAddEditDetails(movieId);
            }

            @Override
            public void onMoveToOtherTab(String movieId) {
                wishList.moveToOtherTab(movieId, true);
            }

            @Override
            public void onDeleteItem(String movieId) {
                wishList.deleteSelectedMovie(movieId);
            }
        });
        return mAdapter;
    }
}
