package com.example.aurora.moviesineedtowatch.dagger;

import android.content.SharedPreferences;

import javax.inject.Inject;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 28/06/18
 * Time: 19:51
 */
public class SharedPreferencesSettings {

    private SharedPreferences mSharedPreferences;

    @Inject
    public SharedPreferencesSettings(SharedPreferences mSharedPreferences) {
        this.mSharedPreferences = mSharedPreferences;
    }

    public void putData(String key, Boolean data) {
        mSharedPreferences.edit().putBoolean(key,data).apply();
    }

    public Boolean getData(String key) {
        return mSharedPreferences.getBoolean(key,false);
    }
}
