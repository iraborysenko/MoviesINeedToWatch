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

    public Boolean contains(String set) {
        return getPrefs().contains(set);
    }

    public void putBooleanData(String key, Boolean data) {
        mSharedPreferences.edit().putBoolean(key,data).apply();
    }

    public Boolean getBooleanData(String key) {
        return mSharedPreferences.getBoolean(key,false);
    }

    public void putStringData(String key, String data) {
        mSharedPreferences.edit().putString(key,data).apply();
    }

    public String getStringData(String key, String defaultValue) {
        return mSharedPreferences.getString(key, defaultValue);
    }

    private SharedPreferences getPrefs() {
        return mSharedPreferences;
    }

    public void registerPrefsListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        getPrefs().registerOnSharedPreferenceChangeListener(listener);
    }

    public void unregisterPrefsListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        getPrefs().unregisterOnSharedPreferenceChangeListener(listener);
    }
}
