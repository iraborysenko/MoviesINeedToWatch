package com.example.aurora.moviesineedtowatch.ui.main.watchedtab;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aurora.moviesineedtowatch.R;
import com.example.aurora.moviesineedtowatch.adaprers.WatchedRecyclerAdapter;
import com.example.aurora.moviesineedtowatch.dagger.blocks.mainsreen.watchedfragment.DaggerWatchedFragmentScreenComponent;
import com.example.aurora.moviesineedtowatch.dagger.blocks.mainsreen.watchedfragment.WatchedFragmentScreenModule;
import com.example.aurora.moviesineedtowatch.tmdb.Movie;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static io.realm.internal.SyncObjectServerFacade.getApplicationContext;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 08/05/19
 * Time: 20:17
 */
public class WatchedFragment extends Fragment implements WatchedFragmentScreen.View {

    @Inject
    WatchedFragmentPresenter mPresenter;

    @BindView(R.id.watched_recycler_view)
    RecyclerView mRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerWatchedFragmentScreenComponent.builder()
                .watchedFragmentScreenModule(new WatchedFragmentScreenModule(this))
                .build().inject(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_watched, container, false);
        ButterKnife.bind(this, view);
        mPresenter.loadWatchedMovies();
        return view;
    }

    @Override
    public WatchedRecyclerAdapter initRecyclerView(List<Movie> movies) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        final WatchedRecyclerAdapter mAdapter = new WatchedRecyclerAdapter(movies, getApplicationContext());
        mRecyclerView.setAdapter(mAdapter);
        return mAdapter;
    }

    @Override
    public void movieWatchedDetails(String movieId, String dataLang) {

    }
}
