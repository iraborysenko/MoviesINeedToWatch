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
import com.example.aurora.moviesineedtowatch.tmdb.Const;
import com.example.aurora.moviesineedtowatch.tmdb.DB;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.util.Locale;
import java.util.Objects;

import static com.example.aurora.moviesineedtowatch.tmdb.Const.COMPS;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.COUNTRS;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.GENRES_IDS;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.IMDB;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.OLANG;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.OTITLE;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.OVERVIEW;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.POST_IMAGE;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.RELEASE_DATE;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.RUNTIME;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.TAGLINE;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.VOTE_AVARG;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.TITLE;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.LANG;

import static com.example.aurora.moviesineedtowatch.tmdb.Const.genres;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.lang;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 25/01/18
 * Time: 20:43
 */

public class MovieActivity extends AppCompatActivity {

    private TextView mTitle;
    private TextView mOTitle;
    private TextView mIMDb;
    private TextView mTMDb;
    private ImageView mImage;
    private TextView mTagline;
    private TextView mYear;
    private TextView mRuntime;
    private TextView mGenres;
    private TextView mOverview;
    private TextView mCountries;
    private TextView mCompanies;

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

    private void setMovieInfo(String movieID) {

        DB db1 = new DB(MovieActivity.this);
        String selectQuery = "SELECT * FROM " + "movies" + " WHERE " + DB.KEY_ID_MOVIE + "=" + movieID;
        SQLiteDatabase db = db1.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();

        mTitle.setText(cursor.getString(TITLE));
        mOTitle.setText(String.format("%s %s", cursor.getString(OLANG), cursor.getString(OTITLE)));
        mTMDb.setText(cursor.getString(VOTE_AVARG));
        mIMDb.setText(cursor.getString(IMDB));
        mImage.setImageBitmap(stringToBitmap(cursor.getString(POST_IMAGE)));
        mTagline.setText(cursor.getString(TAGLINE));
        mRuntime.setText(cursor.getString(RUNTIME));
        mYear.setText(cursor.getString(RELEASE_DATE));
        mOverview.setText(cursor.getString(OVERVIEW));

        //get genres
        StringBuilder genresString = new StringBuilder();
        try {
            JSONArray ids = new JSONArray(cursor.getString(GENRES_IDS));
            if (ids.length() == 0) {
                genresString = new StringBuilder("not defined");
            } else {
                int index = (Objects.equals(cursor.getString(LANG), "true"))?0:1;
                for (int i=0; i<ids.length(); i++)
                    genresString.append(genres.get(ids.get(i))[index]).append("\n");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mGenres.setText(String.valueOf(genresString.toString()));

        //get countries
        StringBuilder countriesString = new StringBuilder();
        try {
            JSONArray ids = new JSONArray(cursor.getString(COUNTRS));
            if (ids.length() == 0) {
                countriesString = new StringBuilder("not defined");
            } else {
                for (int i=0; i<ids.length(); i++) {
                    Locale obj = new Locale("", ids.get(i).toString());
                    countriesString.append(obj.getDisplayCountry(lang.get(cursor.getString(LANG)))).append("\n");
                }
            }
            mCountries.setText(countriesString.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //get companies
        StringBuilder companiesString = new StringBuilder();
        if (Objects.equals(cursor.getString(COMPS), "[]")) {
            companiesString = new StringBuilder("not defined");
        } else {
            String delims = ", |\\[|]";
            String[] tokens = cursor.getString(COMPS).split(delims);
            for (String token : tokens) companiesString.append(token).append("\n");
        }
        mCompanies.setText(companiesString.toString().trim());

        cursor.close();
    }

    public static String bitmapToString(Bitmap in){
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        in.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        return Base64.encodeToString(bytes.toByteArray(),Base64.DEFAULT);
    }
    public static Bitmap stringToBitmap(String in){
        byte[] bytes = Base64.decode(in, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}