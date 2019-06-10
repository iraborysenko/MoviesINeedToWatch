package com.example.aurora.moviesineedtowatch.dagger.wishlist;

import android.os.Environment;

import com.example.aurora.moviesineedtowatch.adaprers.ToWatchRecyclerAdapter;
import com.example.aurora.moviesineedtowatch.adaprers.WatchedRecyclerAdapter;
import com.example.aurora.moviesineedtowatch.tmdb.Movie;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.internal.IOException;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 18/06/18
 * Time: 09:36
 */
public class RealmImpl {

    private Realm mRealm;

    @Inject
    public RealmImpl() {
        this.mRealm = Realm.getDefaultInstance();
    }

    <T extends RealmObject> void add(T model) {
        mRealm.beginTransaction();
        mRealm.copyToRealm(model);
        mRealm.commitTransaction();
    }

    <T extends RealmObject> List<T> findAllToWatch() {
        return mRealm.where((Class<T>) Movie.class)
                .equalTo("isWatched", false)
                .findAll();
    }

    <T extends RealmObject> List<T> findAllWatched() {
        return mRealm.where((Class<T>) Movie.class)
                .equalTo("isWatched", true)
                .findAll();
    }

    public <T extends RealmObject> void addRealmDataChangeListener(List<T> movies,
                                                                   ToWatchRecyclerAdapter mAdapter) {
        RealmResults<T> list = (RealmResults<T>) movies;
        list.addChangeListener((results, changeSet) -> mAdapter.notifyDataSetChanged());
    }

    public <T extends RealmObject> void addRealmDataChangeListener(List<T> movies,
                                                                   WatchedRecyclerAdapter mAdapter) {
        RealmResults<T> list = (RealmResults<T>) movies;
        list.addChangeListener((results, changeSet) -> mAdapter.notifyDataSetChanged());
    }

    public void close() {
        mRealm.close();
    }

    void delete(String movieId) {
        mRealm.beginTransaction();
        RealmResults<Movie> result = mRealm.where(Movie.class)
                .equalTo("id", movieId)
                .findAll();
        result.deleteAllFromRealm();
        mRealm.commitTransaction();
    }

    Movie choose(String movieId) {
        Movie curMovie = mRealm.where(Movie.class)
                .equalTo("id", movieId)
                .findFirst();
        assert curMovie != null;
        return curMovie;
    }

    void move(String movieId, Boolean watchedFlag) {
        mRealm.beginTransaction();
        Movie movie = mRealm.where(Movie.class).equalTo("id", movieId).findFirst();
        if (movie != null) {
            movie.setWatched(watchedFlag);
        }
        mRealm.commitTransaction();
    }

    void moveWithData(String movieId, String commentStr, String myRatingStr) {
        mRealm.beginTransaction();
        Movie movie = mRealm.where(Movie.class).equalTo("id", movieId).findFirst();
        if (movie != null) {
            movie.setWatched(true);
            movie.setComment(commentStr);
            movie.setMyRating(myRatingStr);
        }
        mRealm.commitTransaction();
    }

    void exportDataBase() {
        try {
            final File file = new File(Environment.getExternalStorageDirectory().getPath().concat("/MoviesINeedToWatch.realm"));
            if (file.exists()) {
                file.delete();
            }
            mRealm.writeCopyTo(file);
        } catch (IOException e) {
            mRealm.close();
            e.printStackTrace();
        }
    }
}
