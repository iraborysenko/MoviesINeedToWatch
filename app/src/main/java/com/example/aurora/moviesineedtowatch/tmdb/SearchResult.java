package com.example.aurora.moviesineedtowatch.tmdb;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 15/03/18
 * Time: 20:02
 */
public class SearchResult {
    private String totalResults;
    private FoundMovie[] results;

    public SearchResult(String totalResults, FoundMovie[] results) {
        this.totalResults = totalResults;
        this.results = results;
    }

    public String getTotalResults() {
        return totalResults;
    }

    public FoundMovie[] getResults() {
        return results;
    }
}
