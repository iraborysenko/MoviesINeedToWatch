package com.example.aurora.moviesineedtowatch.tmdb;

import android.graphics.Bitmap;

import java.util.ArrayList;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.Required;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 23/01/18
 * Time:
 */

public class MovieBuilder extends RealmObject {
    @Required
    private String id;
    private String imdbID;
    private String imdb;
    private String title;
    private String originalTitle;
    private String originalLanguage;
    private String overview;
    private String posterPath;
    @Ignore
    private Bitmap posterBitmap;
    private String releaseDate;
    private String tagline;
    private String runtime;
    private String voteAverage;
    private int voteCount;
    @Ignore
    private ArrayList<Integer> genresIds;
    @Ignore
    private ArrayList<String> comps;
    @Ignore
    private ArrayList<String> countrs;
    @Ignore
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

    public MovieBuilder () {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImdbID() {
        return imdbID;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }

    public String getImdb() {
        return imdb;
    }

    public void setImdb(String imdb) {
        this.imdb = imdb;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public Bitmap getPosterBitmap() {
        return posterBitmap;
    }

    public void setPosterBitmap(Bitmap posterBitmap) {
        this.posterBitmap = posterBitmap;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public ArrayList<Integer> getGenresIds() {
        return genresIds;
    }

    public void setGenresIds(ArrayList<Integer> genresIds) {
        this.genresIds = genresIds;
    }

    public ArrayList<String> getComps() {
        return comps;
    }

    public void setComps(ArrayList<String> comps) {
        this.comps = comps;
    }

    public ArrayList<String> getCountrs() {
        return countrs;
    }

    public void setCountrs(ArrayList<String> countrs) {
        this.countrs = countrs;
    }

    public String getSavedLang() {
        return savedLang;
    }

    public void setSavedLang(String savedLang) {
        this.savedLang = savedLang;
    }
}