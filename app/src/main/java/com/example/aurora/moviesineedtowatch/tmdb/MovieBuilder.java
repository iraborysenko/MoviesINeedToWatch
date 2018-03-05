package com.example.aurora.moviesineedtowatch.tmdb;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 23/01/18
 * Time:
 */

public class MovieBuilder {
    private final int id;
    @SerializedName("imdb_id")
    private final String imdbID;
    private final String imdb;
    private final String title;
    @SerializedName("original_title")
    private final String originalTitle;
    @SerializedName("original_language")
    private final String originalLanguage;
    private final String overview;
    @SerializedName("poster_path")
    private final String posterPath;
    private Bitmap posterBitmap;
    @SerializedName("release_date")
    private final String releaseDate;
    private final String tagline;
    private final String runtime;
    @SerializedName("vote_average")
    private final String voteAverage;
    @SerializedName("vote_count")
    private final int voteCount;
    @SerializedName("genres")
    private final List<GengresIDs> genresIds;
    @SerializedName("production_companies")
    private final List<Companies> comps;
    @SerializedName("production_countries")
    private final List<Countries> countrs;
    private final String savedLang;


    private MovieBuilder(Builder builder) {
        id = builder.id;
        imdbID = builder.imdbID;
        imdb = builder.imdb;
        title = builder.title;
        originalTitle = builder.originalTitle;
        originalLanguage = builder.originalLanguage;
        overview = builder.overview;
        posterPath = builder.posterPath;
        posterBitmap = builder.posterBitmap;
        releaseDate = builder.releaseDate;
        tagline = builder.tagline;
        runtime = builder.runtime;
        voteAverage = builder.voteAverage;
        voteCount = builder.voteCount;
        genresIds = builder.genresIds;
        comps = builder.comps;
        countrs = builder.countrs;
        savedLang = builder.savedLang;
    }

    public static Builder newBuilder(int id, String title) {
        return new Builder(id, title);
    }

    public int getId() {
        return id;
    }

    public String getImdbID() { return imdbID; }

    public String getImdb() { return imdb; }

    public String getTitle() {
        return title;
    }

    public String getOriginalTitle() { return originalTitle; }

    public String getOriginalLanguage() { return originalLanguage; }

    String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public Bitmap getPosterBitmap() { return posterBitmap; }

    String getReleaseDate() {
        return releaseDate;
    }

    public String getTagline() {
        return tagline;
    }

    String getRuntime() {
        return runtime;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    int getVoteCount() {
        return voteCount;
    }

    public List<GengresIDs> getGenresIds() { return genresIds; }

    public List<Companies> getComps() { return comps; }

    public List<Countries> getCountrs() { return countrs; }

    String getSavedLang() { return savedLang; }

    public void returnPosterBitmap(Bitmap posterBitmap){
        this.posterBitmap = posterBitmap;
    }


    public static class Builder {
        private int id;
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
        private List<GengresIDs> genresIds;
        private List<Companies> comps;
        private List<Countries> countrs;
        private String savedLang;

        Builder(int id, String title) {
            this.id = id;
            this.title = title;
        }

        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        public Builder setImdbID(String imdbID) {
            this.imdbID = imdbID;
            return this;
        }

        public Builder setImdb(String imdb) {
            this.imdb = imdb;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setOriginalTitle(String originalTitle) {
            this.originalTitle = originalTitle;
            return this;
        }

        public Builder setOriginalLanguage(String originalLanguage) {
            this.originalLanguage = originalLanguage;
            return this;
        }

        public Builder setOverview(String overview) {
            this.overview = overview;
            return this;
        }

        public Builder setPosterPath(String posterPath) {
            this.posterPath = posterPath;
            return this;
        }

        public Builder setPosterBitmap(Bitmap posterBitmap) {
            this.posterBitmap = posterBitmap;
            return this;
        }



        public Builder setReleaseDate(String releaseDate) {
            this.releaseDate = releaseDate;
            return this;
        }

        public Builder setTagline(String tagline) {
            this.tagline = tagline;
            return this;
        }

        public Builder setRuntime(String runtime) {
            this.runtime = runtime;
            return this;
        }

        public Builder setVoteAverage(String voteAverage) {
            this.voteAverage = voteAverage;
            return this;
        }

        public Builder setVoteCount(int voteCount) {
            this.voteCount = voteCount;
            return this;
        }

        public Builder setGenresIds(List<GengresIDs> genresIds) {
            this.genresIds = genresIds;
            return this;
        }

        public Builder setComps(List<Companies> comps) {
            this.comps = comps;
            return this;
        }

        public Builder setCountrs(List<Countries> countrs) {
            this.countrs = countrs;
            return this;
        }

        public Builder setSavedLang(String savedLang) {
            this.savedLang = savedLang;
            return this;
        }

        public MovieBuilder build() {
            return new MovieBuilder(this);
        }

    }
//
//    @Override
//    public String toString() {
//        return "[id=" + id + ", imdb_id=" + imdbID + ", title=" + title
//                + ", original_title=" + originalTitle + ", original_language=" + originalLanguage
//                + ", overview=" + overview
//                + ", poster_path=" + posterPath + ", release_date=" + releaseDate
//                + ", tagline=" + tagline + ", runtime=" + runtime + ", vote_average=" + voteAverage
//                + ", vote_count=" + voteCount
//                + ", genres=" + genresIds
//                + "]";
//    }

}