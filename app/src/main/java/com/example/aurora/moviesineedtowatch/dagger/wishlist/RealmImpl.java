package com.example.aurora.moviesineedtowatch.dagger.wishlist;

import com.example.aurora.moviesineedtowatch.adaprer.MainRecyclerAdapter;
import com.example.aurora.moviesineedtowatch.tmdb.Movie;

import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

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

    public <T extends RealmObject> T add(T model) {
        mRealm.beginTransaction();
        mRealm.copyToRealm(model);
        mRealm.commitTransaction();
        return model;
    }

    public <T extends RealmObject> List<T> findAll(Class<T> tClass) {
        return mRealm.where(tClass).findAll();
    }

    public <T extends RealmObject> void addRealmDataChangeListener(List<T> movies,
                                                                   MainRecyclerAdapter mAdapter) {
        RealmResults<T> list = (RealmResults<T>) movies;
        list.addChangeListener((results, changeSet) -> mAdapter.notifyDataSetChanged());
    }

    public void close() {
        mRealm.close();
    }

    public void delete(String movieId, String dataLang) {
        mRealm.beginTransaction();
        RealmResults<Movie> result = mRealm.where(Movie.class)
                .equalTo("id", movieId)
                .equalTo("savedLang", dataLang)
                .findAll();
        result.deleteAllFromRealm();
        mRealm.commitTransaction();
    }

    public Movie choose(String movieId, String dataLang) {
        Movie curMovie = mRealm.where(Movie.class)
                .equalTo("id", movieId)
                .equalTo("savedLang", dataLang)
                .findFirst();
        assert curMovie != null;
        return curMovie;
    }
}
