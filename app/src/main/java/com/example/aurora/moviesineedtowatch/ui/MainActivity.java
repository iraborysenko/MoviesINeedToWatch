package com.example.aurora.moviesineedtowatch.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.aurora.moviesineedtowatch.R;
import com.example.aurora.moviesineedtowatch.adaprer.MainRecyclerAdapter;
import com.example.aurora.moviesineedtowatch.dagger.DatabaseRealm;
import com.example.aurora.moviesineedtowatch.dagger.Injector;
import com.example.aurora.moviesineedtowatch.dagger.WishList;
import com.example.aurora.moviesineedtowatch.tmdb.Movie;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @SuppressLint("StaticFieldLeak")

    @Inject
    DatabaseRealm databaseRealm;

    @Inject
    WishList wishList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_main);

        Injector.getApplicationComponent().inject(this);
        ButterKnife.bind(this);

        List<Movie> movies = getWishList();
        databaseRealm.addRealmDataChangeListener(movies, initRecyclerView(movies));
    }

    private List<Movie> getWishList() {
        return wishList.findAll();

    }

    @OnClick(R.id.main_search_button)
    void searchTMDB() {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        databaseRealm.close();
    }

    private MainRecyclerAdapter initRecyclerView(List<Movie> movies) {
        final RecyclerView mRecyclerView = findViewById(R.id.main_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        final MainRecyclerAdapter mAdapter = new MainRecyclerAdapter(movies, getApplicationContext(), getResources());
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new MainRecyclerAdapter.ClickListener() {
            @Override
            public void onItemClick(View v, String movieId, String dataLang) {
                movieTMDB(movieId, dataLang);
            }

            @Override
            public void onItemLongClick(View v, String movieId, String dataLang) {
                wishList.deleteSelectedMovie(movieId, dataLang);
            }
        });
        return mAdapter;
    }

    private void movieTMDB(String movieId, String dataLang) {
        Intent intent = new Intent(this, MovieActivity.class);
        intent.putExtra("EXTRA_MOVIE_ID", movieId);
        intent.putExtra("EXTRA_DATA_LANG", dataLang);
        startActivity(intent);
    }
}