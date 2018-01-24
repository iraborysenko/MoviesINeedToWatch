package com.example.aurora.moviesineedtowatch1;

/**
 * Created by aurora on 23/01/18.
 */

public class MovieResult {
    private final int id;
    private final String imdbID;
    private final String title;
    private final String originalTitle;
    private final String originalLanguage;
    private final String overview;
    private final String posterPath;
    private final String releaseDate;
    private final String tagline;
    private final int runtime;
    private final float voteAverage;
    private final int voteCount;


    public MovieResult(Builder builder) {
        id = builder.id;
        imdbID = builder.imdbID;
        title = builder.title;
        originalTitle = builder.originalTitle;
        originalLanguage = builder.originalLanguage;
        overview = builder.overview;
        posterPath = builder.posterPath;
        releaseDate = builder.releaseDate;
        tagline = builder.tagline;
        runtime = builder.runtime;
        voteAverage = builder.voteAverage;
        voteCount = builder.voteCount;
    }


    public static class Builder {
        private int id;
        private String imdbID;
        private String title;
        private String originalTitle;
        private String originalLanguage;
        private String overview;
        private String posterPath;
        private String releaseDate;
        private String tagline;
        private int runtime;
        private float voteAverage;
        private int voteCount;

        public Builder(int id, String title) {
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

        public Builder setReleaseDate(String releaseDate) {
            this.releaseDate = releaseDate;
            return this;
        }

        public Builder setTagline(String tagline) {
            this.tagline = tagline;
            return this;
        }

        public Builder setRuntime(int runtime) {
            this.runtime = runtime;
            return this;
        }

        public Builder setVoteAverage(float voteAverage) {
            this.voteAverage = voteAverage;
            return this;
        }

        public Builder setVoteCount(int voteCount) {
            this.voteCount = voteCount;
            return this;
        }


        public MovieResult build() {
            return new MovieResult(this);
        }

    }

    public static Builder newBuilder(int id, String title) {
        return new Builder(id, title);
    }


    public int getId() {
        return id;
    }

    public String getImdbID() {
        return imdbID;
    }

    public String getTitle() {
        return title;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getTagline() {
        return tagline;
    }

    public int getRuntime() {
        return runtime;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public int getVoteCount() {
        return voteCount;
    }

//    @Override
//    public String toString() {
//        return getTitle();
//    }

}