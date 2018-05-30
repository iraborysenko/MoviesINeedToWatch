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

    //http://api.themoviedb.org/3/search/movie?language=ru-RU&api_key=ffd306e2816e78d8da9d87e05072bf55&query=%D1%80%D0%B0%D0%B1%D0%BE%D1%82%D0%B0+
    //http://api.themoviedb.org/3/movie/14577?language=ru-RU&api_key=ffd306e2816e78d8da9d87e05072bf55

    @GET("search/movie")
    Call<SearchResult> getSearchResult(@Query("language") String lang,
                                              @Query("api_key") String apiKey,
                                              @Query("query") String query);

    @GET("movie/{id}")
    Call<Movie> getMovie(@Path("id") int id,
                                @Query("language") String lang,
                                @Query("api_key") String apiKey);

}
