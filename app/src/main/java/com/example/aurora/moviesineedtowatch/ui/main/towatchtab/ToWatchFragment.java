package com.example.aurora.moviesineedtowatch.ui.main.towatchtab;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aurora.moviesineedtowatch.R;
import com.example.aurora.moviesineedtowatch.adaprers.ToWatchRecyclerAdapter;
import com.example.aurora.moviesineedtowatch.dagger.blocks.mainsreen.towatchfragment.DaggerToWatchFragmentScreenComponent;
import com.example.aurora.moviesineedtowatch.dagger.blocks.mainsreen.towatchfragment.ToWatchFragmentScreenModule;
import com.example.aurora.moviesineedtowatch.tmdb.Movie;
import com.example.aurora.moviesineedtowatch.ui.movie.MovieActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static io.realm.internal.SyncObjectServerFacade.getApplicationContext;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 08/05/19
 * Time: 20:16
 */
public class ToWatchFragment extends Fragment implements ToWatchFragmentScreen.View {

    @Inject
    ToWatchFragmentPresenter mPresenter;

    @BindView(R.id.to_watch_recycler_view)
    RecyclerView mRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerToWatchFragmentScreenComponent.builder()
                .toWatchFragmentScreenModule(new ToWatchFragmentScreenModule(this))
                .build().inject(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_towatch, container, false);
        ButterKnife.bind(this, view);
        mPresenter.loadMovies();
        return view;
    }

    @Override
    public void movieTMDB(String movieId, String dataLang) {
        Intent intent = new Intent(getActivity(), MovieActivity.class);
        intent.putExtra("EXTRA_MOVIE_ID", movieId);
        intent.putExtra("EXTRA_DATA_LANG", dataLang);
        startActivity(intent);
    }

    @Override
    public ToWatchRecyclerAdapter initRecyclerView(List<Movie> movies) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        final ToWatchRecyclerAdapter mAdapter = new ToWatchRecyclerAdapter(movies, getApplicationContext());
        mRecyclerView.setAdapter(mAdapter);
        return mAdapter;
    }
}
