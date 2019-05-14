package com.example.aurora.moviesineedtowatch.ui.search;

import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;

import com.example.aurora.moviesineedtowatch.adaprers.SearchRecyclerAdapter;
import com.example.aurora.moviesineedtowatch.dagger.wishlist.WishList;
import com.example.aurora.moviesineedtowatch.retrofit.ApiInterface;
import com.example.aurora.moviesineedtowatch.tools.Constants;
import com.example.aurora.moviesineedtowatch.tmdb.FoundMovie;
import com.example.aurora.moviesineedtowatch.tmdb.Movie;
import com.example.aurora.moviesineedtowatch.tmdb.SearchResult;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

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
    public void getResultsBasedOnQuery(SearchView searchView) {
        DisposableObserver<SearchResult> searchResultDisposableObserver =
                getObservableQuery(searchView)
                .filter((Predicate<? super String>) s -> !s.equals(""))
                .debounce(2, TimeUnit.SECONDS)
                .distinctUntilChanged()
                .switchMap((Function<String, ObservableSource<SearchResult>>) s ->
                        apiInterface.getSearchResult((mView.getSwitchValue() ? EN : RU), TMDB_KEY, s))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getObserver());
    }

    private Observable<String> getObservableQuery(SearchView searchView){

        final PublishSubject<String> publishSubject = PublishSubject.create();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mView.setProgressBarVisible();
                publishSubject.onNext(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mView.setProgressBarVisible();
                publishSubject.onNext(newText);
                return true;
            }
        });

        return publishSubject;
    }

    private DisposableObserver<SearchResult> getObserver(){
        return new DisposableObserver<SearchResult>() {

            @Override
            public void onNext(SearchResult searchResult) {
                mView.setNotificationField(returnTotalResultString(searchResult.getTotalResults()));
                FoundMovie[] search = searchResult.getResults();
                mView.initRecyclerView(search);
                mView.setProgressBarInvisible();
            }

            @Override
            public void onError(@NonNull Throwable e) {
                e.printStackTrace();
                if (isOffline())
                    mView.setNotificationField(MESSAGE_NO_CONNECTION);
                else mView.setNotificationField(MESSAGE_ERROR_GETTING_DATA);
                mView.setProgressBarInvisible();
            }

            @Override
            public void onComplete() {
                mView.setProgressBarInvisible();
            }
        };
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
