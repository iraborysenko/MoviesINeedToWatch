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

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 16/03/18
 * Time: 20:29
 */
public class ApiClient {

    private static final String BASE_URL = "http://api.themoviedb.org/3/";
    private static Retrofit retrofit = null;


    public static Retrofit getClient(Context context) {
        if (retrofit==null) {

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(MovieBuilder.class, new MovieDeserializer(context))
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getClient1() {
        if (retrofit==null) {

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(SearchResultBuilder.class, new SearchResultDeserializer())
                    .registerTypeAdapter(SearchMovieBuilder.class, new SearchMovieDeserializer())
                    .create();


            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

}
