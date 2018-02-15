package com.example.aurora.moviesineedtowatch.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.aurora.moviesineedtowatch.R;
import com.example.aurora.moviesineedtowatch.tmdb.Const;
import com.example.aurora.moviesineedtowatch.tmdb.DB;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static com.example.aurora.moviesineedtowatch.tmdb.Const.IMAGE_PATH;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.IMAGE_SIZE;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.genres;
import static com.example.aurora.moviesineedtowatch.ui.MovieActivity.stringToBitmap;

public class MainActivity extends AppCompatActivity {


    public final static int ID = 0;
    public final static int ID_MOVIE = 1;
    public final static int ID_IMDB = 2;
    public final static int IMDB = 3;
    public final static int TITLE = 4;
    public final static int OTITLE = 5;
    public final static int LANG = 6;
    public final static int OVERVIEW = 7;
    public final static int POST_PATH = 8;
    public final static int POST_IMAGE = 9;
    public final static int RELEASE_DATE = 10;
    public final static int TAGLINE = 11;
    public final static int RUNTIME = 12;
    public final static int VOTE_AVARG = 13;
    public final static int VOTE_COUNT = 14;
    public final static int GENRES_IDS = 15;
    public final static int COMPS = 16;
    public final static int COUNTRS = 17;



    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        ImageButton searchButton = findViewById(R.id.main_search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                searchTMDB();
            }
        });
//        movieTMDB();

        TableLayout mTable = findViewById(R.id.main_table);

        DB database = new DB(MainActivity.this);

        String selectQuery = "SELECT  * FROM " + "movies";
        SQLiteDatabase db = database.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {

                RelativeLayout tr = new RelativeLayout(MainActivity.this);

                //get poster
                ImageView mPoster = new ImageView(MainActivity.this);
                mPoster.setId(1);
                mPoster.setImageBitmap(stringToBitmap(cursor.getString(POST_IMAGE)));

                //title
                TextView mTitle = new TextView(MainActivity.this);
                mTitle.setId(2);
                mTitle.setText(cursor.getString(TITLE));

                //original title
                TextView mOTitle = new TextView(MainActivity.this);
                mOTitle.setId(3);
                mOTitle.setText(cursor.getString(OTITLE));

                //original language
                TextView mOLang = new TextView(MainActivity.this);
                mOLang.setId(4);
                mOLang.setText(cursor.getString(LANG));

                //tagline
                TextView mTagline = new TextView(MainActivity.this);
                mTagline.setId(5);
                mTagline.setText(cursor.getString(TAGLINE));

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

                TextView mGenres = new TextView(MainActivity.this);
                mGenres.setId(4);
                mGenres.setText(String.valueOf(genresString));

                //get year
                TextView mYear = new TextView(MainActivity.this);
                mYear.setId(5);
                mYear.setText(cursor.getString(RELEASE_DATE).subSequence(0, 4));
                Log.e(Const.DEBUG, cursor.getString(RELEASE_DATE));

                //get TMDb rating
                TextView mIMDb = new TextView(MainActivity.this);
                mIMDb.setId(6);
                mIMDb.setText(cursor.getString(IMDB));
                Log.e(Const.DEBUG, cursor.getString(IMDB));


                RelativeLayout.LayoutParams posterParams = new RelativeLayout.LayoutParams(
                        230, RelativeLayout.LayoutParams.WRAP_CONTENT);
                posterParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
                tr.addView(mPoster, posterParams);

                RelativeLayout.LayoutParams titleParams = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                titleParams.addRule(RelativeLayout.ALIGN_TOP);
                mTitle.setGravity(Gravity.CENTER);
                tr.addView(mTitle, titleParams);

                RelativeLayout.LayoutParams langParams = new RelativeLayout.LayoutParams(
                        140, RelativeLayout.LayoutParams.WRAP_CONTENT);
                langParams.addRule(RelativeLayout.RIGHT_OF, mPoster.getId());
                langParams.addRule(RelativeLayout.BELOW, mTitle.getId());
                tr.addView(mOLang, langParams);

                RelativeLayout.LayoutParams oTitleParams = new RelativeLayout.LayoutParams(
                        230, RelativeLayout.LayoutParams.WRAP_CONTENT);
                oTitleParams.addRule(RelativeLayout.RIGHT_OF, mPoster.getId());
                oTitleParams.addRule(RelativeLayout.BELOW, mTitle.getId());
                tr.addView(mOTitle, oTitleParams);

                RelativeLayout.LayoutParams taglineParams = new RelativeLayout.LayoutParams(
                        170, RelativeLayout.LayoutParams.WRAP_CONTENT);
                taglineParams.addRule(RelativeLayout.RIGHT_OF, mPoster.getId());
                taglineParams.addRule(RelativeLayout.BELOW, mOLang.getId());
                tr.addView(mTagline, taglineParams);

                RelativeLayout.LayoutParams genresParams = new RelativeLayout.LayoutParams(
                        170, RelativeLayout.LayoutParams.WRAP_CONTENT);
                genresParams.addRule(RelativeLayout.RIGHT_OF, mPoster.getId());
                genresParams.addRule(RelativeLayout.BELOW, mTagline.getId());
                tr.addView(mGenres, genresParams);

                RelativeLayout.LayoutParams imdbParams = new RelativeLayout.LayoutParams(
                        100, RelativeLayout.LayoutParams.WRAP_CONTENT);
                imdbParams.addRule(RelativeLayout.BELOW, mTitle.getId());
                imdbParams.addRule(RelativeLayout.RIGHT_OF, mTagline.getId());
                tr.addView(mIMDb, imdbParams);

                RelativeLayout.LayoutParams yearParams = new RelativeLayout.LayoutParams(
                        50, RelativeLayout.LayoutParams.WRAP_CONTENT);
                yearParams.addRule(RelativeLayout.BELOW, mIMDb.getId());
                tr.addView(mYear, yearParams);

                mTable.addView(tr);


                cursor.moveToNext();
            }
        }
        cursor.close();

    }

    public void movieTMDB() {
        Intent intent = new Intent(this, MovieActivity.class);
        startActivity(intent);
    }

    public void searchTMDB() {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }
}