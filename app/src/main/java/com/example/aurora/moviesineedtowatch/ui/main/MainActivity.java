package com.example.aurora.moviesineedtowatch.ui.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.aurora.moviesineedtowatch.R;
import com.example.aurora.moviesineedtowatch.adaprer.MainRecyclerAdapter;
import com.example.aurora.moviesineedtowatch.dagger.component.DaggerMainScreenComponent;
import com.example.aurora.moviesineedtowatch.dagger.module.screens.MainScreenModule;
import com.example.aurora.moviesineedtowatch.tmdb.Movie;
import com.example.aurora.moviesineedtowatch.ui.movie.MovieActivity;
import com.example.aurora.moviesineedtowatch.ui.search.SearchActivity;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity  implements MainScreen.View {
    @SuppressLint("StaticFieldLeak")

    @Inject
    MainPresenter mainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_main);

        DaggerMainScreenComponent.builder()
                .mainScreenModule(new MainScreenModule(this))
                .build().inject(this);

        ButterKnife.bind(this);

        mainPresenter.loadMovies();
    }

    @OnClick(R.id.main_search_button)
    public void searchButtonClicked() {
        mainPresenter.searchButton();
    }

    @Override
    public void searchTMDB() {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    @Override
    public void movieTMDB(String movieId, String dataLang) {
        Intent intent = new Intent(this, MovieActivity.class);
        intent.putExtra("EXTRA_MOVIE_ID", movieId);
        intent.putExtra("EXTRA_DATA_LANG", dataLang);
        startActivity(intent);
    }

    @Override
    public MainRecyclerAdapter initRecyclerView(List<Movie> movies) {
        final RecyclerView mRecyclerView = findViewById(R.id.main_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        final MainRecyclerAdapter mAdapter = new MainRecyclerAdapter(movies, getApplicationContext());
        mRecyclerView.setAdapter(mAdapter);
        return mAdapter;
    }
}