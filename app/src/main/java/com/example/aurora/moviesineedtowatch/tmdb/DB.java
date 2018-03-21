package com.example.aurora.moviesineedtowatch.tmdb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static com.example.aurora.moviesineedtowatch.ui.MovieActivity.bitmapToString;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 09/02/18
 * Time: 16:32
 */

public class DB extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "MoviesToWatch";
    public static final String TABLE_MOVIE = "movies";

    public static final String KEY_ID = "id";
    private static final String KEY_ID_MOVIE = "id_movie";
    private static final String KEY_ID_IMDB = "id_imdb";
    private static final String KEY_IMDB = "imdb";
    private static final String KEY_TITLE = "title";
    private static final String KEY_OTITLE = "original_title";
    private static final String KEY_OLanguage = "original_language";
    private static final String KEY_OVERVIEW = "overview";
    private static final String KEY_POSTER_PATH = "poster_path";
    private static final String KEY_POSTER_IMAGE = "poster_image";
    private static final String KEY_RELEASE_DATE = "release_date";
    private static final String KEY_TAGLINE = "tagline";
    private static final String KEY_RUNTIME = "runtime";
    private static final String KEY_VOTE_AVERAGE = "vote_avarage";
    private static final String KEY_VOTE_COUNT = "vote_count";
    private static final String KEY_GENRES_IDS = "genres_ids";
    private static final String KEY_COMPANIES = "companies";
    private static final String KEY_COUNTRIES = "countries";
    //additional columns
    private static final String KEY_LANG = "language";
    private static final String KEY_RATING = "my_rating";
    private static final String KEY_WATCHED = "watched";
    private static final String KEY_COMMENT = "comment";

    public DB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MOVIE_TABLE = "CREATE TABLE " + TABLE_MOVIE + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_ID_MOVIE + " TEXT," + KEY_ID_IMDB + " TEXT,"
                + KEY_IMDB + " TEXT," + KEY_TITLE + " TEXT," + KEY_OTITLE + " TEXT,"
                + KEY_OLanguage + " TEXT," + KEY_OVERVIEW + " TEXT," + KEY_POSTER_PATH + " TEXT,"
                + KEY_POSTER_IMAGE + " BLOB," + KEY_RELEASE_DATE + " TEXT," + KEY_TAGLINE + " TEXT,"
                + KEY_RUNTIME + " TEXT," + KEY_VOTE_AVERAGE + " TEXT," + KEY_VOTE_COUNT + " TEXT,"
                + KEY_GENRES_IDS + " TEXT," + KEY_COMPANIES + " TEXT," + KEY_COUNTRIES + " TEXT,"
                + KEY_LANG + " TEXT," + KEY_RATING + " TEXT," + KEY_WATCHED + " TEXT,"
                + KEY_COMMENT + " TEXT" + ")";
        db.execSQL(CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIE);
        onCreate(db);
    }

    public void addMovie(MovieBuilder mBuilder) {
        SQLiteDatabase db = this.getWritableDatabase();

        Log.e(Const.DEBUG, mBuilder.getImdb());
        ContentValues values = new ContentValues();
        values.put(KEY_ID_MOVIE, mBuilder.getId());
        values.put(KEY_ID_IMDB, mBuilder.getImdbID());
        values.put(KEY_IMDB, mBuilder.getImdb());
        values.put(KEY_TITLE, mBuilder.getTitle());
        values.put(KEY_OTITLE, mBuilder.getOriginalTitle());
        values.put(KEY_OLanguage, mBuilder.getOriginalLanguage());
        values.put(KEY_OVERVIEW, mBuilder.getOverview());
        values.put(KEY_POSTER_PATH, mBuilder.getPosterPath());
        values.put(KEY_POSTER_IMAGE, mBuilder.getPosterBitmap());
        values.put(KEY_RELEASE_DATE, mBuilder.getReleaseDate());
        values.put(KEY_TAGLINE, mBuilder.getTagline());
        values.put(KEY_RUNTIME, mBuilder.getRuntime());
        values.put(KEY_VOTE_AVERAGE, mBuilder.getVoteAverage());
        values.put(KEY_VOTE_COUNT, mBuilder.getVoteCount());
        values.put(KEY_GENRES_IDS, mBuilder.getGenresIds());
        values.put(KEY_COMPANIES, mBuilder.getCompsArr());
        values.put(KEY_COUNTRIES, mBuilder.getCountrsArr());
        values.put(KEY_LANG, mBuilder.getSavedLang());

        // Inserting Row
        db.insert(TABLE_MOVIE, null, values);
        db.close();
    }

    public Cursor getAllMovies() {
        String selectQuery = "SELECT  * FROM " + TABLE_MOVIE + " ORDER BY " + KEY_ID + " DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery(selectQuery, null);
    }
}