package com.example.aurora.moviesineedtowatch.retrofit;

import com.example.aurora.moviesineedtowatch.tmdb.Movie;
import com.example.aurora.moviesineedtowatch.tmdb.SearchResult;

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
    @GET("search/movie")
    Call<SearchResult> getSearchResult(@Query("language") String lang,
                                              @Query("api_key") String apiKey,
                                              @Query("query") String query);

    @GET("movie/{id}")
    Call<Movie> getMovie(@Path("id") int id,
                                @Query("language") String lang,
                                @Query("api_key") String apiKey);
}
