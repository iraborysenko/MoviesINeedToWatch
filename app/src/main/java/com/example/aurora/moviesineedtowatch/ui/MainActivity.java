package com.example.aurora.moviesineedtowatch.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.aurora.moviesineedtowatch.R;
import com.example.aurora.moviesineedtowatch.tmdb.Const;
import com.example.aurora.moviesineedtowatch.tmdb.DB;
import com.example.aurora.moviesineedtowatch.tmdb.MovieBuilder;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.example.aurora.moviesineedtowatch.tmdb.Const.DEBUG;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.GENRES_IDS;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.ID;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.ID_MOVIE;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.IMDB;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.LANG;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.POST_IMAGE;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.RELEASE_DATE;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.SEE;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.SHARED_REFERENCES;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.TAGLINE;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.TITLE;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.genres;
import static com.example.aurora.moviesineedtowatch.ui.MovieActivity.stringToBitmap;

public class MainActivity extends AppCompatActivity {
    TableLayout mTable;
    Typeface font;
    private static Realm mRealm;

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
        font = Typeface.createFromAsset(getAssets(), "comic_relief.ttf");
        mTable = findViewById(R.id.main_table);

        Realm.init(this);
        mRealm = Realm.getDefaultInstance();

//        showDBData();
        displayListOfMovies();

    }

    @SuppressLint("ResourceType")
    private void displayListOfMovies() {
        mRealm.beginTransaction();
        RealmResults<MovieBuilder> movies = mRealm.where(MovieBuilder.class).findAll();
        MovieBuilder movie;

        if(!movies.isEmpty()) {
            for(int i = movies.size() - 1; i >= 0; i--) {
                movie = movies.get(i);
                assert movie != null;

                final String movieId = movie.getId();
                final String dataLang = movie.getSavedLang();

                RelativeLayout tr = new RelativeLayout(MainActivity.this);

                //get poster
                ImageView mPoster = new ImageView(MainActivity.this);
                mPoster.setId(1);

                RequestOptions options = new RequestOptions()
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE);
                Glide.with(this)
                        .asBitmap()
                        .load(stringToBitmap(movie.getPosterBitmap()))
                        .apply(options)
                        .into(mPoster);

                //title
                final TextView mTitle = new TextView(MainActivity.this);
                mTitle.setId(2);
                mTitle.setText(movie.getTitle());
                mTitle.setTypeface(font, Typeface.BOLD);
                mTitle.setGravity(Gravity.CENTER);
                mTitle.setTextColor(getResources().getColor(R.color.colorDarkGreen));
                mTitle.setTextSize(20);

                //tagline
                TextView mTagline = new TextView(MainActivity.this);
                mTagline.setId(3);
                mTagline.setText(movie.getTagline());
                mTagline.setTypeface(font, Typeface.BOLD_ITALIC);
                mTagline.setTextSize(16);

                //get genres
                StringBuilder genresString = new StringBuilder();
                try {
                    JSONArray ids = new JSONArray(movie.getGenresIds());
                    if (ids.length() == 0) {
                        genresString = new StringBuilder("not defined");
                    } else {
                        int index = (Objects.equals(movie.getSavedLang(), "true"))?0:1;
                        for (int j=0; j<ids.length(); j++)
                            genresString.append(genres.get(ids.get(j))[index]).append("\n");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                TextView mGenres = new TextView(MainActivity.this);
                mGenres.setId(4);
                mGenres.setText(String.valueOf(genresString.toString()));
                mGenres.setTypeface(font, Typeface.BOLD);
                mGenres.setTextColor(getResources().getColor(R.color.colorLightBlue));

                //get year
                TextView mYear = new TextView(MainActivity.this);
                mYear.setId(5);
                mYear.setText(movie.getReleaseDate());
                mYear.setTypeface(font, Typeface.BOLD);
                mYear.setGravity(Gravity.CENTER);

                //get IMDb rating
                TextView mIMDb = new TextView(MainActivity.this);
                mIMDb.setId(6);
                mIMDb.setText(movie.getImdb());
                mIMDb.setTypeface(font, Typeface.BOLD);
                mIMDb.setBackgroundColor(getResources().getColor(chooseColor(movie.getImdb())));
                mIMDb.setTextColor(getResources().getColor(R.color.colorBeige));
                mIMDb.setTextSize(18);
                mIMDb.setGravity(Gravity.CENTER);


                RelativeLayout.LayoutParams posterParams = new RelativeLayout.LayoutParams(
                        230, 490);
                posterParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
                posterParams.setMargins(20,0,15, 20);
                tr.addView(mPoster, posterParams);

                RelativeLayout.LayoutParams titleParams = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                titleParams.addRule(RelativeLayout.ALIGN_TOP);
                tr.addView(mTitle, titleParams);

                RelativeLayout.LayoutParams taglineParams = new RelativeLayout.LayoutParams(
                        550, RelativeLayout.LayoutParams.WRAP_CONTENT);
                taglineParams.addRule(RelativeLayout.RIGHT_OF, mPoster.getId());
                taglineParams.addRule(RelativeLayout.BELOW, mTitle.getId());
                taglineParams.setMargins(0, 7, 20, 10);
                tr.addView(mTagline, taglineParams);

                RelativeLayout.LayoutParams genresParams = new RelativeLayout.LayoutParams(
                        235, RelativeLayout.LayoutParams.WRAP_CONTENT);
                genresParams.addRule(RelativeLayout.RIGHT_OF, mPoster.getId());
                genresParams.addRule(RelativeLayout.BELOW, mTagline.getId());
                genresParams.setMargins(0, 0, 5, 10);
                tr.addView(mGenres, genresParams);

                RelativeLayout.LayoutParams imdbParams = new RelativeLayout.LayoutParams(
                        190, 230);
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

                tr.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        movieTMDB(movieId, dataLang);
                    }
                });

//                tr.setOnLongClickListener(new View.OnLongClickListener() {
//                    @Override
//                    public boolean onLongClick(View v) {
//                        Log.e(Const.TAG, "it's long click listener");
//                        DB db1 = new DB(MainActivity.this);
//                        SQLiteDatabase db = db1.getWritableDatabase();
//                        db.delete(DB.TABLE_MOVIE, "id = " + rowId, null);
//                        showDBData();
//                        return true;
//                    }
//                });


                mTable.addView(tr);
            }

        } else {
            Log.e(DEBUG, "No data yet.");
        }
        mRealm.commitTransaction();

    }

    @Override
    protected void onRestart() {
        super.onRestart();

        SharedPreferences settings = getSharedPreferences(SHARED_REFERENCES, MODE_PRIVATE);
        boolean is_changed = settings.getBoolean("db_is_changed", false);
        if(is_changed){
            Log.e(Const.TAG, "I'm onRestart!");
//            showDBData();
            SharedPreferences.Editor editor = getSharedPreferences(SHARED_REFERENCES, MODE_PRIVATE).edit();
            editor.putBoolean("db_is_changed", false);
            editor.apply();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }

    public static Realm getRealm() {
        return mRealm;
    }
//
//    @SuppressLint("ResourceType")
//    private void showDBData() {
//        mTable.removeAllViewsInLayout();
//
//        DB db = new DB(MainActivity.this);
//        Cursor cursor = db.getAllMovies();
//
//        if (cursor.moveToFirst()) {
//            while (!cursor.isAfterLast()) {
//
//                final String rowId = cursor.getString(ID);
//
//                RelativeLayout tr = new RelativeLayout(MainActivity.this);
//
//                //get poster
//                ImageView mPoster = new ImageView(MainActivity.this);
//                mPoster.setId(1);
//
//                RequestOptions options = new RequestOptions()
//                        .skipMemoryCache(true)
//                        .diskCacheStrategy(DiskCacheStrategy.NONE);
//                Glide.with(this)
//                        .asBitmap()
//                        .load(stringToBitmap(cursor.getString(POST_IMAGE)))
//                        .apply(options)
//                        .into(mPoster);
//
//                //title
//                final TextView mTitle = new TextView(MainActivity.this);
//                mTitle.setId(2);
//                mTitle.setText(cursor.getString(TITLE));
//                mTitle.setTypeface(font, Typeface.BOLD);
//                mTitle.setGravity(Gravity.CENTER);
//                mTitle.setTextColor(getResources().getColor(R.color.colorDarkGreen));
//                mTitle.setTextSize(20);
//
//                //tagline
//                TextView mTagline = new TextView(MainActivity.this);
//                mTagline.setId(3);
//                mTagline.setText(cursor.getString(TAGLINE));
//                mTagline.setTypeface(font, Typeface.BOLD_ITALIC);
//                mTagline.setTextSize(16);
//
//                //get genres
//                StringBuilder genresString = new StringBuilder();
//                try {
//                    JSONArray ids = new JSONArray(cursor.getString(GENRES_IDS));
//                    if (ids.length() == 0) {
//                        genresString = new StringBuilder("not defined");
//                    } else {
//                        int index = (Objects.equals(cursor.getString(LANG), "true"))?0:1;
//                        for (int i=0; i<ids.length(); i++)
//                            genresString.append(genres.get(ids.get(i))[index]).append("\n");
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                TextView mGenres = new TextView(MainActivity.this);
//                mGenres.setId(4);
//                mGenres.setText(String.valueOf(genresString.toString()));
//                mGenres.setTypeface(font, Typeface.BOLD);
//                mGenres.setTextColor(getResources().getColor(R.color.colorLightBlue));
//
//                //get year
//                TextView mYear = new TextView(MainActivity.this);
//                mYear.setId(5);
//                mYear.setText(cursor.getString(RELEASE_DATE));
//                mYear.setTypeface(font, Typeface.BOLD);
//                mYear.setGravity(Gravity.CENTER);
//
//                //get IMDb rating
//                TextView mIMDb = new TextView(MainActivity.this);
//                mIMDb.setId(6);
//                mIMDb.setText(cursor.getString(IMDB));
//                mIMDb.setTypeface(font, Typeface.BOLD);
//                mIMDb.setBackgroundColor(getResources().getColor(chooseColor(cursor.getString(IMDB))));
//                mIMDb.setTextColor(getResources().getColor(R.color.colorBeige));
//                mIMDb.setTextSize(18);
//                mIMDb.setGravity(Gravity.CENTER);
//
//
//                RelativeLayout.LayoutParams posterParams = new RelativeLayout.LayoutParams(
//                        230, 490);
//                posterParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
//                posterParams.setMargins(20,0,15, 20);
//                tr.addView(mPoster, posterParams);
//
//                RelativeLayout.LayoutParams titleParams = new RelativeLayout.LayoutParams(
//                        RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//                titleParams.addRule(RelativeLayout.ALIGN_TOP);
//                tr.addView(mTitle, titleParams);
//
//                RelativeLayout.LayoutParams taglineParams = new RelativeLayout.LayoutParams(
//                        550, RelativeLayout.LayoutParams.WRAP_CONTENT);
//                taglineParams.addRule(RelativeLayout.RIGHT_OF, mPoster.getId());
//                taglineParams.addRule(RelativeLayout.BELOW, mTitle.getId());
//                taglineParams.setMargins(0, 7, 20, 10);
//                tr.addView(mTagline, taglineParams);
//
//                RelativeLayout.LayoutParams genresParams = new RelativeLayout.LayoutParams(
//                        235, RelativeLayout.LayoutParams.WRAP_CONTENT);
//                genresParams.addRule(RelativeLayout.RIGHT_OF, mPoster.getId());
//                genresParams.addRule(RelativeLayout.BELOW, mTagline.getId());
//                genresParams.setMargins(0, 0, 5, 10);
//                tr.addView(mGenres, genresParams);
//
//                RelativeLayout.LayoutParams imdbParams = new RelativeLayout.LayoutParams(
//                        190, 230);
//                imdbParams.addRule(RelativeLayout.BELOW, mTagline.getId());
//                imdbParams.addRule(RelativeLayout.RIGHT_OF, mGenres.getId());
//                imdbParams.setMargins(10, 5, 20, 10);
//                tr.addView(mIMDb, imdbParams);
//
//                RelativeLayout.LayoutParams yearParams = new RelativeLayout.LayoutParams(
//                        200, RelativeLayout.LayoutParams.WRAP_CONTENT);
//                yearParams.addRule(RelativeLayout.BELOW, mIMDb.getId());
//                yearParams.addRule(RelativeLayout.RIGHT_OF, mGenres.getId());
//                yearParams.setMargins(10, 0, 20, 20);
//                tr.addView(mYear, yearParams);
//
//                tr.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v)
//                    {
//                        movieTMDB(rowId);
//                    }
//                });
//
//                tr.setOnLongClickListener(new View.OnLongClickListener() {
//                    @Override
//                    public boolean onLongClick(View v) {
//                        Log.e(Const.TAG, "it's long click listener");
//                        DB db1 = new DB(MainActivity.this);
//                        SQLiteDatabase db = db1.getWritableDatabase();
//                        db.delete(DB.TABLE_MOVIE, "id = " + rowId, null);
//                        showDBData();
//                        return true;
//                    }
//                });
//
//
//                mTable.addView(tr);
//                cursor.moveToNext();
//            }
//        }
//        cursor.close();
//    }

    private int chooseColor(String imdbRating) {
        int color = R.color.OutOfBound;
        if (imdbRating.equals("none")) {
            color = R.color.NoMovie;
        } else {
            float rating = Float.parseFloat(imdbRating);
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
        }

        return color;
    }

    private void movieTMDB(String movieId, String dataLang) {
        Intent intent = new Intent(this, MovieActivity.class);
        intent.putExtra("EXTRA_MOVIE_ID", movieId);
        intent.putExtra("EXTRA_DATA_LANG", dataLang);
        startActivity(intent);
    }

    private void searchTMDB() {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }
}