package com.example.aurora.moviesineedtowatch.tmdb;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 15/03/18
 * Time: 20:02
 */
public class SearchResultBuilder {
    private String totalResults;
    private SearchMovieBuilder[] results;

    public SearchResultBuilder(String totalResults, SearchMovieBuilder[] results) {
        this.totalResults = totalResults;
        this.results = results;
    }

    public String getTotalResults() {
        return totalResults;
    }

    public SearchMovieBuilder[] getResults() {
        return results;
    }
}
