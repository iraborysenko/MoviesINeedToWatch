package com.example.aurora.moviesineedtowatch.retrofit;

import com.example.aurora.moviesineedtowatch.tmdb.MovieBuilder;
import com.example.aurora.moviesineedtowatch.tmdb.SearchResultBuilder;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 16/03/18
 * Time: 20:28
 */
public interface ApiInterface {

//    @GET("movie/top_rated")
//    Call<SearchResultBuilder> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("movie/{id}")
    Call<MovieBuilder> getMovie(@Path("id") int id, @Query("api_key") String apiKey);

}
