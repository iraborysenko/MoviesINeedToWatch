package com.example.aurora.moviesineedtowatch.retrofit;

import android.content.Context;

import com.example.aurora.moviesineedtowatch.gson.MovieDeserializer;
import com.example.aurora.moviesineedtowatch.gson.SearchMovieDeserializer;
import com.example.aurora.moviesineedtowatch.gson.SearchResultDeserializer;
import com.example.aurora.moviesineedtowatch.tmdb.MovieBuilder;
import com.example.aurora.moviesineedtowatch.tmdb.SearchMovieBuilder;
import com.example.aurora.moviesineedtowatch.tmdb.SearchResultBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.aurora.moviesineedtowatch.tmdb.Const.TMDB_BASE;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 16/03/18
 * Time: 20:29
 */
public class ApiClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient(Context context) {
        if (retrofit==null) {

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(MovieBuilder.class, new MovieDeserializer(context))
                    .registerTypeAdapter(SearchResultBuilder.class, new SearchResultDeserializer())
                    .registerTypeAdapter(SearchMovieBuilder.class, new SearchMovieDeserializer())
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(TMDB_BASE)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

}
