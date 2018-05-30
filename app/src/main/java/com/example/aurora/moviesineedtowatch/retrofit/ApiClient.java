package com.example.aurora.moviesineedtowatch.retrofit;

import static com.example.aurora.moviesineedtowatch.tmdb.Const.TMDB_BASE;

import android.content.Context;

import com.example.aurora.moviesineedtowatch.gson.MovieDeserializer;
import com.example.aurora.moviesineedtowatch.gson.FoundMovieDeserializer;
import com.example.aurora.moviesineedtowatch.gson.SearchResultDeserializer;
import com.example.aurora.moviesineedtowatch.tmdb.Movie;
import com.example.aurora.moviesineedtowatch.tmdb.FoundMovie;
import com.example.aurora.moviesineedtowatch.tmdb.SearchResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
                    .registerTypeAdapter(Movie.class, new MovieDeserializer(context))
                    .registerTypeAdapter(SearchResult.class, new SearchResultDeserializer())
                    .registerTypeAdapter(FoundMovie.class, new FoundMovieDeserializer())
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(TMDB_BASE)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

}
