package com.example.aurora.moviesineedtowatch.tmdb;

import java.util.ArrayList;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 23/01/18
 * Time:
 */

public class SearchBuilder {
    private final int id;
    private final String title;
    private final String originalTitle;
    private final String posterPath;
    private final String releaseDate;
    private final float voteAverage;
    private final ArrayList<Integer> genreIds;

    private SearchBuilder(Builder builder) {
        id = builder.id;
        title = builder.title;
        originalTitle = builder.originalTitle;
        posterPath = builder.posterPath;
        releaseDate = builder.releaseDate;
        voteAverage = builder.voteAverage;
        genreIds = builder.genreIds;
    }

    public static Builder newBuilder(int id, String title) {
        return new Builder(id, title);
    }

    public int getId() {
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

    public float getVoteAverage() {
        return voteAverage;
    }

    public ArrayList<Integer> getGenreIds() { return genreIds; }

    public static class Builder {
        private int id;
        private String title;
        private String originalTitle;
        private String posterPath;
        private String releaseDate;
        private float voteAverage;
        private ArrayList<Integer> genreIds;

        Builder(int id, String title) {
            this.id = id;
            this.title = title;
        }

        public Builder setId(int id) {
            this.id = id;
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

        public Builder setPosterPath(String posterPath) {
            this.posterPath = posterPath;
            return this;
        }

        public Builder setReleaseDate(String releaseDate) {
            this.releaseDate = releaseDate;
            return this;
        }

        public Builder setVoteAverage(float voteAverage) {
            this.voteAverage = voteAverage;
            return this;
        }

        public Builder setGenreIds(ArrayList<Integer> genreIds) {
            this.genreIds = genreIds;
            return this;
        }

        public SearchBuilder build() {
            return new SearchBuilder(this);
        }

    }

}