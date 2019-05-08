package com.example.aurora.moviesineedtowatch.dagger.module;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 08/05/19
 * Time: 17:20
 */
@Module
public class ContextModule {

    private Context context;

    public ContextModule(Context context){
        this.context = context;
    }

    @Provides
    public Context context(){ return context.getApplicationContext(); }
}
