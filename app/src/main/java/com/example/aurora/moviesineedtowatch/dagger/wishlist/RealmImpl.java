package com.example.aurora.moviesineedtowatch.dagger.wishlist;

import android.app.Activity;

import com.example.aurora.moviesineedtowatch.adaprers.ToWatchRecyclerAdapter;
import com.example.aurora.moviesineedtowatch.adaprers.WatchedRecyclerAdapter;
import com.example.aurora.moviesineedtowatch.tmdb.Movie;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.internal.IOException;

import static com.example.aurora.moviesineedtowatch.tools.Constants.EXPORT_REALM_FILE_NAME;
import static com.example.aurora.moviesineedtowatch.tools.Constants.EXPORT_REALM_PATH;
import static com.example.aurora.moviesineedtowatch.tools.Constants.IMPORT_REALM_FILE_NAME;

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
        File exportRealmFile;
        try {
            EXPORT_REALM_PATH.mkdirs();
            exportRealmFile = new File(EXPORT_REALM_PATH, EXPORT_REALM_FILE_NAME);
            exportRealmFile.delete();
            mRealm.writeCopyTo(exportRealmFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
        close();
    }

    void importDataBase(Activity activity) {
        String restoreFilePath = EXPORT_REALM_PATH + "/" + EXPORT_REALM_FILE_NAME;
        copyBundledRealmFile(activity, restoreFilePath, IMPORT_REALM_FILE_NAME);
    }

    private void copyBundledRealmFile(Activity activity, String oldFilePath, String outFileName) {
        try {
            File file = new File(activity.getApplicationContext().getFilesDir(), outFileName);

            FileOutputStream outputStream = new FileOutputStream(file);

            FileInputStream inputStream = new FileInputStream(new File(oldFilePath));

            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, bytesRead);
            }
            outputStream.close();
            file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
}
