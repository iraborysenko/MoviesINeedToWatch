package com.example.aurora.moviesineedtowatch.dagger.module;

import android.content.Context;
import android.content.SharedPreferences;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.SHARED_REFERENCES;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 28/06/18
 * Time: 19:54
 */
@Module
public class SharedPreferencesModule {

    private Context context;

    public SharedPreferencesModule(Context context) {
        this.context = context;
    }

    @Provides
    SharedPreferences provideSharedPreferences() {
        return context.getSharedPreferences(SHARED_REFERENCES, Context.MODE_PRIVATE);
    }
}