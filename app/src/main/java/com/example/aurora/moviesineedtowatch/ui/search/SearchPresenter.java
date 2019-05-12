package com.example.aurora.moviesineedtowatch.ui.search;

import android.util.Log;
import android.view.View;

import com.example.aurora.moviesineedtowatch.adaprers.SearchRecyclerAdapter;
import com.example.aurora.moviesineedtowatch.dagger.wishlist.WishList;
import com.example.aurora.moviesineedtowatch.retrofit.ApiInterface;
import com.example.aurora.moviesineedtowatch.rxjava.RxJava2ApiCallback;
import com.example.aurora.moviesineedtowatch.rxjava.RxJava2FlowableApiCallHelper;
import com.example.aurora.moviesineedtowatch.tools.Constants;
import com.example.aurora.moviesineedtowatch.tmdb.FoundMovie;
import com.example.aurora.moviesineedtowatch.tmdb.Movie;
import com.example.aurora.moviesineedtowatch.tmdb.SearchResult;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.aurora.moviesineedtowatch.tools.Constants.EN;
import static com.example.aurora.moviesineedtowatch.tools.Constants.MESSAGE_ERROR_GETTING_DATA;
import static com.example.aurora.moviesineedtowatch.tools.Constants.MESSAGE_NO_CONNECTION;
import static com.example.aurora.moviesineedtowatch.tools.Constants.RU;
import static com.example.aurora.moviesineedtowatch.tools.Constants.TMDB_KEY;
import static com.example.aurora.moviesineedtowatch.tools.Extensions.*;

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

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

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

        Flowable<SearchResult> flowable =
                apiInterface.getSearchResult((mView.getSwitchValue()?EN:RU), TMDB_KEY, searchQuery);
        Disposable disposable = RxJava2FlowableApiCallHelper.call(flowable, new RxJava2ApiCallback<SearchResult>() {
            @Override
            public void onSuccess(SearchResult jsonArray) {
                mView.setNotificationField(returnTotalResultString(jsonArray.getTotalResults()));
                FoundMovie[] search = jsonArray.getResults();
                mView.initRecyclerView(search);
                mView.setProgressBarInvisible();
            }
            @Override
            public void onFailed(Throwable throwable) {
                if (isOffline())
                    mView.setNotificationField(MESSAGE_NO_CONNECTION);
                else mView.setNotificationField(MESSAGE_ERROR_GETTING_DATA);
                mView.setProgressBarInvisible();
            }
        });
        mCompositeDisposable.add(disposable);
    }

    @Override
    public void clearDisposable() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
        }
    }

    private void loadMovieInfo(String movieId) {
        mView.setProgressBarVisible();

        Observable<Movie> observable =
                apiInterface.getMovie(Integer.parseInt(movieId),(mView.getSwitchValue()?EN:RU), TMDB_KEY);
        Disposable subscribe = observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movie -> {
                    wishList.addSelectedMovie(movie);
                    mView.setProgressBarInvisible();
                    mView.showAddedMovieToast(returnAddMovieString(movie.getTitle()));
                }, throwable -> {
                    if (isOffline())
                        mView.setNotificationField(MESSAGE_NO_CONNECTION);
                    else mView.setNotificationField(MESSAGE_ERROR_GETTING_DATA);
                    mView.setProgressBarInvisible();
                });
    }
}
