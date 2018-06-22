package com.example.aurora.moviesineedtowatch.dagger.wishlist;

import android.content.Context;

import com.example.aurora.moviesineedtowatch.adaprer.MainRecyclerAdapter;
import com.example.aurora.moviesineedtowatch.dagger.Injector;
import com.example.aurora.moviesineedtowatch.tmdb.Movie;

import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 18/06/18
 * Time: 09:36
 */
public class DatabaseRealm {

    @Inject
    Context mContext;

    private RealmConfiguration realmConfiguration;

    public DatabaseRealm() {
        Injector.getApplicationComponent().inject(this);
    }

    public void setup() {
        Realm.init(mContext);
        if (realmConfiguration == null) {
            realmConfiguration = new RealmConfiguration.Builder()
                    .deleteRealmIfMigrationNeeded()
                    .build();
            Realm.setDefaultConfiguration(realmConfiguration);
        } else {
            throw new IllegalStateException("database already configured");
        }
    }

    private Realm getRealmInstance() {
        return Realm.getDefaultInstance();
    }

    public <T extends RealmObject> T add(T model) {
        Realm realm = getRealmInstance();
        realm.beginTransaction();
        realm.copyToRealm(model);
        realm.commitTransaction();
        return model;
    }

    public <T extends RealmObject> List<T> findAll(Class<T> tClass) {
        return getRealmInstance().where(tClass).findAll();
    }

    public <T extends RealmObject> void addRealmDataChangeListener(List<T> movies,
                                                                   MainRecyclerAdapter mAdapter) {
        RealmResults<T> list = (RealmResults<T>) movies;
        list.addChangeListener((results, changeSet) -> mAdapter.notifyDataSetChanged());
    }

    public void close() {
        getRealmInstance().close();
    }

    public void delete(String movieId, String dataLang) {
        Realm realm = getRealmInstance();
        realm.beginTransaction();
        RealmResults<Movie> result = realm.where(Movie.class)
                .equalTo("id", movieId)
                .equalTo("savedLang", dataLang)
                .findAll();
        result.deleteAllFromRealm();
        realm.commitTransaction();
    }

    public Movie choose(String movieId, String dataLang) {
        Realm realm = getRealmInstance();
        Movie curMovie = realm.where(Movie.class)
                .equalTo("id", movieId)
                .equalTo("savedLang", dataLang)
                .findFirst();
        assert curMovie != null;
        return curMovie;
    }
}
