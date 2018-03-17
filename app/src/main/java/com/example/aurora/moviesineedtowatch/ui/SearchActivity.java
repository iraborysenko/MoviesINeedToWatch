package com.example.aurora.moviesineedtowatch.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.aurora.moviesineedtowatch.R;
import com.example.aurora.moviesineedtowatch.gson.MovieDeserializer;
import com.example.aurora.moviesineedtowatch.gson.SearchMovieDeserializer;
import com.example.aurora.moviesineedtowatch.gson.SearchResultDeserializer;
import com.example.aurora.moviesineedtowatch.retrofit.ApiClient;
import com.example.aurora.moviesineedtowatch.retrofit.ApiInterface;
import com.example.aurora.moviesineedtowatch.tmdb.API;
import com.example.aurora.moviesineedtowatch.tmdb.Const;
import com.example.aurora.moviesineedtowatch.tmdb.DB;
import com.example.aurora.moviesineedtowatch.tmdb.MovieBuilder;
import com.example.aurora.moviesineedtowatch.tmdb.SearchMovieBuilder;
import com.example.aurora.moviesineedtowatch.tmdb.SearchResultBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.aurora.moviesineedtowatch.tmdb.Const.EN;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.EN1;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.RU;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.IMAGE_PATH;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.IMAGE_SIZE;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.QUERY;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.RU1;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.SEE;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.SHARED_REFERENCES;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.TMDB_MOVIE;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.TMDB_SEARCH;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.genres;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 25/01/18
 * Time: 20:41
 */

