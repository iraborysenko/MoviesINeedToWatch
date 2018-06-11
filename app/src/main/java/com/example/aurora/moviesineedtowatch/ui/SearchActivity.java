package com.example.aurora.moviesineedtowatch.ui;

import static com.example.aurora.moviesineedtowatch.tmdb.Const.DEBUG;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.EN;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.RU;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.SEE;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.SHARED_REFERENCES;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aurora.moviesineedtowatch.R;
import com.example.aurora.moviesineedtowatch.adaprer.SearchRecyclerAdapter;
import com.example.aurora.moviesineedtowatch.retrofit.ApiClient;
import com.example.aurora.moviesineedtowatch.retrofit.ApiInterface;
import com.example.aurora.moviesineedtowatch.retrofit.API;
import com.example.aurora.moviesineedtowatch.tmdb.Const;
import com.example.aurora.moviesineedtowatch.tmdb.Movie;
import com.example.aurora.moviesineedtowatch.tmdb.FoundMovie;
import com.example.aurora.moviesineedtowatch.tmdb.SearchResult;

import io.realm.Realm;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.Objects;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 25/01/18
 * Time: 20:41
 */
public class SearchActivity extends AppCompatActivity {
    private Switch mSwitch;
    public ProgressBar progressBar;
    private TextView mNotificationField;
    private Realm mRealm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_search);

        final EditText editText = findViewById(R.id.search_query);
        ImageButton button = findViewById(R.id.search_button);
        mNotificationField = findViewById(R.id.notificationField);
        progressBar = findViewById(R.id.progressBar);

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

        mSwitch = findViewById(R.id.switchToEN);
        SharedPreferences settings = getSharedPreferences(SHARED_REFERENCES, MODE_PRIVATE);
        boolean set = settings.getBoolean("lang_key", false);
        mSwitch.setChecked(set);

        mSwitch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                SharedPreferences.Editor editor = getSharedPreferences(SHARED_REFERENCES, MODE_PRIVATE).edit();
                editor.putBoolean("lang_key", mSwitch.isChecked());
                editor.apply();
            }
        });

    }

    private void runSearch(EditText editText) {

        String searchQuery = editText.getText().toString();
        Log.d(Const.SEE, searchQuery);

        ApiInterface apiService =
                ApiClient.getClient(getApplicationContext()).create(ApiInterface.class);

        progressBar.setVisibility(View.VISIBLE);

        Call<SearchResult> call = apiService.getSearchResult((mSwitch.isChecked()?EN:RU),
                API.KEY, searchQuery);
        call.enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(@NonNull Call<SearchResult>call, @NonNull Response<SearchResult> response) {
                SearchResult result = response.body();
                assert result != null;

                mNotificationField.setText(String.format("Total amount: %s", result.getTotalResults()));
                mNotificationField.setTextSize(20);
                FoundMovie[] search = result.getResults();
                initRecyclerView(search);
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(@NonNull Call<SearchResult>call, @NonNull Throwable t) {
                Log.e(SEE, t.toString());
            }
        });
    }

    private void initRecyclerView(FoundMovie[] search) {
        final RecyclerView mRecyclerView = findViewById(R.id.search_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        final SearchRecyclerAdapter mAdapter =
                new SearchRecyclerAdapter(search, getApplicationContext(), mSwitch);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new SearchRecyclerAdapter.ClickListener() {
            @Override
            public void onItemClick(View v, String movieId) {
                Log.d(DEBUG, "It's onclick");
            }

            @Override
            public void onItemLongClick(View v, String movieId) {
                loadMovieInfo(movieId);
            }
        });
    }

    private void loadMovieInfo(String movieId) {
        progressBar.setVisibility(View.VISIBLE);
        ApiInterface apiService =
                ApiClient.getClient(getApplicationContext()).create(ApiInterface.class);

        Call<Movie> call =
                apiService.getMovie(Integer.parseInt(movieId),(mSwitch.isChecked()?EN:RU), API.KEY);
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(@NonNull Call<Movie>call, @NonNull Response<Movie> response) {
                Movie movie = response.body();
                assert movie != null;

                mRealm.beginTransaction();
                mRealm.copyToRealm(movie);
                mRealm.commitTransaction();

                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(SearchActivity.this, "Movie \""+ movie.getTitle()
                        + "\" added to the wish list", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NonNull Call<Movie>call, @NonNull Throwable t) {
                Log.e(SEE, t.toString());
            }
        });
    }
}
