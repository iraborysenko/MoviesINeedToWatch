package com.example.aurora.moviesineedtowatch.ui;

import static com.example.aurora.moviesineedtowatch.tmdb.Const.DEBUG;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.genres;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.SEE;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.SHARED_REFERENCES;
import static com.example.aurora.moviesineedtowatch.ui.MovieActivity.stringToBitmap;
import static com.example.aurora.moviesineedtowatch.adaprer.RecyclerAdapter.ClickListener;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.aurora.moviesineedtowatch.R;
import com.example.aurora.moviesineedtowatch.adaprer.RecyclerAdapter;
import com.example.aurora.moviesineedtowatch.tmdb.Const;
import com.example.aurora.moviesineedtowatch.tmdb.Movie;

import org.json.JSONArray;
import org.json.JSONException;

import io.realm.Realm;
import io.realm.RealmResults;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    @SuppressLint("StaticFieldLeak")
    private static Realm sRealm;
    TableLayout mTable;
    Typeface mFont;

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
        mFont = Typeface.createFromAsset(getAssets(), "comic_relief.ttf");
//        mTable = findViewById(R.id.main_table);

        Realm.init(this);
        sRealm = Realm.getDefaultInstance();

//        displayListOfMovies();


        sRealm.beginTransaction();
        RealmResults<Movie> movies = sRealm.where(Movie.class).findAll();
        sRealm.commitTransaction();



        RecyclerView mRecyclerView = findViewById(R.id.movie_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerAdapter mAdapter = new RecyclerAdapter(movies, getApplicationContext(), getResources());
        mRecyclerView.setAdapter(mAdapter);


        mAdapter.setOnItemClickListener(new RecyclerAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Log.e(Const.TAG, "onItemClick position: " + position);
            }

            @Override
            public void onItemLongClick(int position, View v) {
                Log.e(Const.TAG, "onItemLongClick pos = " + position);
            }
        });

    }

//    private String[] getDataSet() {
//
//        String[] mDataSet = new String[100];
//        for (int i = 0; i < 50; i++) {
//            mDataSet[i] = "item" + i;
//        }
//        return mDataSet;
//    }

//    @SuppressLint("ResourceType")
//    private void displayListOfMovies() {
//        mTable.removeAllViewsInLayout();
//
//        sRealm.beginTransaction();
//        RealmResults<Movie> movies = sRealm.where(Movie.class).findAll();
//        sRealm.commitTransaction();
//        Movie movie;
//
//        if(!movies.isEmpty()) {
//            for(int i = movies.size() - 1; i >= 0; i--) {
//                movie = movies.get(i);
//                assert movie != null;
//
//                final String movieId = movie.getId();
//                final String dataLang = movie.getSavedLang();
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
//                        .load(stringToBitmap(movie.getPosterBitmap()))
//                        .apply(options)
//                        .into(mPoster);
//
//                //title
//                final TextView mTitle = new TextView(MainActivity.this);
//                mTitle.setId(2);
//                mTitle.setText(movie.getTitle());
//                mTitle.setTypeface(mFont, Typeface.BOLD);
//                mTitle.setGravity(Gravity.CENTER);
//                mTitle.setTextColor(getResources().getColor(R.color.colorDarkGreen));
//                mTitle.setTextSize(20);
//
//                //tagline
//                TextView mTagline = new TextView(MainActivity.this);
//                mTagline.setId(3);
//                mTagline.setText(movie.getTagline());
//                mTagline.setTypeface(mFont, Typeface.BOLD_ITALIC);
//                mTagline.setTextSize(16);
//
//                //get genres
//                StringBuilder genresString = new StringBuilder();
//                try {
//                    JSONArray ids = new JSONArray(movie.getGenresIds());
//                    if (ids.length() == 0) {
//                        genresString = new StringBuilder("not defined");
//                    } else {
//                        int index = (Objects.equals(movie.getSavedLang(), "true"))?0:1;
//                        for (int j=0; j<ids.length(); j++)
//                            genresString.append(genres.get(ids.get(j))[index]).append("\n");
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                TextView mGenres = new TextView(MainActivity.this);
//                mGenres.setId(4);
//                mGenres.setText(String.valueOf(genresString.toString()));
//                mGenres.setTypeface(mFont, Typeface.BOLD);
//                mGenres.setTextColor(getResources().getColor(R.color.colorLightBlue));
//
//                //get year
//                TextView mYear = new TextView(MainActivity.this);
//                mYear.setId(5);
//                mYear.setText(movie.getReleaseDate());
//                mYear.setTypeface(mFont, Typeface.BOLD);
//                mYear.setGravity(Gravity.CENTER);
//
//                //get IMDb rating
//                TextView mIMDb = new TextView(MainActivity.this);
//                mIMDb.setId(6);
//                mIMDb.setText(movie.getImdb());
//                mIMDb.setTypeface(mFont, Typeface.BOLD);
////                mIMDb.setBackgroundColor(getResources().getColor(chooseColor(movie.getImdb())));
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
//                        movieTMDB(movieId, dataLang);
//                    }
//                });
//
//                tr.setOnLongClickListener(new View.OnLongClickListener() {
//                    @Override
//                    public boolean onLongClick(View v) {
//                        sRealm.beginTransaction();
//
//                        RealmResults<Movie> result = sRealm.where(Movie.class)
//                                .equalTo("id", movieId)
//                                .equalTo("savedLang", dataLang)
//                                .findAll();
//                        result.deleteAllFromRealm();
//                        sRealm.commitTransaction();
//
//                        displayListOfMovies();
//                        return true;
//                    }
//                });
//
//
//                mTable.addView(tr);
//            }
//
//        } else {
//            Log.i(DEBUG, "No data yet.");
//        }
//
//    }

    @Override
    protected void onRestart() {
        super.onRestart();

        SharedPreferences settings = getSharedPreferences(SHARED_REFERENCES, MODE_PRIVATE);
        boolean is_changed = settings.getBoolean("db_is_changed", false);
        if(is_changed){
            Log.i(Const.TAG, "I'm onRestart!");
//            displayListOfMovies();
            SharedPreferences.Editor editor = getSharedPreferences(SHARED_REFERENCES, MODE_PRIVATE).edit();
            editor.putBoolean("db_is_changed", false);
            editor.apply();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sRealm.close();
    }

    public static Realm getRealm() {
        return sRealm;
    }

//    private int chooseColor(String imdbRating) {
//        int color = R.color.OutOfBound;
//        if (imdbRating.equals("none")) {
//            color = R.color.NoMovie;
//        } else {
//            float rating = Float.parseFloat(imdbRating);
//            if (rating<5.0f) {
//                color = R.color.VeryBad;
//            } else if (5.0f<= rating && rating<=5.9f) {
//                color = R.color.Bad;
//            } else if (6.0f<= rating && rating<=6.5f) {
//                color = R.color.Avarage;
//            } else if (6.6f<= rating && rating<=6.8f) {
//                color = R.color.AboveAvarage;
//            } else if (6.9f<= rating && rating<=7.2f) {
//                color = R.color.Intermediate;
//            } else if (7.3f<= rating && rating<=7.7f) {
//                color = R.color.Good;
//            } else if (7.8f<= rating && rating<=8.1f) {
//                color = R.color.VeryGood;
//            } else if (8.2f<= rating && rating<=8.5f) {
//                color = R.color.Great;
//            } else if (8.6f<= rating && rating<=8.9f) {
//                color = R.color.Adept;
//            } else if ( rating >= 9.0f) {
//                color = R.color.Unicum;
//            }
//        }
//
//        return color;
//    }

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