public class SearchActivity extends AppCompatActivity {
    private Switch s;
    private ProgressBar progressBar;
    private TextView mNotificationField;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_search);

        final EditText editText = findViewById(R.id.search_query);
        ImageButton button = findViewById(R.id.search_button);
        mNotificationField = findViewById(R.id.notificationField);

        editText.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    runSearch(editText);
                    return true;
                }
                return false;
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                runSearch(editText);
            }
        });

        s = findViewById(R.id.switchToEN);
        SharedPreferences settings = getSharedPreferences(SHARED_REFERENCES, MODE_PRIVATE);
        boolean set = settings.getBoolean("lang_key", false);
        s.setChecked(set);

        s.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                SharedPreferences.Editor editor = getSharedPreferences(SHARED_REFERENCES, MODE_PRIVATE).edit();
                editor.putBoolean("lang_key", s.isChecked());
                editor.apply();
            }
        });




        //retrofit part

        try {
            String searchQuery = "Amelie";
            String encodedQuery = URLEncoder.encode(searchQuery, "UTF-8");

            ApiInterface apiService =
                    ApiClient.getClient1().create(ApiInterface.class);

            Call<SearchResultBuilder> call = apiService.getSearchResult((s.isChecked()?EN1:RU1),
                    API.KEY, encodedQuery);
            call.enqueue(new Callback<SearchResultBuilder>() {
                @Override
                public void onResponse(Call<SearchResultBuilder>call, Response<SearchResultBuilder> response) {
                    SearchResultBuilder result = response.body();
                    assert result != null;
                    Log.e(Const.TAG, "Result data: " + result.getResults()[0].getTitle());
                }

                @Override
                public void onFailure(Call<SearchResultBuilder>call, Throwable t) {
                    Log.e(SEE, t.toString());
                }
            });

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }





    }


    @SuppressLint("SetTextI18n")
    void runSearch(EditText editText) {
        String searchQuery = editText.getText().toString();
        Log.e(Const.DEBUG, searchQuery);
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connMgr != null;
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new TMDBSearchManager().execute(searchQuery);
        } else {
            TextView textView = new TextView(SearchActivity.this);
            textView.setText("No network connection.");
            setContentView(textView);
            Log.e(Const.ERR, "stepErr");
        }
    }

    class TMDBSearchManager extends AsyncTask <String, Void, SearchResultBuilder>{

        @Override
        protected void onPreExecute() {
            progressBar = findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected SearchResultBuilder doInBackground(String... params) {
            String searchQuery = params[0];
            try {
                return search(searchQuery);
            } catch (IOException e) {
                return null;
            }
        }

        @SuppressLint("ResourceType")
        @Override
        protected void onPostExecute(SearchResultBuilder searchResult) {
            Log.e(SEE, searchResult.getTotalResults());

            SearchMovieBuilder[] search = searchResult.getResults();
            TableLayout mTable = findViewById(R.id.searchTable);

            mTable.removeAllViewsInLayout();
            mNotificationField.setText(String.format("Total amount: %s", searchResult.getTotalResults()));
            mNotificationField.setTextSize(20);

            final Typeface font = Typeface.createFromAsset(getAssets(), "comic_relief.ttf");

            for (SearchMovieBuilder aSearch : search) {
                RelativeLayout tr = new RelativeLayout(SearchActivity.this);

                final String movieId = String.valueOf(aSearch.getId());

                //get poster
                ImageView mPoster = new ImageView(SearchActivity.this);
                mPoster.setId(1);
                String imagePath = IMAGE_PATH + IMAGE_SIZE[3] + aSearch.getPosterPath();
                RequestOptions options = new RequestOptions()
                        .error(R.drawable.noposter)
                        .skipMemoryCache(true);

                Glide.with(SearchActivity.this)
                        .load(imagePath)
                        .apply(options)
                        .into(mPoster);

                //title
                TextView mTitle = new TextView(SearchActivity.this);
                mTitle.setId(2);
                mTitle.setText(aSearch.getTitle());
                mTitle.setTypeface(font, Typeface.BOLD);
                mTitle.setTextColor(getResources().getColor(R.color.colorBlue));
                mTitle.setGravity(Gravity.CENTER);
                mTitle.setTextSize(16);

                //original title
                TextView mOTitle = new TextView(SearchActivity.this);
                mOTitle.setId(3);
                mOTitle.setText(aSearch.getOriginalTitle());
                mOTitle.setTypeface(font, Typeface.ITALIC);

                //get genres
                StringBuilder genresString = new StringBuilder();
                if (aSearch.getGenreIds().isEmpty())
                    genresString = new StringBuilder("not defined");
                else {
                    for (Integer genreId : aSearch.getGenreIds()) {
                        genresString.append(genres.get(genreId)[s.isChecked() ? 0 : 1]).append("\n");
                    }
                }
                TextView mGenres = new TextView(SearchActivity.this);
                mGenres.setId(4);
                mGenres.setText(String.valueOf(genresString.toString()));
                mGenres.setTypeface(font, Typeface.BOLD);
                mGenres.setTextColor(getResources().getColor(R.color.colorLightBlue));

                //get TMDb rating
                TextView mTMDb = new TextView(SearchActivity.this);
                mTMDb.setId(5);
                mTMDb.setText(String.valueOf(aSearch.getVoteAverage()));
                mTMDb.setTypeface(font, Typeface.BOLD);
                mTMDb.setTextSize(15);

                //get year
                TextView mYear = new TextView(SearchActivity.this);
                mYear.setId(6);
                mYear.setTypeface(font);
                if (!Objects.equals(aSearch.getReleaseDate(), ""))
                    mYear.setText(aSearch.getReleaseDate().subSequence(0, 4));
                else mYear.setText("----");

                RelativeLayout.LayoutParams posterParams = new RelativeLayout.LayoutParams(230, 485);
                posterParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
                posterParams.setMargins(20, 5, 20, 20);
                tr.addView(mPoster, posterParams);

                RelativeLayout.LayoutParams titleParams = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                titleParams.addRule(RelativeLayout.ALIGN_TOP);
                titleParams.setMargins(0, 10, 0, 10);
                tr.addView(mTitle, titleParams);

                RelativeLayout.LayoutParams oTitleParams = new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                oTitleParams.addRule(RelativeLayout.RIGHT_OF, mPoster.getId());
                oTitleParams.addRule(RelativeLayout.BELOW, mTitle.getId());
                oTitleParams.setMargins(0, 0, 10, 20);
                tr.addView(mOTitle, oTitleParams);

                RelativeLayout.LayoutParams genresParams = new RelativeLayout.LayoutParams(
                        320, RelativeLayout.LayoutParams.WRAP_CONTENT);
                genresParams.addRule(RelativeLayout.RIGHT_OF, mPoster.getId());
                genresParams.addRule(RelativeLayout.BELOW, mOTitle.getId());
                genresParams.setMargins(10, 0, 10, 10);
                tr.addView(mGenres, genresParams);

                RelativeLayout.LayoutParams tmdbParams = new RelativeLayout.LayoutParams(
                        50, RelativeLayout.LayoutParams.WRAP_CONTENT);
                tmdbParams.addRule(RelativeLayout.RIGHT_OF, mGenres.getId());
                tmdbParams.addRule(RelativeLayout.BELOW, mOTitle.getId());
                tmdbParams.setMargins(10, 0, 20, 0);
                tr.addView(mTMDb, tmdbParams);

                RelativeLayout.LayoutParams yearParams = new RelativeLayout.LayoutParams(
                        70, RelativeLayout.LayoutParams.WRAP_CONTENT);
                yearParams.addRule(RelativeLayout.RIGHT_OF, mGenres.getId());
                yearParams.addRule(RelativeLayout.BELOW, mTMDb.getId());
                yearParams.setMargins(0, 0, 0, 0);
                tr.addView(mYear, yearParams);

                tr.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        new TMDBMovieManager().execute(movieId);
                        loadMovieInfo(movieId);
                    }
                });


                mTable.addView(tr);
            }
            progressBar.setVisibility(View.INVISIBLE);
            Log.e(Const.DEBUG, "we're on the onPostExecute");
        }

        SearchResultBuilder search(String searchQuery) throws IOException {

            String encodedQuery = URLEncoder.encode(searchQuery, "UTF-8");
            String stringBuilder = TMDB_SEARCH + (s.isChecked()?EN:RU) + "api_key=" + API.KEY + QUERY + encodedQuery;

            URL url = new URL(stringBuilder);
            Log.e(Const.SEE, url.toString());

            InputStream stream = null;
            try {
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.addRequestProperty("Accept", "application/json");
                conn.setDoInput(true);
                conn.connect();
                int responseCode = conn.getResponseCode();
                Log.e(Const.DEBUG, "The response code is: " + responseCode + " " + conn.getResponseMessage());
                stream = conn.getInputStream();
                return parseSearch(stringify(stream));
            } finally {
                if (stream != null) {
                    stream.close();
                }
            }
        }

        private SearchResultBuilder parseSearch(String result) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(SearchResultBuilder.class, new SearchResultDeserializer());
            gsonBuilder.registerTypeAdapter(SearchMovieBuilder.class, new SearchMovieDeserializer());

            Gson customGson = gsonBuilder.create();
            return customGson.fromJson(result, SearchResultBuilder.class);
        }

        String stringify(InputStream stream) throws IOException {
            Reader reader = new InputStreamReader(stream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(reader);
            return bufferedReader.readLine();
        }
    }

    private void loadMovieInfo(String movieId) {

        ApiInterface apiService =
                ApiClient.getClient(getApplicationContext()).create(ApiInterface.class);

        Call<MovieBuilder> call =
                apiService.getMovie(Integer.parseInt(movieId),(s.isChecked()?EN1:RU1), API.KEY);
        call.enqueue(new Callback<MovieBuilder>() {
            @Override
            public void onResponse(Call<MovieBuilder>call, Response<MovieBuilder> response) {
                MovieBuilder movie = response.body();
                assert movie != null;
                Log.e(Const.TAG, "Movie data: " + movie.getTitle());
            }

            @Override
            public void onFailure(Call<MovieBuilder>call, Throwable t) {
                Log.e(SEE, t.toString());
            }
        });
    }

    private class TMDBMovieManager extends AsyncTask <String, Void, MovieBuilder>{

        @Override
        protected void onPreExecute() {
            progressBar = findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected MovieBuilder doInBackground(String... params) {
            String movieId = params[0];
            try {
                return movie(movieId);
            } catch (IOException e) {
                return null;
            }
        }

        @SuppressLint({"DefaultLocale", "SetTextI18n"})
        @Override
        protected void onPostExecute(MovieBuilder movie) {
            DB db1 = new DB(SearchActivity.this);
//            SQLiteDatabase db = db1.getWritableDatabase();
//            db1.onUpgrade(db, 3,4);
            db1.addMovie(movie);

            SharedPreferences.Editor editor = getSharedPreferences(SHARED_REFERENCES, MODE_PRIVATE).edit();
            editor.putBoolean("db_is_changed", true);
            editor.apply();

            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(SearchActivity.this, "Movie added to the wish list", Toast.LENGTH_SHORT).show();
            Log.e(Const.DEBUG, "we're on the onPostExecute of the movie");
        }

        MovieBuilder movie(String movieId) throws IOException {
            String stringBuilder = TMDB_MOVIE + movieId + (s.isChecked()?EN:RU) + "api_key=" + API.KEY;
            URL url = new URL(stringBuilder);
            Log.e(Const.TAG,url.toString());

            InputStream stream = null;
            try {
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.addRequestProperty("Accept", "application/json");
                conn.setDoInput(true);
                conn.connect();
                int responseCode = conn.getResponseCode();
                Log.e(Const.DEBUG, "The response code is: " + responseCode + " " + conn.getResponseMessage());
                stream = conn.getInputStream();
                return parseMovie(stringify(stream));
            } finally {
                if (stream != null) {
                    stream.close();
                }
            }
        }

        private MovieBuilder parseMovie(String result) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(MovieBuilder.class,
                    new MovieDeserializer(getApplicationContext()));

            Gson customGson = gsonBuilder.create();
            return customGson.fromJson(result, MovieBuilder.class);
        }

        String stringify(InputStream stream) throws IOException {
            Reader reader = new InputStreamReader(stream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(reader);
            return bufferedReader.readLine();
        }

    }
}
