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

import static com.example.aurora.moviesineedtowatch.tmdb.Const.GENRES_IDS;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.ID_MOVIE;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.IMDB;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.LANG;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.OTITLE;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.POST_IMAGE;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.RELEASE_DATE;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.TAGLINE;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.TITLE;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.genres;
import static com.example.aurora.moviesineedtowatch.ui.MovieActivity.stringToBitmap;

public class MainActivity extends AppCompatActivity {

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

        TableLayout mTable = findViewById(R.id.main_table);

        DB db = new DB(MainActivity.this);
        Cursor cursor = db.getAllMovies();

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {

                final String movieId = cursor.getString(ID_MOVIE);

                RelativeLayout tr = new RelativeLayout(MainActivity.this);

                //get poster
                ImageView mPoster = new ImageView(MainActivity.this);
                mPoster.setId(1);
                mPoster.setImageBitmap(stringToBitmap(cursor.getString(POST_IMAGE)));

                //title
                final TextView mTitle = new TextView(MainActivity.this);
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
                mGenres.setId(6);
                mGenres.setText(String.valueOf(genresString));

                //get year
                TextView mYear = new TextView(MainActivity.this);
                mYear.setId(7);
                mYear.setText(cursor.getString(RELEASE_DATE));

                //get IMDb rating
                TextView mIMDb = new TextView(MainActivity.this);
                mIMDb.setId(8);
                mIMDb.setText(cursor.getString(IMDB));

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
                        40, RelativeLayout.LayoutParams.WRAP_CONTENT);
                langParams.addRule(RelativeLayout.RIGHT_OF, mPoster.getId());
                langParams.addRule(RelativeLayout.BELOW, mTitle.getId());
                tr.addView(mOLang, langParams);

                RelativeLayout.LayoutParams oTitleParams = new RelativeLayout.LayoutParams(
                        350, RelativeLayout.LayoutParams.WRAP_CONTENT);
                oTitleParams.addRule(RelativeLayout.RIGHT_OF, mOLang.getId());
                oTitleParams.addRule(RelativeLayout.BELOW, mTitle.getId());
                tr.addView(mOTitle, oTitleParams);

                RelativeLayout.LayoutParams taglineParams = new RelativeLayout.LayoutParams(
                        350, RelativeLayout.LayoutParams.WRAP_CONTENT);
                taglineParams.addRule(RelativeLayout.RIGHT_OF, mPoster.getId());
                taglineParams.addRule(RelativeLayout.BELOW, mOLang.getId());
                taglineParams.addRule(RelativeLayout.BELOW, mOTitle.getId());
                tr.addView(mTagline, taglineParams);

                RelativeLayout.LayoutParams genresParams = new RelativeLayout.LayoutParams(
                        170, RelativeLayout.LayoutParams.WRAP_CONTENT);
                genresParams.addRule(RelativeLayout.RIGHT_OF, mPoster.getId());
                genresParams.addRule(RelativeLayout.BELOW, mTagline.getId());
                tr.addView(mGenres, genresParams);

                RelativeLayout.LayoutParams imdbParams = new RelativeLayout.LayoutParams(
                        100, RelativeLayout.LayoutParams.WRAP_CONTENT);
                imdbParams.addRule(RelativeLayout.BELOW, mTitle.getId());
                imdbParams.addRule(RelativeLayout.RIGHT_OF, mOTitle.getId());
                tr.addView(mIMDb, imdbParams);

                RelativeLayout.LayoutParams yearParams = new RelativeLayout.LayoutParams(
                        70, RelativeLayout.LayoutParams.WRAP_CONTENT);
                yearParams.addRule(RelativeLayout.BELOW, mIMDb.getId());
                yearParams.addRule(RelativeLayout.RIGHT_OF, mOTitle.getId());
                tr.addView(mYear, yearParams);

                tr.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        v.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
                        movieTMDB(movieId);
                    }
                });


                mTable.addView(tr);
                cursor.moveToNext();
            }
        }
        cursor.close();
    }

    public void movieTMDB(String movieId) {
        Intent intent = new Intent(this, MovieActivity.class);
        intent.putExtra("EXTRA_MOVIE_ID", movieId);
        startActivity(intent);
    }

    public void searchTMDB() {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }
}