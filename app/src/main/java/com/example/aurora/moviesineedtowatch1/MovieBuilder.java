package com.example.aurora.moviesineedtowatch1;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 23/01/18
 * Time:
 */

class MovieBuilder {
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


    MovieBuilder(Builder builder) {
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

    static Builder newBuilder(int id, String title) {
        return new Builder(id, title);
    }

    int getId() {
        return id;
    }

    String getImdbID() {
        return imdbID;
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

    String getReleaseDate() {
        return releaseDate;
    }

    String getTagline() {
        return tagline;
    }

    int getRuntime() {
        return runtime;
    }

    float getVoteAverage() {
        return voteAverage;
    }

    int getVoteCount() {
        return voteCount;
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

        Builder(int id, String title) {
            this.id = id;
            this.title = title;
        }

        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        Builder setImdbID(String imdbID) {
            this.imdbID = imdbID;
            return this;
        }

        Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        Builder setOriginalTitle(String originalTitle) {
            this.originalTitle = originalTitle;
            return this;
        }

        Builder setOriginalLanguage(String originalLanguage) {
            this.originalLanguage = originalLanguage;
            return this;
        }

        Builder setOverview(String overview) {
            this.overview = overview;
            return this;
        }

        Builder setPosterPath(String posterPath) {
            this.posterPath = posterPath;
            return this;
        }

        Builder setReleaseDate(String releaseDate) {
            this.releaseDate = releaseDate;
            return this;
        }

        Builder setTagline(String tagline) {
            this.tagline = tagline;
            return this;
        }

        Builder setRuntime(int runtime) {
            this.runtime = runtime;
            return this;
        }

        Builder setVoteAverage(float voteAverage) {
            this.voteAverage = voteAverage;
            return this;
        }

        Builder setVoteCount(int voteCount) {
            this.voteCount = voteCount;
            return this;
        }

        MovieBuilder build() {
            return new MovieBuilder(this);
        }

    }

//    @Override
//    public String toString() {
//        return getTitle();
//    }

}