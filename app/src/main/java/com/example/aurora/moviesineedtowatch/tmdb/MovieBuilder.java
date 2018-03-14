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
    String getId() {
        return id;
    }

    String getImdbID() {
        return imdbID;
    }

    String getImdb() {
        return imdb;
    }

    String getTitle() {
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

    //setters
    public void setId(String id) {
        this.id = id;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }

    public void setImdb(String imdb) {
        this.imdb = imdb;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void setPosterBitmap(Bitmap posterBitmap) {
        this.posterBitmap = posterBitmap;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public void setGenresIds(ArrayList<Integer> genresIds) {
        this.genresIds = genresIds;
    }

    public void setComps(ArrayList<String> comps) {
        this.comps = comps;
    }

    public void setCountrs(ArrayList<String> countrs) {
        this.countrs = countrs;
    }

    public void setSavedLang(String savedLang) {
        this.savedLang = savedLang;
    }

}