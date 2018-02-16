package com.example.aurora.moviesineedtowatch.ui;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aurora.moviesineedtowatch.R;
import com.example.aurora.moviesineedtowatch.tmdb.DB;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.util.Locale;

import static com.example.aurora.moviesineedtowatch.tmdb.Const.COMPS;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.COUNTRS;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.GENRES_IDS;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.IMDB;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.LANG;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.OTITLE;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.OVERVIEW;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.POST_IMAGE;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.RELEASE_DATE;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.RUNTIME;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.TAGLINE;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.VOTE_AVARG;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.genres;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.ruLocale;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.TITLE;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 25/01/18
 * Time: 20:43
 */

public class MovieActivity extends AppCompatActivity {

    TextView mTitle;
    TextView mOTitle;
    TextView mIMDb;
    TextView mTMDb;
    ImageView mImage;
    TextView mTagline;
    TextView mYear;
    TextView mRuntime;
    TextView mGenres;
    TextView mOverview;
    TextView mCountries;
    TextView mCompanies;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_movie);

        mTitle = findViewById(R.id.title);
        mOTitle = findViewById(R.id.otitle);
        mImage = findViewById(R.id.poster);
        mIMDb = findViewById(R.id.imdb);
        mTMDb = findViewById(R.id.tmdb);
        mTagline = findViewById(R.id.tagline);
        mYear = findViewById(R.id.year);
        mRuntime = findViewById(R.id.runtime);
        mGenres = findViewById(R.id.genres);
        mOverview = findViewById(R.id.overview);
        mCountries = findViewById(R.id.countries);
        mCompanies = findViewById(R.id.companies);

        String movieID = getIntent().getStringExtra("EXTRA_MOVIE_ID");
        setMovieInfo(movieID);
    }

    public void setMovieInfo(String movieID) {

        DB db1 = new DB(MovieActivity.this);
        String selectQuery = "SELECT * FROM " + "movies" + " WHERE " + DB.KEY_ID_MOVIE + "=" + movieID;
        SQLiteDatabase db = db1.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();

        mTitle.setText(cursor.getString(TITLE));
        mOTitle.setText(String.format("%s %s", cursor.getString(LANG), cursor.getString(OTITLE)));
        mTMDb.setText(cursor.getString(VOTE_AVARG));
        mIMDb.setText(cursor.getString(IMDB));
        mImage.setImageBitmap(stringToBitmap(cursor.getString(POST_IMAGE)));
        mTagline.setText(cursor.getString(TAGLINE));
        mRuntime.setText(String.format("%s min", cursor.getString(RUNTIME)));
        mYear.setText(cursor.getString(RELEASE_DATE).subSequence(0, 4));
        mOverview.setText(cursor.getString(OVERVIEW));

        //get genres
        String genresString = "";
        try {
            JSONArray ids = new JSONArray(cursor.getString(GENRES_IDS));
            if (ids.length() == 0) {
                genresString = "not defined";
            } else {
                for (int i=0; i<ids.length(); i++) {
                    genresString += genres.get(ids.get(i))[0] + "\n";
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mGenres.setText(String.valueOf(genresString));

        //get countries
        String countriesString = "";
        try {
            JSONArray ids = new JSONArray(cursor.getString(COUNTRS));
            if (ids.length() == 0) {
                countriesString = "not defined";
            } else {
                for (int i=0; i<ids.length(); i++) {
                    Locale obj = new Locale("", ids.get(i).toString());
                    countriesString += obj.getDisplayCountry(ruLocale) + "\n";
                }
            }
            mCountries.setText(countriesString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //get companies
        String companiesString = "";
        if (cursor.getString(COMPS) == "") {
            countriesString = "not defined";
        } else {
            String delims = ", |\\[|\\]";
            String[] tokens = cursor.getString(COMPS).split(delims);
            for (String token : tokens) companiesString += token + "\n";
        }
        mCompanies.setText(companiesString.trim());

        cursor.close();
    }

    public final static String bitmapToString(Bitmap in){
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        in.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        return Base64.encodeToString(bytes.toByteArray(),Base64.DEFAULT);
    }
    public final static Bitmap stringToBitmap(String in){
        byte[] bytes = Base64.decode(in, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}