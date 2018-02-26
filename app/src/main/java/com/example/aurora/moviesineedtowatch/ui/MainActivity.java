package com.example.aurora.moviesineedtowatch.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
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

import java.util.Objects;

import static com.example.aurora.moviesineedtowatch.tmdb.Const.GENRES_IDS;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.ID_MOVIE;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.IMDB;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.LANG;
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
        Typeface font = Typeface.createFromAsset(getAssets(), "comic_relief.ttf");

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
                mTitle.setTypeface(font, Typeface.BOLD);
                mTitle.setGravity(Gravity.CENTER);
                mTitle.setTextColor(getResources().getColor(R.color.colorDarkGreen));
                mTitle.setTextSize(20);

                //tagline
                TextView mTagline = new TextView(MainActivity.this);
                mTagline.setId(3);
                mTagline.setText(cursor.getString(TAGLINE));
                mTagline.setTypeface(font, Typeface.BOLD_ITALIC);
                mTagline.setTextSize(16);

                //get genres
                String genresString = "";
                try {
                    JSONArray ids = new JSONArray(cursor.getString(GENRES_IDS));
                    if (ids.length() == 0) {
                        genresString = "not defined";
                    } else {
                        int index = (Objects.equals(cursor.getString(LANG), "true"))?0:1;
                        for (int i=0; i<ids.length(); i++) {
                            genresString += genres.get(ids.get(i))[index] + "\n";
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                TextView mGenres = new TextView(MainActivity.this);
                mGenres.setId(4);
                mGenres.setText(String.valueOf(genresString));
                mGenres.setTypeface(font, Typeface.BOLD);
                mGenres.setTextColor(getResources().getColor(R.color.colorLightBlue));

                //get year
                TextView mYear = new TextView(MainActivity.this);
                mYear.setId(5);
                mYear.setText(cursor.getString(RELEASE_DATE));
                mYear.setTypeface(font, Typeface.BOLD);
                mYear.setGravity(Gravity.CENTER);

                //get IMDb rating
                TextView mIMDb = new TextView(MainActivity.this);
                mIMDb.setId(6);
                mIMDb.setText(cursor.getString(IMDB));
                mIMDb.setTypeface(font, Typeface.BOLD);
                mIMDb.setBackgroundColor(getResources().getColor(chooseColor(cursor.getString(IMDB))));
                mIMDb.setTextColor(getResources().getColor(R.color.colorBeige));
                mIMDb.setTextSize(18);
                mIMDb.setGravity(Gravity.CENTER);


                RelativeLayout.LayoutParams posterParams = new RelativeLayout.LayoutParams(
                        230, RelativeLayout.LayoutParams.WRAP_CONTENT);
                posterParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
                posterParams.setMargins(20,0,20, 20);
                tr.addView(mPoster, posterParams);

                RelativeLayout.LayoutParams titleParams = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                titleParams.addRule(RelativeLayout.ALIGN_TOP);
                tr.addView(mTitle, titleParams);

                RelativeLayout.LayoutParams taglineParams = new RelativeLayout.LayoutParams(
                        550, RelativeLayout.LayoutParams.WRAP_CONTENT);
                taglineParams.addRule(RelativeLayout.RIGHT_OF, mPoster.getId());
                taglineParams.addRule(RelativeLayout.BELOW, mTitle.getId());
                taglineParams.setMargins(0, 5, 20, 10);
                tr.addView(mTagline, taglineParams);

                RelativeLayout.LayoutParams genresParams = new RelativeLayout.LayoutParams(
                        220, RelativeLayout.LayoutParams.WRAP_CONTENT);
                genresParams.addRule(RelativeLayout.RIGHT_OF, mPoster.getId());
                genresParams.addRule(RelativeLayout.BELOW, mTagline.getId());
                genresParams.setMargins(0, 0, 10, 10);
                tr.addView(mGenres, genresParams);

                RelativeLayout.LayoutParams imdbParams = new RelativeLayout.LayoutParams(
                        200, 230);
                imdbParams.addRule(RelativeLayout.BELOW, mTagline.getId());
                imdbParams.addRule(RelativeLayout.RIGHT_OF, mGenres.getId());
                imdbParams.setMargins(10, 5, 20, 10);
                tr.addView(mIMDb, imdbParams);

                RelativeLayout.LayoutParams yearParams = new RelativeLayout.LayoutParams(
                        200, RelativeLayout.LayoutParams.WRAP_CONTENT);
                yearParams.addRule(RelativeLayout.BELOW, mIMDb.getId());
                yearParams.addRule(RelativeLayout.RIGHT_OF, mGenres.getId());
                yearParams.setMargins(10, 0, 20, 20);
                tr.addView(mYear, yearParams);

                tr.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        movieTMDB(movieId);
                    }
                });

                mTable.addView(tr);
                cursor.moveToNext();
            }
        }
        cursor.close();
    }

    private int chooseColor(String imdbRating) {
        float rating = Float.parseFloat(imdbRating);
        int color = R.color.OutOfBound;

        if (rating<5.0f) {
            color = R.color.VeryBad;
        } else if (5.0f<= rating && rating<=5.9f) {
            color = R.color.Bad;
        } else if (6.0f<= rating && rating<=6.5f) {
            color = R.color.Avarage;
        } else if (6.6f<= rating && rating<=6.8f) {
            color = R.color.AboveAvarage;
        } else if (6.9f<= rating && rating<=7.2f) {
            color = R.color.Intermediate;
        } else if (7.3f<= rating && rating<=7.7f) {
            color = R.color.Good;
        } else if (7.8f<= rating && rating<=8.1f) {
            color = R.color.VeryGood;
        } else if (8.2f<= rating && rating<=8.5f) {
            color = R.color.Great;
        } else if (8.6f<= rating && rating<=8.9f) {
            color = R.color.Adept;
        } else if ( rating >= 9.0f) {
            color = R.color.Unicum;
        }

        return color;
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