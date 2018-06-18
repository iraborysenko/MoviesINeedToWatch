package com.example.aurora.moviesineedtowatch.dagger;

import android.content.Context;

import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;

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
            realmConfiguration = new RealmConfiguration.Builder().build();
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

    public <T extends RealmObject> List<T> findAll(Class<T> clazz) {
        return getRealmInstance().where(clazz).findAll();
    }

    public void close() {
        getRealmInstance().close();
    }
}
