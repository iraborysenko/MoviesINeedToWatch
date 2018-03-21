package com.example.aurora.moviesineedtowatch.ui;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.example.aurora.moviesineedtowatch.retrofit.ApiClient;
import com.example.aurora.moviesineedtowatch.retrofit.ApiInterface;
import com.example.aurora.moviesineedtowatch.retrofit.API;
import com.example.aurora.moviesineedtowatch.tmdb.Const;
import com.example.aurora.moviesineedtowatch.tmdb.DB;
import com.example.aurora.moviesineedtowatch.tmdb.MovieBuilder;
import com.example.aurora.moviesineedtowatch.tmdb.SearchMovieBuilder;
import com.example.aurora.moviesineedtowatch.tmdb.SearchResultBuilder;
import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.aurora.moviesineedtowatch.tmdb.Const.EN;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.IMAGE_PATH;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.IMAGE_SIZE;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.RU;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.SEE;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.SHARED_REFERENCES;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.genres;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 25/01/18
 * Time: 20:41
 */

public class SearchActivity extends AppCompatActivity {
    private Switch s;
    public ProgressBar progressBar;
    private TextView mNotificationField;
    private Realm mRealm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_search);

        final EditText editText = findViewById(R.id.search_query);
        ImageButton button = findViewById(R.id.search_button);
        mNotificationField = findViewById(R.id.notificationField);
        progressBar = findViewById(R.id.progressBar);

//        Realm.init(SearchActivity.this);
//        mRealm = Realm.getDefaultInstance();
        mRealm = MainActivity.getRealm();

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

    }

    private void runSearch(EditText editText) {

        String searchQuery = editText.getText().toString();
        Log.e(Const.SEE, searchQuery);

        ApiInterface apiService =
                ApiClient.getClient(getApplicationContext()).create(ApiInterface.class);

        progressBar.setVisibility(View.VISIBLE);

        Call<SearchResultBuilder> call = apiService.getSearchResult((s.isChecked()?EN:RU),
                API.KEY, searchQuery);
        call.enqueue(new Callback<SearchResultBuilder>() {
            @Override
            public void onResponse(@NonNull Call<SearchResultBuilder>call, @NonNull Response<SearchResultBuilder> response) {
                SearchResultBuilder result = response.body();
                assert result != null;
                displaySearchResults(result);
            }

            @Override
            public void onFailure(@NonNull Call<SearchResultBuilder>call, @NonNull Throwable t) {
                Log.e(SEE, t.toString());
            }
        });
    }

    @SuppressLint("ResourceType")
    private void displaySearchResults(SearchResultBuilder searchResult) {
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
                    loadMovieInfo(movieId);
                }
            });


            mTable.addView(tr);
        }
        progressBar.setVisibility(View.INVISIBLE);
        Log.e(Const.DEBUG, "we're on the onPostExecute");

    }

    private void loadMovieInfo(String movieId) {
        progressBar.setVisibility(View.VISIBLE);
        ApiInterface apiService =
                ApiClient.getClient(getApplicationContext()).create(ApiInterface.class);

        Call<MovieBuilder> call =
                apiService.getMovie(Integer.parseInt(movieId),(s.isChecked()?EN:RU), API.KEY);
        call.enqueue(new Callback<MovieBuilder>() {
            @Override
            public void onResponse(@NonNull Call<MovieBuilder>call, @NonNull Response<MovieBuilder> response) {
                MovieBuilder movie = response.body();
                assert movie != null;

                mRealm.beginTransaction();
                mRealm.copyToRealm(movie);
                mRealm.commitTransaction();

                SharedPreferences.Editor editor = getSharedPreferences(SHARED_REFERENCES, MODE_PRIVATE).edit();
                editor.putBoolean("db_is_changed", true);
                editor.apply();

                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(SearchActivity.this, "Movie \""+ movie.getTitle() + "\" added to the wish list", Toast.LENGTH_SHORT).show();
                Log.e(Const.DEBUG, "we're on the onPostExecute of the movie");
            }

            @Override
            public void onFailure(@NonNull Call<MovieBuilder>call, @NonNull Throwable t) {
                Log.e(SEE, t.toString());
            }
        });
    }
}
