package com.example.aurora.moviesineedtowatch.tmdb;

import android.content.ContentValues;
import android.content.Context;
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
    private static final String TABLE_MOVIE = "movies";

    private static final String KEY_ID = "id";
    public static final String KEY_ID_MOVIE = "id_movie";
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
                + KEY_GENRES_IDS + " TEXT," + KEY_COMPANIES + " TEXT," + KEY_COUNTRIES + " TEXT" + ")";
        db.execSQL(CREATE_MOVIE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIE);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new contact
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
        values.put(KEY_POSTER_IMAGE, bitmapToString(mBuilder.getPosterBitmap()));
        values.put(KEY_RELEASE_DATE, mBuilder.getReleaseDate());
        values.put(KEY_TAGLINE, mBuilder.getTagline());
        values.put(KEY_RUNTIME, String.valueOf(mBuilder.getRuntime()));
        values.put(KEY_VOTE_AVERAGE, String.valueOf(mBuilder.getVoteAverage()));
        values.put(KEY_VOTE_COUNT, String.valueOf(mBuilder.getVoteCount()));
        values.put(KEY_GENRES_IDS, String.valueOf(mBuilder.getGenresIds()));
        values.put(KEY_COMPANIES, String.valueOf(mBuilder.getComps()));
        values.put(KEY_COUNTRIES, String.valueOf(mBuilder.getCountrs()));

//        String str = String.valueOf(mBuilder.getGenresIds());
//        Log.e(Const.SEE, str);
//
//        try {
//            JSONArray ids = new JSONArray(str);
//            Log.e(Const.SEE, ids.get(1).toString());
//            Log.e(Const.DEBUG, "eeeeeeeeeeeeeeee");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }


        // Inserting Row
        db.insert(TABLE_MOVIE, null, values);
        db.close(); // Closing database connection
    }

    // Getting single contact
//    MovieBuilder getMovie(int id) {
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID,
//                        KEY_NAME, KEY_PH_NO }, KEY_ID + "=?",
//                new String[] { String.valueOf(id) }, null, null, null, null);
//        if (cursor != null)
//            cursor.moveToFirst();
//
////        MovieBuilder contact = new MovieBuilder(Integer.parseInt(cursor.getString(0)),
////                cursor.getString(1), cursor.getString(2));
//        // return contact
//        return movie;
//    }

//    // Getting All Contacts
//    public List<Contact> getAllContacts() {
//        List<Contact> contactList = new ArrayList<Contact>();
//        // Select All Query
//        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;
//
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
//
//        // looping through all rows and adding to list
//        if (cursor.moveToFirst()) {
//            do {
//                Contact contact = new Contact();
//                contact.setID(Integer.parseInt(cursor.getString(0)));
//                contact.setName(cursor.getString(1));
//                contact.setPhoneNumber(cursor.getString(2));
//                // Adding contact to list
//                contactList.add(contact);
//            } while (cursor.moveToNext());
//        }
//
//        // return contact list
//        return contactList;
//    }

//    // Updating single contact
//    public int updateContact(Contact contact) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(KEY_NAME, contact.getName());
//        values.put(KEY_PH_NO, contact.getPhoneNumber());
//
//        // updating row
//        return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
//                new String[] { String.valueOf(contact.getID()) });
//    }
//
//    // Deleting single contact
//    public void deleteContact(Contact contact) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
//                new String[] { String.valueOf(contact.getID()) });
//        db.close();
//    }

//
//    // Getting contacts Count
//    public int getContactsCount() {
//        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(countQuery, null);
//        cursor.close();
//
//        // return count
//        return cursor.getCount();
//    }

}