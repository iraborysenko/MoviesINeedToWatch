package com.example.aurora.moviesineedtowatch.tmdb;

import java.util.ArrayList;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 23/01/18
 * Time:
 */
public class SearchMovieBuilder {
    private String id;
    private String title;
    private String originalTitle;
    private String posterPath;
    private String releaseDate;
    private String voteAverage;
    private ArrayList<Integer> genreIds;

    public SearchMovieBuilder(String id, String title, String originalTitle, String posterPath,
                              String releaseDate, String voteAverage, ArrayList<Integer> genreIds) {
        this.id = id;
        this.title = title;
        this.originalTitle = originalTitle;
        this.posterPath = posterPath;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.genreIds = genreIds;

    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public ArrayList<Integer> getGenreIds() {
        return genreIds;
    }

}