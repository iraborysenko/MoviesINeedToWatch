package com.example.aurora.moviesineedtowatch.tmdb;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 23/01/18
 * Time:
 */

public class MovieBuilder {
    private String id;
    private String imdbID;
    private String imdb;
    private String title;
    private String originalTitle;
    private String originalLanguage;
    private String overview;
    private String posterPath;
    private Bitmap posterBitmap;
    private String releaseDate;
    private String tagline;
    private String runtime;
    private String voteAverage;
    private int voteCount;
    private ArrayList<Integer> genresIds;
    private ArrayList<String> comps;
    private ArrayList<String> countrs;
    private String savedLang;

    public MovieBuilder(String id, String imdbID, String imdb, String title, String originalTitle,
                        String originalLanguage, String overview, String posterPath,
                        Bitmap posterBitmap, String releaseDate, String tagline,
                        String runtime, String voteAverage, int voteCount,
                        ArrayList<Integer> genresIds, ArrayList<String> comps,
                        ArrayList<String> countrs, String savedLang) {
        this.id = id;
        this.imdbID = imdbID;
        this.imdb = imdb;
        this.title = title;
        this.originalTitle = originalTitle;
        this.originalLanguage = originalLanguage;
        this.overview = overview;
        this.posterPath = posterPath;
        this.posterBitmap = posterBitmap;
        this.releaseDate = releaseDate;
        this.tagline = tagline;
        this.runtime = runtime;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
        this.genresIds = genresIds;
        this.comps = comps;
        this.countrs = countrs;
        this.savedLang = savedLang;
    }

    //getters
    public String getId() {
        return id;
    }

    String getImdbID() {
        return imdbID;
    }

    public String getImdb() {
        return imdb;
    }

    public String getTitle() {
        return title;
    }

    String getOriginalTitle() {
        return originalTitle;
    }

    String getOriginalLanguage() {
        return originalLanguage;
    }

    String getOverview() {
        return overview;
    }

    String getPosterPath() {
        return posterPath;
    }

    Bitmap getPosterBitmap() {
        return posterBitmap;
    }

    String getReleaseDate() {
        return releaseDate;
    }

    String getTagline() {
        return tagline;
    }

    String getRuntime() {
        return runtime;
    }

    String getVoteAverage() {
        return voteAverage;
    }

    int getVoteCount() {
        return voteCount;
    }

    ArrayList<Integer> getGenresIds() {
        return genresIds;
    }

    ArrayList<String> getComps() {
        return comps;
    }

    ArrayList<String> getCountrs() {
        return countrs;
    }

    String getSavedLang() {
        return savedLang;
    }

}