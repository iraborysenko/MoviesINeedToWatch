package com.example.aurora.moviesineedtowatch.dagger.module;

import android.content.Context;

import com.example.aurora.moviesineedtowatch.gson.FoundMovieDeserializer;
import com.example.aurora.moviesineedtowatch.gson.MovieDeserializer;
import com.example.aurora.moviesineedtowatch.gson.SearchResultDeserializer;
import com.example.aurora.moviesineedtowatch.retrofit.ApiInterface;
import com.example.aurora.moviesineedtowatch.tmdb.FoundMovie;
import com.example.aurora.moviesineedtowatch.tmdb.Movie;
import com.example.aurora.moviesineedtowatch.tmdb.SearchResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.aurora.moviesineedtowatch.tools.Constants.TMDB_BASE;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 22/06/18
 * Time: 19:59
 */
@Module(includes = ContextModule.class)
public class NetModule {

    @Provides
    @Singleton
    File provideCacheFile(Context context){
        return new File(context.getCacheDir(), "HttpCache");
    }

    @Provides
    @Singleton
    Cache provideHttpCache(File cacheDirectory) {
        int cacheSize = 10 * 1024 * 1024;
        return new Cache(cacheDirectory, cacheSize);
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(Cache cache) {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.connectTimeout(50, TimeUnit.SECONDS);
        client.readTimeout(55, TimeUnit.SECONDS);
        client.writeTimeout(55, TimeUnit.SECONDS);
        client.cache(cache);
        client.addInterceptor(chain -> {
            try {
                return chain.proceed(chain.request());
            } catch (Exception e) {
                Request offlineRequest = chain.request().newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24)
                        .build();
                return chain.proceed(offlineRequest);
            }
        });
        return client.build();
    }

    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Movie.class, new MovieDeserializer())
                .registerTypeAdapter(SearchResult.class, new SearchResultDeserializer())
                .registerTypeAdapter(FoundMovie.class, new FoundMovieDeserializer())
                .create();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(TMDB_BASE)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    ApiInterface provideApiInterface(Retrofit retrofit) {
        return retrofit.create(ApiInterface.class);
    }
}
