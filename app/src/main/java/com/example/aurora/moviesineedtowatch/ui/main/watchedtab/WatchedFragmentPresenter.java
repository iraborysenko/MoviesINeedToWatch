package com.example.aurora.moviesineedtowatch.ui.main.watchedtab;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aurora.moviesineedtowatch.adaprers.WatchedRecyclerAdapter;
import com.example.aurora.moviesineedtowatch.dagger.wishlist.RealmImpl;
import com.example.aurora.moviesineedtowatch.dagger.wishlist.WishList;
import com.example.aurora.moviesineedtowatch.tmdb.Movie;

import java.util.List;

import javax.inject.Inject;

import static io.realm.internal.SyncObjectServerFacade.getApplicationContext;

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
    private WatchedRecyclerAdapter mAdapter;

    @Inject
    WatchedFragmentPresenter(WatchedFragmentScreen.View mView) {
        this.mView = mView;
    }

    @Override
    public void loadWatchedMovies() {
        List<Movie> movies = getWishList();
        realmImpl.addRealmDataChangeListener(movies, recyclerViewListener(movies));
    }

    @Override
    public void updateList(RecyclerView mRecyclerView) {
        if (mAdapter!=null) {
            mRecyclerView.setAdapter(null);
            mRecyclerView.setLayoutManager(null);
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            mAdapter.notifyDataSetChanged();
        }
    }

    private List<Movie> getWishList() {
        return wishList.findAllWatched();
    }

    private WatchedRecyclerAdapter recyclerViewListener(List<Movie> movies) {

        mAdapter = mView.initRecyclerView(movies);

        mAdapter.setOnItemClickListener(new WatchedRecyclerAdapter.ClickListener() {

            @Override
            public void onItemClick(String movieId) {
                mView.movieWatchedDetails(movieId);
            }

            @Override
            public void onMoveToOtherTab(String movieId) {
                wishList.moveToOtherTab(movieId, false);
            }

            @Override
            public void onDeleteItem(String movieId) {
                wishList.deleteSelectedMovie(movieId);
            }

        });
        return mAdapter;
    }
}
