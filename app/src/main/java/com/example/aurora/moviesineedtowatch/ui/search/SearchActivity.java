package com.example.aurora.moviesineedtowatch.ui.search;

import static com.example.aurora.moviesineedtowatch.tmdb.Const.EN;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.RU;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.SHARED_LANG_KEY;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aurora.moviesineedtowatch.App;
import com.example.aurora.moviesineedtowatch.R;
import com.example.aurora.moviesineedtowatch.adaprer.SearchRecyclerAdapter;
import com.example.aurora.moviesineedtowatch.dagger.SharedPreferencesSettings;
import com.example.aurora.moviesineedtowatch.dagger.wishlist.WishList;
import com.example.aurora.moviesineedtowatch.retrofit.ApiInterface;
import com.example.aurora.moviesineedtowatch.retrofit.API;
import com.example.aurora.moviesineedtowatch.tmdb.Const;
import com.example.aurora.moviesineedtowatch.tmdb.Movie;
import com.example.aurora.moviesineedtowatch.tmdb.FoundMovie;
import com.example.aurora.moviesineedtowatch.tmdb.SearchResult;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.Objects;

import javax.inject.Inject;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 25/01/18
 * Time: 20:41
 */
public class SearchActivity extends AppCompatActivity {

    @Inject
    WishList wishList;

    @Inject
    ApiInterface apiInterface;

    @Inject
    SharedPreferencesSettings sharedPreferencesSettings;

    @BindView(R.id.switchToEN) Switch mSwitch;
    @BindView(R.id.notificationField) TextView mNotificationField;
    @BindView(R.id.progressBar) ProgressBar mProgressBar;
    @BindView(R.id.search_query) EditText editText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_search);

        ((App) getApplicationContext()).getApplicationComponent().inject(this);

        ButterKnife.bind(this);

        editText.setOnKeyListener((v, keyCode, event) -> {
            if(event.getAction() == KeyEvent.ACTION_DOWN &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                runSearch();
                return true;
            }
            return false;
        });

        mSwitch.setChecked(sharedPreferencesSettings.getData(SHARED_LANG_KEY));
    }

    @OnClick(R.id.search_button)
    void runSearch() {

        String searchQuery = editText.getText().toString();
        Log.d(Const.SEE, searchQuery);

        mProgressBar.setVisibility(View.VISIBLE);

        Call<SearchResult> call = apiInterface.getSearchResult((mSwitch.isChecked()?EN:RU),
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
                mProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(@NonNull Call<SearchResult>call, @NonNull Throwable t) {
                Log.e(Const.SEE, t.toString());
            }
        });
    }

    @OnClick(R.id.switchToEN)
    void saveSwitchState() {
        sharedPreferencesSettings.putData(SHARED_LANG_KEY, mSwitch.isChecked());
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
                Log.d(Const.DEBUG, "It's onclick");
            }

            @Override
            public void onItemLongClick(View v, String movieId) {
                loadMovieInfo(movieId);
            }
        });
    }

    private void loadMovieInfo(String movieId) {
        mProgressBar.setVisibility(View.VISIBLE);

        Call<Movie> call =
                apiInterface.getMovie(Integer.parseInt(movieId),(mSwitch.isChecked()?EN:RU), API.KEY);
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(@NonNull Call<Movie>call, @NonNull Response<Movie> response) {
                Movie movie = response.body();
                assert movie != null;
                wishList.addSelectedMovie(movie);
                mProgressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(SearchActivity.this, "Movie \""+ movie.getTitle()
                        + "\" added to the wish list", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NonNull Call<Movie>call, @NonNull Throwable t) {
                Log.e(Const.SEE, t.toString());
            }
        });
    }
}