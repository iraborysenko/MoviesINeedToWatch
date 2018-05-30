package com.example.aurora.moviesineedtowatch.tmdb;

import io.realm.annotations.Required;
import io.realm.RealmObject;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 23/01/18
 * Time:
 */
public class Movie extends RealmObject {

    @Required
    private String id;
    @Required
    private String savedLang;
    private String imdbID;
    private String imdb;
    private String title;
    private String originalTitle;
    private String originalLanguage;
    private String overview;
    private String posterPath;
    private String posterBitmap;
    private String releaseDate;
    private String tagline;
    private String runtime;
    private String voteAverage;
    private String voteCount;
    private String genresIds;
    private String compsArr;
    private String countrsArr;

    private float myRating;
    private Boolean isWatched;
    private String comment;

    public Movie(String id, String imdbID, String imdb, String title, String originalTitle,
                        String originalLanguage, String overview, String posterPath,
                        String posterBitmap, String releaseDate, String tagline,
                        String runtime, String voteAverage, String voteCount,
                        String genresIds, String compsArr,
                        String countrsArr, String savedLang) {
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
        this.compsArr = compsArr;
        this.countrsArr = countrsArr;
        this.savedLang = savedLang;
    }

    public Movie () {}

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

    public String getPosterBitmap() {
        return posterBitmap;
    }

    public void setPosterBitmap(String posterBitmap) {
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

    public String getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(String voteCount) {
        this.voteCount = voteCount;
    }

    public String getGenresIds() {
        return genresIds;
    }

    public void setGenresIds(String genresIds) {
        this.genresIds = genresIds;
    }

    public String getCompsArr() {
        return compsArr;
    }

    public void setCompsArr(String compsArr) {
        this.compsArr = compsArr;
    }

    public String getCountrsArr() {
        return countrsArr;
    }

    public void setCountrsArr(String countrsArr) {
        this.countrsArr = countrsArr;
    }

    public String getSavedLang() {
        return savedLang;
    }

    public void setSavedLang(String savedLang) {
        this.savedLang = savedLang;
    }

    public float getMyRating() {
        return myRating;
    }

    public void setMyRating(float myRating) {
        this.myRating = myRating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Boolean getWatched() {
        return isWatched;
    }

    public void setWatched(Boolean watched) {
        isWatched = watched;
    }
}