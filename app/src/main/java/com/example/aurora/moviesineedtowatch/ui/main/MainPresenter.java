package com.example.aurora.moviesineedtowatch.ui.main;

import android.view.View;

import com.example.aurora.moviesineedtowatch.App;
import com.example.aurora.moviesineedtowatch.adaprer.MainRecyclerAdapter;
import com.example.aurora.moviesineedtowatch.dagger.wishlist.RealmImpl;
import com.example.aurora.moviesineedtowatch.dagger.wishlist.WishList;
import com.example.aurora.moviesineedtowatch.tmdb.Movie;

import java.util.List;

import javax.inject.Inject;

import static io.realm.internal.SyncObjectServerFacade.getApplicationContext;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 16/07/18
 * Time: 19:32
 */
public class MainPresenter implements MainScreen.Presenter{

    private MainScreen.View mView;

    @Inject
    RealmImpl realmImpl;

    @Inject
    WishList wishList;

    @Inject
    MainPresenter(MainScreen.View mView) {
        this.mView = mView;
        ((App) getApplicationContext()).getApplicationComponent().inject(this);
    }

    @Override
    public void loadMovies(){
        List<Movie> movies = getWishList();
        realmImpl.addRealmDataChangeListener(movies, recyclerViewListener(movies));
    }

    @Override
    public void searchButton() {
        mView.searchTMDB();
    }

    private List<Movie> getWishList() {
        return wishList.findAll();
    }

    private MainRecyclerAdapter recyclerViewListener(List<Movie> movies) {

        MainRecyclerAdapter mAdapter = mView.initRecyclerView(movies);

        mAdapter.setOnItemClickListener(new MainRecyclerAdapter.ClickListener() {
            @Override
            public void onItemClick(View v, String movieId, String dataLang) {
                mView.movieTMDB(movieId, dataLang);
            }

            @Override
            public void onItemLongClick(View v, String movieId, String dataLang) {
                wishList.deleteSelectedMovie(movieId, dataLang);
            }
        });
        return mAdapter;
    }
}
